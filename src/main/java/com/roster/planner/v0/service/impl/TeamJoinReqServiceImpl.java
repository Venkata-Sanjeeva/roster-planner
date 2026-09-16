package com.roster.planner.v0.service.impl;

import com.roster.planner.v0.entity.Team;
import com.roster.planner.v0.entity.TeamJoinRequest;
import com.roster.planner.v0.entity.User;
import com.roster.planner.v0.enums.RequestStatus;
import com.roster.planner.v0.repository.TeamJoinReqRepository;
import com.roster.planner.v0.request.TeamJoinReqDTO;
import com.roster.planner.v0.response.TeamJoinReqResDTO;
import com.roster.planner.v0.service.interfaces.TeamJoinReqService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class TeamJoinReqServiceImpl implements TeamJoinReqService {

    private final TeamJoinReqRepository teamJoinReqRepo;
    private final UserServiceImpl userService;
    private final TeamServiceImpl teamService;

    private static TeamJoinReqResDTO convertor(TeamJoinRequest teamJoinReqObj) {
        User empObj = teamJoinReqObj.getRequestedBy();
        Team teamObj = teamJoinReqObj.getTeam();
        User approvedByEmpObj = teamJoinReqObj.getApprovedBy();

        TeamJoinReqResDTO response = new TeamJoinReqResDTO();


        response.setJoinReqUID(teamJoinReqObj.getJoinReqUID());
        response.setReqByEmp(new TeamJoinReqResDTO.ByObj(empObj.getEmpUID(), empObj.getName()));
        response.setReqTeam(new TeamJoinReqResDTO.RequestedTeam(teamObj.getTeamUID(), teamObj.getTeamName()));
        response.setReqStatus(teamJoinReqObj.getStatus());
        response.setCreatedAt(teamJoinReqObj.getCreatedAt());

        if (approvedByEmpObj != null) {
            response.setApprovedBy(new TeamJoinReqResDTO.ByObj(approvedByEmpObj.getEmpUID(), approvedByEmpObj.getName()));
            response.setApprovedAt(teamJoinReqObj.getApprovedAt());
        }

        return response;
    }

    @Override
    public TeamJoinReqResDTO createJoinReq(TeamJoinReqDTO reqDetails) {

        String empUID = reqDetails.getEmpUID();
        String teamUID = reqDetails.getTeamUID();

        User empObj = userService.getUserByEmpUID(empUID);
        Team teamObj = teamService.getOriginalTeamDetails(teamUID);

        TeamJoinRequest teamJoinReq = new TeamJoinRequest();

        teamJoinReq.setRequestedBy(empObj);
        teamJoinReq.setStatus(RequestStatus.CREATED);
        teamJoinReq.setTeam(teamObj);

        TeamJoinRequest savedTeamJoinReq = teamJoinReqRepo.save(teamJoinReq);

        return convertor(savedTeamJoinReq);
    }

    @Override
    public TeamJoinReqResDTO updateJoinReq(String reqUID, TeamJoinReqDTO reqDetails) {
        System.out.println("UPDATE WHOLE JOIN REQUEST FUNCTION CALLED!!!!\nPLS CHECK THE LOGIC....");
        return null;
    }

    @Override
    public TeamJoinReqResDTO updateTeamJoinReqStatus(String approvedEmpEmail, String teamJoinReqUID, RequestStatus reqStatus) {
        TeamJoinRequest joinReqObj = teamJoinReqRepo.findByJoinReqUID(teamJoinReqUID).orElseThrow(() -> new RuntimeException("Team Join Request with UID: " + teamJoinReqUID + " not found!"));
        User approvedEmpObj = userService.getUserByEmail(approvedEmpEmail);
        Team teamObj = joinReqObj.getTeam();

        if(joinReqObj.getStatus() != RequestStatus.APPROVED && reqStatus.equals(RequestStatus.APPROVED)) {
            joinReqObj.setApprovedBy(approvedEmpObj);
            joinReqObj.setApprovedAt(LocalDateTime.now());

            // Assign team to the user.
            User reqUserObj = userService.getUserByEmpUID(joinReqObj.getRequestedBy().getEmpUID());

            reqUserObj.setTeam(teamObj);
            User savedUser = userService.saveUser(reqUserObj);

            // Add employee to the team.
            Set<User> availableTeamMembers = teamObj.getTeamMembers();
            availableTeamMembers.add(savedUser);
            teamObj.setTeamMembers(availableTeamMembers);

            teamService.saveTeam(teamObj);
        }

        joinReqObj.setStatus(reqStatus);
        joinReqObj.setUpdatedAt(LocalDateTime.now());

        TeamJoinRequest savedJoinReq = teamJoinReqRepo.save(joinReqObj);

        return convertor(savedJoinReq);
    }

    @Override
    public TeamJoinReqResDTO readJoinReq(String reqUID) {
        TeamJoinRequest teamJoinReqObj = teamJoinReqRepo.findByJoinReqUID(reqUID).orElseThrow(() -> new RuntimeException("Team Join Request with UID: " + reqUID + " not found!"));
        return convertor(teamJoinReqObj);
    }

    @Override
    public List<TeamJoinReqResDTO> readAllJoinReqOfTeam(String teamUID) {
        List<TeamJoinRequest> teamReqList = teamJoinReqRepo.findByTeam_TeamUID(teamUID);
        return teamReqList.stream().map(TeamJoinReqServiceImpl :: convertor).toList();
    }

    @Override
    public void deleteJoinReq(String reqUID) {
        teamJoinReqRepo.deleteByJoinReqUID(reqUID);
    }
}
