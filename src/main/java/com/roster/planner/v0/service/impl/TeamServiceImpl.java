package com.roster.planner.v0.service.impl;

import com.roster.planner.v0.entity.Team;
import com.roster.planner.v0.entity.User;
import com.roster.planner.v0.repository.TeamRepository;
import com.roster.planner.v0.request.TeamRequest;
import com.roster.planner.v0.response.TeamResponse;
import com.roster.planner.v0.service.interfaces.TeamService;
import com.roster.planner.v0.util.IdentifierGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepo;
    private final UserServiceImpl userService;

    private TeamResponse convertor(Team team) {

        Set<Map<String, String>> teamMemDetails = new HashSet<>();

        team.getTeamMembers().forEach((member) -> {
            Map<String, String> memberDetails = new HashMap<>();

            memberDetails.put("name", member.getName());
            memberDetails.put("email", member.getEmail());

            teamMemDetails.add(memberDetails);
        });

        User pmObj = team.getProjectManager();
        User tlObj = team.getTeamLead();

        return TeamResponse.builder()
                .teamUID(team.getTeamUID())
                .teamName(team.getTeamName())
                .projectManager(new TeamResponse.ProjectManager(pmObj.getEmpUID(), pmObj.getName()))
                .teamLead(new TeamResponse.TeamLead(tlObj == null ? null : tlObj.getEmpUID(), tlObj == null ? null: tlObj.getName()))
                .teamMembersDetails(teamMemDetails)
                .build();
    }

    @Override
    public TeamResponse createTeam(String createdByEmailID, TeamRequest teamReq) {
        String teamName = teamReq.getName();

        TeamRequest.ProjectManager pmObj = teamReq.getPmObj();

        String pmUID = pmObj.getPmUID();

        User existingPmObj = userService.getUserByEmpUID(pmUID);
        User existingTeamLeadObj = userService.getUserByEmail(createdByEmailID);

        String teamUID = IdentifierGenerator.generate("tea");

        Team teamObj = new Team();

        teamObj.setTeamUID(teamUID);
        teamObj.setTeamName(teamName);
        teamObj.setProjectManager(existingPmObj);
        teamObj.setTeamLead(existingTeamLeadObj);

        Team savedTeam = teamRepo.save(teamObj);

        return convertor(savedTeam);
    }

    @Override
    public TeamResponse getTeam(String teamUID) {
        Team team = teamRepo.findByTeamUID(teamUID).orElseThrow(() -> new RuntimeException("Team with " + teamUID + " not found!"));
        return convertor(team);
    }

    @Override
    public TeamResponse updateTeam(String updatedByEmailID, String teamUID, TeamRequest teamReq) {
        Team existingTeam = teamRepo.findByTeamUID(teamUID).orElseThrow(() -> new RuntimeException("Team with " + teamUID + " not found!"));

        User projectManager = userService.getUserByEmpUID(teamReq.getPmObj().getPmUID());
        User teamLead = userService.getUserByEmail(updatedByEmailID);

        existingTeam.setTeamName(teamReq.getName());
        existingTeam.setProjectManager(projectManager);
        existingTeam.setTeamLead(teamLead);
        existingTeam.setUpdatedAt(LocalDateTime.now());

        Team savedTeam = teamRepo.save(existingTeam);

        return convertor(savedTeam);
    }

    @Override
    @Transactional
    public void deleteTeam(String teamUID) {

        Team team = teamRepo.findByTeamUID(teamUID)
                .orElseThrow(() ->
                        new RuntimeException("Team not found"));

        // Remove team reference from all users
        for (User user : team.getTeamMembers()) {
            user.setTeam(null);
        }

        userService.saveAllUsers(team.getTeamMembers());

        // Now delete the team
        teamRepo.delete(team);
    }
}
