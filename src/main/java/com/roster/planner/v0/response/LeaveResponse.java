package com.roster.planner.v0.response;

import com.roster.planner.v0.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class LeaveResponse {
    private String leaveUID;
    private LocalDate startDate;
    private LocalDate endDate;
    private RequestStatus leaveStatus;
    private LocalDateTime createdAt;

    @Data
    @AllArgsConstructor
    public static class EmpDetails {
        private String empUID;
        private String empName;
    }

    private EmpDetails requestedBy;
    private EmpDetails approvedBy;
    private LocalDateTime approvedAt;
}
