package com.roster.planner.v0.service.impl;

import com.roster.planner.v0.entity.Leave;
import com.roster.planner.v0.entity.User;
import com.roster.planner.v0.enums.RequestStatus;
import com.roster.planner.v0.repository.LeaveRepository;
import com.roster.planner.v0.request.LeaveRequest;
import com.roster.planner.v0.response.LeaveResponse;
import com.roster.planner.v0.service.interfaces.LeaveService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveServiceImpl implements LeaveService {

    private final LeaveRepository leaveRepo;
    private final UserServiceImpl userService;

    private static LeaveResponse convertor(Leave leaveObj) {
        User requestedByEmpDetails = leaveObj.getRequestedBy();
        User approvedByEmpDetails = leaveObj.getApprovedBy();


        LeaveResponse response = LeaveResponse.builder()
                .leaveUID(leaveObj.getLeaveUID())
                .leaveStatus(leaveObj.getLeaveStatus())
                .startDate(leaveObj.getStartDate())
                .endDate(leaveObj.getEndDate())
                .createdAt(leaveObj.getCreatedAt())
                .requestedBy(new LeaveResponse.EmpDetails(requestedByEmpDetails.getEmpUID(), requestedByEmpDetails.getName()))
                .build();

        if(approvedByEmpDetails != null) {
            response.setApprovedBy(new LeaveResponse.EmpDetails(approvedByEmpDetails.getEmpUID(), approvedByEmpDetails.getName()));
            response.setApprovedAt(leaveObj.getApprovedAt());
        }

        return response;
    }

    @Override
    public LeaveResponse createLeaveRequest(LeaveRequest leaveReq) {
        User requestedEmpObj = userService.getUserByEmpUID(leaveReq.getRequestedByEmpUID());

        Leave leaveObj = new Leave();

        leaveObj.setRequestedBy(requestedEmpObj);
        leaveObj.setLeaveStatus(RequestStatus.PENDING);
        leaveObj.setStartDate(leaveReq.getStartDate());
        leaveObj.setEndDate(leaveReq.getEndDate());

        Leave savedLeaveObj = leaveRepo.save(leaveObj);

        return convertor(savedLeaveObj);
    }

    @Override
    public List<LeaveResponse> readAllLeavesOfEmp(String empEmail) {

        List<Leave> empLeavesList = leaveRepo.findByRequestedBy_Email(empEmail);

        return empLeavesList.stream().map(LeaveServiceImpl::convertor).toList();
    }

    @Override
    public LeaveResponse readLeaveOfEmp(String empEmail, String leaveUID) {
        Leave leaveObj = leaveRepo.findByLeaveUIDAndRequestedByEmail(leaveUID, empEmail).orElseThrow(() -> new RuntimeException("Leave with " + leaveUID + " not found!"));

        return convertor(leaveObj);
    }

    @Override
    public LeaveResponse updateSingleLeaveRequestStatus(String approvedByEmpEmail, String leaveUID, RequestStatus leaveStatus) {

        Leave leaveObj = leaveRepo.findByLeaveUID(leaveUID).orElseThrow(() -> new RuntimeException("Leave with " + leaveUID + " not found!"));

        User approvedByEmpObj = userService.getUserByEmail(approvedByEmpEmail);

        if (leaveStatus == RequestStatus.APPROVED) {

            leaveObj.setApprovedBy(approvedByEmpObj);
            leaveObj.setApprovedAt(LocalDateTime.now());

        } else if (leaveStatus == RequestStatus.REJECTED) {

            leaveObj.setApprovedBy(null);
            leaveObj.setApprovedAt(null);
        }

        leaveObj.setLeaveStatus(leaveStatus);

        Leave savedLeaveObj = leaveRepo.save(leaveObj);

        return convertor(savedLeaveObj);
    }

    @Override
    public List<LeaveResponse> updateEmpLeaveRequestStatus(String approvedByEmpEmail, String requestedByEmpUID, LocalDate startDate, LocalDate endDate, RequestStatus leavesStatus) {
        User leavesReqByEmpObj = userService.getUserByEmpUID(requestedByEmpUID);
        User approvedByEmpObj = userService.getUserByEmail(approvedByEmpEmail);

        List<Leave> empLeavesListInRange = leaveRepo.findByRequestedByAndStartDateGreaterThanEqualAndEndDateLessThanEqual(leavesReqByEmpObj, startDate, endDate);

        empLeavesListInRange.forEach(leaveObj -> {
            if (leavesStatus == RequestStatus.APPROVED) {

                leaveObj.setApprovedBy(approvedByEmpObj);
                leaveObj.setApprovedAt(LocalDateTime.now());

            } else if (leavesStatus == RequestStatus.REJECTED) {

                leaveObj.setApprovedBy(null);
                leaveObj.setApprovedAt(null);
            }
            leaveObj.setLeaveStatus(leavesStatus);
        });

        return leaveRepo.saveAll(empLeavesListInRange).stream().map(LeaveServiceImpl :: convertor).toList();
    }

    /*
        * leaves.requested_by → users.id
        * When you delete the Leave row, its foreign-key reference disappears automatically because the row itself is being deleted.

    */

    @Override
    @Transactional
    public void deleteLeave(String empEmail, String leaveUID) {

        Leave leave = leaveRepo
                .findByLeaveUIDAndRequestedByEmail(leaveUID, empEmail)
                .orElseThrow(() -> new RuntimeException(
                        "Leave not found or does not belong to this employee"
                ));

        if (leave.getLeaveStatus() == RequestStatus.APPROVED) {
            throw new IllegalStateException(
                    "Approved leave cannot be deleted"
            );
        }

        leaveRepo.delete(leave);
    }
}
