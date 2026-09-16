package com.roster.planner.v0.repository;

import com.roster.planner.v0.entity.TeamJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamJoinReqRepository extends JpaRepository<TeamJoinRequest, Long> {
    Optional<TeamJoinRequest> findByJoinReqUID(String joinReqUID);
    List<TeamJoinRequest> findByTeam_TeamUID(String teamUID);
    void deleteByJoinReqUID(String reqUID);
}
