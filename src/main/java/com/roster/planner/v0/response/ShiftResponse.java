package com.roster.planner.v0.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class ShiftResponse {

    private String shiftUID;
    private String name;
    private String shortName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private LocalTime startTime;
    private LocalTime endTime;

    private String teamUID;

}
