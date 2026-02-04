package com.quest.badminton.entity;

import com.quest.badminton.entity.enumaration.MatchStatus;
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
@Table(name = "t_matches_history")
@Builder
public class MatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "fk_player_pair_id_1", referencedColumnName = "id")
    @ManyToOne
    private PlayerPair playerPair1;

    @JoinColumn(name = "fk_player_pair_id_2", referencedColumnName = "id")
    @ManyToOne
    private PlayerPair playerPair2;

    @Column(name = "score_1")
    private Integer score1;

    @Column(name = "score_2")
    private Integer score2;

    @JoinColumn(name = "fk_serve_player_id", referencedColumnName = "id")
    @ManyToOne
    private Player servePlayer;

    @JoinColumn(name = "fk_receive_player_id", referencedColumnName = "id")
    @ManyToOne
    private Player receivePlayer;

    @ManyToOne
    @JoinColumn(name = "fk_match_id", referencedColumnName = "id")
    private Match match;
}
