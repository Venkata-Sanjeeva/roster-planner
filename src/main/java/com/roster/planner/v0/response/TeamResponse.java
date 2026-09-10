package com.roster.planner.v0.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

@Data
@Builder
public class TeamResponse {
    private String teamUID;
    private String teamName;

    @Data
    @AllArgsConstructor
    public static class ProjectManager {
        private String pmUID;
        private String pmName;
    }

    @Data
    @AllArgsConstructor
    public static class TeamLead {
        private String tlUID;
        private String tlName;
    }

    private ProjectManager projectManager;
    private TeamLead teamLead;
    private Set<Map<String, String>> teamMembersDetails;
}
