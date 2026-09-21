package com.roster.planner.v0.repository;

import com.roster.planner.v0.entity.Leave;
import com.roster.planner.v0.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveRepository extends JpaRepository<Leave, Long> {
    Optional<Leave> findByLeaveUID(String leaveUID);

    List<Leave> findByRequestedBy_Email(String empUID);

    Optional<Leave> findByLeaveUIDAndRequestedByEmail(
            String leaveUID,
            String email
    );

    List<Leave> findByRequestedByAndStartDateGreaterThanEqualAndEndDateLessThanEqual(
            User requestedBy,
            LocalDate startDate,
            LocalDate endDate
    );
}
