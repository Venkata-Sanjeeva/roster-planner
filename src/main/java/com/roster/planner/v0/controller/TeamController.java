package com.roster.planner.v0.controller;

import com.roster.planner.v0.request.TeamRequest;
import com.roster.planner.v0.response.GlobalResponse;
import com.roster.planner.v0.response.TeamResponse;
import com.roster.planner.v0.service.impl.TeamServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamServiceImpl teamService;

    @PostMapping("/lead/createTeam")
    public ResponseEntity<GlobalResponse<TeamResponse>> createTeam(Principal userObj, @RequestBody TeamRequest teamReq) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.<TeamResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .data(teamService.createTeam(userObj.getName(), teamReq))
                        .message("Team created successfully!")
                        .build());
    }

    @GetMapping("/fetch/{teamUID}")
    public ResponseEntity<GlobalResponse<TeamResponse>> getTeam(@PathVariable String teamUID) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<TeamResponse>builder()
                        .status(HttpStatus.OK.value())
                        .data(teamService.getTeam(teamUID))
                        .message("Team fetched successfully...")
                        .build());
    }

    @PutMapping("/lead/updateTeam/{teamUID}")
    public ResponseEntity<GlobalResponse<TeamResponse>> updateTeamDetails(Principal userObj, @PathVariable String teamUID, @RequestBody TeamRequest teamReq) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<TeamResponse>builder()
                        .status(HttpStatus.OK.value())
                        .data(teamService.updateTeam(userObj.getName(), teamUID, teamReq))
                        .message("Team updated successfully!")
                        .build());
    }

    @DeleteMapping("/lead/deleteTeam/{teamUID}")
    public ResponseEntity<GlobalResponse<String>> deleteTeam(@PathVariable String teamUID) {
        teamService.deleteTeam(teamUID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .data(null)
                        .message("Team deleted successfully!")
                        .build());
    }
}
