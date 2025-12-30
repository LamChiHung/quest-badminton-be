package com.quest.badminton.entity;

import com.quest.badminton.entity.enumaration.Club;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "t_users")
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    private String roles;

    @Column(name = "club")
    @Enumerated(EnumType.STRING)
    private Club club;

    @Column(name = "is_enabled")
    @Builder.Default
    private boolean enabled = false;

    @Column(name = "register_token")
    private String registerToken;

    @OneToMany(mappedBy = "user")
    private List<Player> players;
}
