package com.roster.planner.v0.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.roster.planner.v0.util.IdentifierGenerator;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "shifts")
@Getter
@Setter
@NoArgsConstructor
public class Shift {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String shiftUID = IdentifierGenerator.generate("shi");

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String shortName;

    /*
        * Removed nullable = true, because for WEEK_OFF's / COMPANY_HOLIDAY's we can be able to use this shift alone.
        * Like, if we see startTime & endTime as NULL values we can get to know that it's either a HOLIDAY / WEEK_OFF.
    */
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

}
