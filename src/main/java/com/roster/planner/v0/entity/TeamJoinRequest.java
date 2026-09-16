package com.roster.planner.v0.entity;

import com.roster.planner.v0.enums.RequestStatus;
import com.roster.planner.v0.util.IdentifierGenerator;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Table(name = "teamJoinRequest")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class TeamJoinRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Join request UID is required")
    @Column(unique = true, nullable = false)
    private String joinReqUID = IdentifierGenerator.generate("JREQ");

    @OneToOne
    private User requestedBy;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    // Add Approved At
    private LocalDateTime approvedAt;

    // Add Approved By
    @ManyToOne
    private User approvedBy;
}

/*
Join_Req must be created by EMP,
Join_Req must be approved by TL,

So, POST/CREATE of Join_Req should be connected with EMP endpoint
Then, GET/READ, UPDATE/APPROVE should be connected with TL endpoint
 */