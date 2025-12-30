package com.quest.badminton.entity;

import com.quest.badminton.entity.enumaration.TourStatus;
import com.quest.badminton.entity.enumaration.TourType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_tours")
@Builder
public class Tour {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TourStatus status;

    @Column(name = "male_players")
    private Integer malePlayers;

    @Column(name = "female_players")
    private Integer femalePlayers;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TourType type;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "registration_end_date")
    private Instant registrationEndDate;

    @Column(name = "location")
    private String location;

    @Column(name = "location_url")
    private String locationUrl;

    @Column(name = "rule_url")
    private String ruleUrl;

    @OneToMany(mappedBy = "tour")
    private List<Team> teams;

    @OneToMany(mappedBy = "tour")
    private List<Player> players;
}
