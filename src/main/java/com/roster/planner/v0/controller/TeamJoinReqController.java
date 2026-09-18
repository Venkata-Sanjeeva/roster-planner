package com.roster.planner.v0.controller;

import com.roster.planner.v0.enums.RequestStatus;
import com.roster.planner.v0.request.TeamJoinReqDTO;
import com.roster.planner.v0.response.GlobalResponse;
import com.roster.planner.v0.response.TeamJoinReqResDTO;
import com.roster.planner.v0.service.impl.TeamJoinReqServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/joinRequest")
@RequiredArgsConstructor
public class TeamJoinReqController {

    private final TeamJoinReqServiceImpl teamJoinReqService;

    /* In API, Sanjeeava creating a Join_Request for Sundar while Sanjeeva having the Authentication instead of Sundar's Authentication. NEED TO WORK ON THIS ONLY IN BACKEND I BELIEVE */
    @PostMapping("/employee/createJoinReq")
    public ResponseEntity<GlobalResponse<TeamJoinReqResDTO>> createJoinReq(@RequestBody TeamJoinReqDTO joinReqObj) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.<TeamJoinReqResDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .data(teamJoinReqService.createJoinReq(joinReqObj))
                        .message("Join Request created successfully...")
                        .build());
    }

    /* Join Req status is getting APPROVED by any TEAM_LEAD, CONDITION: Only the requested Team's Team_Lead should APPROVE the join_req of  employee */
    @PutMapping("/teamLead/updateReqStatus/{teamJoinReqUID}/{status}")
    public ResponseEntity<GlobalResponse<TeamJoinReqResDTO>> updateJoinReqStatus(Principal userDetailsObj,
                                                                                 @PathVariable String teamJoinReqUID,
                                                                                 @PathVariable String status) {
        RequestStatus reqStatus = RequestStatus.valueOf(status);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<TeamJoinReqResDTO>builder()
                        .status(HttpStatus.OK.value())
                        .data(teamJoinReqService.updateTeamJoinReqStatus(userDetailsObj.getName(), teamJoinReqUID, reqStatus))
                        .message("Request status updated successfully...")
                        .build());

    }

    @GetMapping("/any/read/{teamJoinReqUID}")
    public ResponseEntity<GlobalResponse<TeamJoinReqResDTO>> readJoinReq(@PathVariable String teamJoinReqUID) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<TeamJoinReqResDTO>builder()
                        .status(HttpStatus.OK.value())
                        .data(teamJoinReqService.readJoinReq(teamJoinReqUID))
                        .message("Join Request fetched successfully...")
                        .build());
    }

    @GetMapping("/teamLead/readAll/{teamUID}")
    public ResponseEntity<GlobalResponse<List<TeamJoinReqResDTO>>> readAllTeamJoinReq(@PathVariable String teamUID) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<List<TeamJoinReqResDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .data(teamJoinReqService.readAllJoinReqOfTeam(teamUID))
                        .message("List of Join Requests fetched successfully...")
                        .build());
    }

    @DeleteMapping("/teamLead/delete/{teamJoinReqUID}")
    public ResponseEntity<GlobalResponse<String>> deleteTeamJoinReq(@PathVariable String teamJoinReqUID) {
        teamJoinReqService.deleteJoinReq(teamJoinReqUID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .data("Team Join Request with UID: " + teamJoinReqUID + " deleted successfully...")
                        .build());
    }

}
