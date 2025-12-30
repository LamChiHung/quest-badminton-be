package com.quest.badminton.entity;


import com.quest.badminton.entity.enumaration.PlayerStatus;
import com.quest.badminton.entity.enumaration.PlayerTier;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_players")
@Builder
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne()
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "fk_tour_id", referencedColumnName = "id")
    private Tour tour;

    @ManyToOne()
    @JoinColumn(name = "fk_team_id", referencedColumnName = "id")
    private Team team;

    @Column(name = "tier")
    @Enumerated(EnumType.STRING)
    private PlayerTier tier;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PlayerStatus status;

    @Column(name = "note")
    private String note;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "rejected_by")
    private Long rejectedBy;
}
