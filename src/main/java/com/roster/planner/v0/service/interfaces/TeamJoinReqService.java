package com.roster.planner.v0.service.interfaces;

import com.roster.planner.v0.enums.RequestStatus;
import com.roster.planner.v0.request.TeamJoinReqDTO;
import com.roster.planner.v0.response.TeamJoinReqResDTO;

import java.util.List;

public interface TeamJoinReqService {
    TeamJoinReqResDTO createJoinReq(TeamJoinReqDTO reqDetails);
    TeamJoinReqResDTO updateJoinReq(String reqUID, TeamJoinReqDTO reqDetails);
    TeamJoinReqResDTO updateTeamJoinReqStatus(String approvedEmpEmail, String teamJoinReqUID, RequestStatus reqStatus);
    TeamJoinReqResDTO readJoinReq(String reqUID);
    List<TeamJoinReqResDTO> readAllJoinReqOfTeam(String teamUID);
    void deleteJoinReq(String reqUID);

}
