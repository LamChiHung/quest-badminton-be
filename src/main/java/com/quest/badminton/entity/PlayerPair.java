package com.quest.badminton.entity;

import com.quest.badminton.entity.enumaration.PlayerPairType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_player_pairs")
@Builder
public class PlayerPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "fk_player_id_1", referencedColumnName = "id")
    @ManyToOne
    private Player player1;

    @JoinColumn(name = "fk_player_id_2", referencedColumnName = "id")
    @ManyToOne
    private Player player2;

    @JoinColumn(name = "fk_tour_id", referencedColumnName = "id")
    @ManyToOne
    private Tour tour;

    @JoinColumn(name = "fk_team_id", referencedColumnName = "id")
    @ManyToOne
    private Team team;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private PlayerPairType type;
}
