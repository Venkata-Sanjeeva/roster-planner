package com.roster.planner.v0.service.interfaces;

import com.roster.planner.v0.request.ShiftRequest;
import com.roster.planner.v0.response.ShiftResponse;

import java.util.List;

public interface ShiftService {

    ShiftResponse createShift(String teamUID, ShiftRequest shiftRequestData);
    ShiftResponse readShiftByUID(String shiftUID);
    List<ShiftResponse> readAllShiftsOfTeamByUID(String teamUID);
    ShiftResponse updateShift(String shiftUID, ShiftRequest shiftRequestData);
    void deleteShiftByUID(String shiftUID);

}
