package com.roster.planner.v0.request;

import lombok.Data;

import java.time.LocalTime;

@Data
public class ShiftRequest {
    private String name;
    private String shortName;

    private LocalTime startTime;
    private LocalTime endTime;

}
