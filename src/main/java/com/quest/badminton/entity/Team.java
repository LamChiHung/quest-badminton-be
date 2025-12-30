package com.quest.badminton.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_teams")
@Builder
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number")
    private Integer number;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_tour_id", referencedColumnName = "id")
    private Tour tour;

    @OneToMany(mappedBy = "team")
    private List<Player> players;
}
