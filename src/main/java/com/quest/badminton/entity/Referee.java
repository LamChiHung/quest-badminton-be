package com.quest.badminton.entity;

import com.quest.badminton.entity.enumaration.PlayerStatus;
import jakarta.persistence.*;
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
@Table(name = "t_referees")
@Builder
public class Referee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PlayerStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_tour_id", referencedColumnName = "id")
    private Tour tour;

    @Column(name = "note")
    private String note;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "rejected_by")
    private Long rejectedBy;
}
