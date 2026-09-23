package com.roster.planner.v0.repository;

import com.roster.planner.v0.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Long> {

    Optional<Shift> findByShiftUID(String shiftUID);

    List<Shift> findByTeam_TeamUID(String teamUID);

    void deleteByShiftUID(String shiftUID);
}
