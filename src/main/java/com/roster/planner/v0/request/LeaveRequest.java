package com.roster.planner.v0.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private String requestedByEmpUID;
}
