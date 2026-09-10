package com.roster.planner.v0.service.interfaces;

import com.roster.planner.v0.request.TeamRequest;
import com.roster.planner.v0.response.TeamResponse;

public interface TeamService {
    TeamResponse createTeam(String createdByEmailID, TeamRequest teamReq);
    TeamResponse getTeam(String teamUID);
    TeamResponse updateTeam(String updatedByEmailID, String teamUID, TeamRequest teamReq);
    void deleteTeam(String teamUID);
}
