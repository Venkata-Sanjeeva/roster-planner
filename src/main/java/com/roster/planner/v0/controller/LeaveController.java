package com.roster.planner.v0.controller;

import com.roster.planner.v0.enums.RequestStatus;
import com.roster.planner.v0.request.LeaveRequest;
import com.roster.planner.v0.response.GlobalResponse;
import com.roster.planner.v0.response.LeaveResponse;
import com.roster.planner.v0.service.impl.LeaveServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
@RequiredArgsConstructor
public class LeaveController {

    private final LeaveServiceImpl leaveService;

    @PostMapping("/emp/create")
    public ResponseEntity<GlobalResponse<LeaveResponse>> createLeave(@RequestBody LeaveRequest leaveReq) {
        LeaveResponse response = leaveService.createLeaveRequest(leaveReq);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.<LeaveResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .data(response)
                        .message("Leave Request Created Successfully!!!")
                        .build());
    }

    @GetMapping("/emp/read/{leaveUID}")
    public ResponseEntity<GlobalResponse<LeaveResponse>> readEmpLeave(Principal userDetails, @PathVariable String leaveUID) {
        LeaveResponse response = leaveService.readLeaveOfEmp(userDetails.getName(), leaveUID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<LeaveResponse>builder()
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .message("Leave fetched successfully...")
                        .build());
    }

    @GetMapping("/emp/read/all")
    public ResponseEntity<GlobalResponse<List<LeaveResponse>>> readAllEmpLeave(Principal userDetails) {
        List<LeaveResponse> response = leaveService.readAllLeavesOfEmp(userDetails.getName());

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<List<LeaveResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .message("Leaves of an Employee fetched successfully...")
                        .build());
    }

    @PatchMapping("/lead/update/{leaveUID}/{leaveStatus}")
    public ResponseEntity<GlobalResponse<LeaveResponse>> updateSingleLeaveReqStatus(
            Principal userDetails,
            @PathVariable String leaveUID,
            @PathVariable RequestStatus leaveStatus) {

        LeaveResponse response = leaveService.updateSingleLeaveRequestStatus(userDetails.getName(), leaveUID, leaveStatus);

        return ResponseEntity.status(HttpStatus.OK.value())
                .body(GlobalResponse.<LeaveResponse>builder()
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .message("Leave Request status of an Employee updated successfully...")
                        .build());
    }

    @PatchMapping("/lead/update/all")
    public ResponseEntity<GlobalResponse<List<LeaveResponse>>> updateEmpLeaveReqStatus(
            Principal userDetails,
            @RequestParam String requestedByEmpUID,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam RequestStatus leavesStatus
            ) {
        List<LeaveResponse> response = leaveService.updateEmpLeaveRequestStatus(userDetails.getName(), requestedByEmpUID, startDate, endDate, leavesStatus);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<List<LeaveResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .message("Employee Leaves Request status updated successfully...")
                        .build());
    }

    @DeleteMapping("/emp/delete/{leaveUID}")
    public ResponseEntity<GlobalResponse<String>> deleteEmpLeaveReq(Principal userDetails, @PathVariable String leaveUID) {

        leaveService.deleteLeave(userDetails.getName(), leaveUID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .data("Leave Request with UID " + leaveUID + " deleted successfully.")
                        .message("Deleted Successfully...")
                        .build());
    }

}
