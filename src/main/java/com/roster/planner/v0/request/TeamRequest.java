package com.roster.planner.v0.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class TeamRequest {
    private String name;

    @Data
    @AllArgsConstructor
    public static class ProjectManager {
        private String pmUID;
        private String pmName;
    }
    private ProjectManager pmObj;
}
