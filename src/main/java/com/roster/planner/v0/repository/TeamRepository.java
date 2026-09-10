package com.roster.planner.v0.repository;

import com.roster.planner.v0.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamUID(String teamUID);
    void deleteTeamByTeamUID(String teamUID);
}
