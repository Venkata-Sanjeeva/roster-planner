package com.roster.planner.v0.response;

import com.roster.planner.v0.enums.RequestStatus;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class TeamJoinReqResDTO {
    String joinReqUID;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class ByObj {
        String empUID;
        String empName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class RequestedTeam {
        String teamUID;
        String teamName;
    }

    ByObj reqByEmp;
    ByObj approvedBy;
    RequestedTeam reqTeam;
    RequestStatus reqStatus;
    LocalDateTime createdAt;
    LocalDateTime approvedAt;
}
