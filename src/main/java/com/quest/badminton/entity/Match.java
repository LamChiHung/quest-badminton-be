package com.quest.badminton.entity;

import com.quest.badminton.entity.enumaration.MatchStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_matches")
@Builder
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "fk_player_pair_id_1", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PlayerPair playerPair1;

    @JoinColumn(name = "fk_player_pair_id_2", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PlayerPair playerPair2;

    @JoinColumn(name = "fk_group_match_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private GroupMatch groupMatch;

    @JoinColumn(name = "fk_round_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Round round;

    @JoinColumn(name = "fk_tour_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Tour tour;

    @Column(name = "score_1_set_1")
    @Builder.Default
    private Integer score1Set1 = 0;

    @Column(name = "score_2_set_1")
    @Builder.Default
    private Integer score2Set1 = 0;

    @Column(name = "score_1_set_2")
    @Builder.Default
    private Integer score1Set2 = 0;

    @Column(name = "score_2_set_2")
    @Builder.Default
    private Integer score2Set2 = 0;

    @Column(name = "score_1_set_3")
    @Builder.Default
    private Integer score1Set3 = 0;

    @Column(name = "score_2_set_3")
    @Builder.Default
    private Integer score2Set3 = 0;

    @Column(name = "set")
    @Builder.Default
    private Integer set = 1;

    @Column(name = "total_set")
    @Builder.Default
    private Integer totalSet = 3;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MatchStatus status = MatchStatus.UPCOMING;

    @JoinColumn(name = "fk_serve_player_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Player servePlayer;

    @JoinColumn(name = "fk_receive_player_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Player receivePlayer;

    @JoinColumn(name = "fk_winner_player_pair_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PlayerPair winner;

    @JoinColumn(name = "fk_referee_id", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Referee referee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_parent_match_id_1", referencedColumnName = "id")
    private Match parentMatch1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_parent_match_id_2", referencedColumnName = "id")
    private Match parentMatch2;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;
}
