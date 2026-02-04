package com.quest.badminton.entity;

import com.quest.badminton.entity.enumaration.RoundType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_rounds")
@Builder
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_tour_id", referencedColumnName = "id")
    private Tour tour;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private RoundType type;
}
