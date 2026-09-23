package com.roster.planner.v0.controller;

import com.roster.planner.v0.request.ShiftRequest;
import com.roster.planner.v0.response.GlobalResponse;
import com.roster.planner.v0.response.ShiftResponse;
import com.roster.planner.v0.service.impl.ShiftServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/shift")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftServiceImpl shiftService;

    @PostMapping("/lead/create/{teamUID}")
    public ResponseEntity<GlobalResponse<ShiftResponse>> createShift(
            @PathVariable String teamUID,
            @RequestBody ShiftRequest shiftRequest) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.<ShiftResponse>builder()
                        .status(HttpStatus.CREATED.value())
                        .data(shiftService.createShift(teamUID, shiftRequest))
                        .message("Shift created successfully!")
                        .build());
    }

    @GetMapping("/lead/read/all/{teamUID}")
    public ResponseEntity<GlobalResponse<List<ShiftResponse>>> readAllShiftOfTeam(@PathVariable String teamUID) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<List<ShiftResponse>>builder()
                        .status(HttpStatus.OK.value())
                        .data(shiftService.readAllShiftsOfTeamByUID(teamUID))
                        .message("Fetched all shifts of team with UID: " + teamUID)
                        .build());
    }

    @GetMapping("/lead/read/{shiftUID}")
    public ResponseEntity<GlobalResponse<ShiftResponse>> readShiftByUID(@PathVariable String shiftUID) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<ShiftResponse>builder()
                        .status(HttpStatus.OK.value())
                        .data(shiftService.readShiftByUID(shiftUID))
                        .message("Fetched shift with UID: " + shiftUID)
                        .build());
    }

    @PatchMapping("/lead/update/{shiftUID}")
    public ResponseEntity<GlobalResponse<ShiftResponse>> updateShiftByUID(
            @PathVariable String shiftUID,
            @RequestBody ShiftRequest shiftRequest) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<ShiftResponse>builder()
                        .status(HttpStatus.OK.value())
                        .data(shiftService.updateShift(shiftUID, shiftRequest))
                        .message("Shift updated successfully...")
                        .build());
    }

    @DeleteMapping("/lead/delete/{shiftUID}")
    public ResponseEntity<GlobalResponse<String>> deleteShiftByUID(@PathVariable String shiftUID) {
        shiftService.deleteShiftByUID(shiftUID);

        return ResponseEntity.status(HttpStatus.OK)
                .body(GlobalResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .data(null)
                        .message("Shift with UID: " + shiftUID + " deleted successfully...")
                        .build());
    }

}
