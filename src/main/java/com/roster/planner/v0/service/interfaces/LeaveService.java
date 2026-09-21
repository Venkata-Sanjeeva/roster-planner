package com.roster.planner.v0.service.interfaces;

import com.roster.planner.v0.enums.RequestStatus;
import com.roster.planner.v0.request.LeaveRequest;
import com.roster.planner.v0.response.LeaveResponse;

import java.time.LocalDate;
import java.util.List;

public interface LeaveService {
    LeaveResponse createLeaveRequest(LeaveRequest leaveReq);
    LeaveResponse updateSingleLeaveRequestStatus(String approvedByEmpEmail, String leaveUID, RequestStatus leaveStatus);
    List<LeaveResponse> updateEmpLeaveRequestStatus(String approvedByEmpEmail, String requestedByEmpUID, LocalDate startDate, LocalDate endDate, RequestStatus leavesStatus);
    LeaveResponse readLeaveOfEmp(String empEmail, String leaveUID);
    List<LeaveResponse> readAllLeavesOfEmp(String empEmail);
    void deleteLeave(String empEmail, String leaveUID);
}
