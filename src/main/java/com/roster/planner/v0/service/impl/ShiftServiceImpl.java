package com.roster.planner.v0.service.impl;

import com.roster.planner.v0.entity.Shift;
import com.roster.planner.v0.entity.Team;
import com.roster.planner.v0.repository.ShiftRepository;
import com.roster.planner.v0.request.ShiftRequest;
import com.roster.planner.v0.response.ShiftResponse;
import com.roster.planner.v0.service.interfaces.ShiftService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftServiceImpl implements ShiftService {

    private final ShiftRepository shiftRepo;
    private final TeamServiceImpl teamService;

    private static ShiftResponse convertor(Shift shiftObj) {
        return ShiftResponse.builder()
                .shiftUID(shiftObj.getShiftUID())
                .name(shiftObj.getName())
                .shortName(shiftObj.getShortName())
                .startTime(shiftObj.getStartTime())
                .endTime(shiftObj.getEndTime())
                .createdAt(shiftObj.getCreatedAt())
                .updatedAt(shiftObj.getUpdatedAt())
                .teamUID(shiftObj.getTeam().getTeamUID())
                .build();
    }

    @Override
    public ShiftResponse createShift(String teamUID, ShiftRequest shiftRequestData) {
        Team teamObj = teamService.getOriginalTeamDetails(teamUID);

        Shift shiftObj = new Shift();

        shiftObj.setName(shiftRequestData.getName());
        shiftObj.setShortName(shiftRequestData.getShortName());
        shiftObj.setStartTime(shiftRequestData.getStartTime());
        shiftObj.setEndTime(shiftRequestData.getEndTime());
        shiftObj.setTeam(teamObj);

        Shift savedShiftObj = shiftRepo.save(shiftObj);

        return convertor(savedShiftObj);
    }

    @Override
    public ShiftResponse readShiftByUID(String shiftUID) {
        Shift shiftOBj = shiftRepo.findByShiftUID(shiftUID).orElseThrow(() -> new RuntimeException("Shift with UID: " + shiftUID + " not found!"));

        return convertor(shiftOBj);
    }

    @Override
    public List<ShiftResponse> readAllShiftsOfTeamByUID(String teamUID) {
        return shiftRepo.findByTeam_TeamUID(teamUID).stream().map(ShiftServiceImpl::convertor).toList();
    }

    @Override
    public ShiftResponse updateShift(String shiftUID, ShiftRequest shiftRequestData) {
        Shift shiftObj = shiftRepo.findByShiftUID(shiftUID).orElseThrow(() -> new RuntimeException("Shift with UID: " + shiftUID + " not found!"));

        shiftObj.setName(shiftRequestData.getName());
        shiftObj.setShortName(shiftRequestData.getShortName());

        shiftObj.setStartTime(shiftRequestData.getStartTime());
        shiftObj.setEndTime(shiftRequestData.getEndTime());

        shiftObj.setUpdatedAt(LocalDateTime.now());

        return convertor(shiftRepo.save(shiftObj));
    }

    @Override
    @Transactional
    public void deleteShiftByUID(String shiftUID) {
        Shift shiftObj = shiftRepo
                .findByShiftUID(shiftUID)
                .orElseThrow(() -> new RuntimeException(
                        "Shift with UID: " + shiftUID + " not found!"
                ));

        shiftRepo.delete(shiftObj);
    }
}
