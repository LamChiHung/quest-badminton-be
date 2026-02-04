package com.quest.badminton.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_group_matches_rounds")
@Builder
public class GroupMatchRound {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fk_group_match_id")
    private GroupMatch groupMatch;

    @ManyToOne
    @JoinColumn(name = "fk_round_id")
    private Round round;
}
