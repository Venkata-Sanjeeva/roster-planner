package com.roster.planner.v0.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Team UID is required!")
    @Column(unique = true, nullable = false)
    private String teamUID;

    @NotBlank(message = "Name is required")
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_manager_id")
    private User projectManager;

    @OneToOne
    @JoinColumn(name = "team_lead_id")
    private User teamLead;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "team")
    private Set<User> teamMembers = new HashSet<>();

    @OneToMany(
            mappedBy = "team",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TeamJoinRequest> teamJoinRequestList = new ArrayList<>();
}

// PROBLEM
// I'm seeing an issue to add a TEAM_LEAD under Team where other
// employees needs to join and due to this I think it throws an error, like duplicates found
// TEAM_LEAD is also an USER and there are multiple USERS must undergo this Team as EMPLOYEES.


// SOLUTION
// You shouldn't create a separate TeamLead entity if a Team Lead is simply a User with a particular role.
//
//For example:
//
//User
// ├── TEAM_LEAD
// ├── EMPLOYEE
// └── ADMIN
//
//Then:
//
//Team
// ├── teamLead → User
// └── teamMembers → Set<User>