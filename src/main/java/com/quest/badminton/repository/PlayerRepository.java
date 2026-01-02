package com.quest.badminton.repository;

import com.quest.badminton.entity.Player;
import com.quest.badminton.entity.enumaration.Gender;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long>, JpaSpecificationExecutor<Player> {

    Integer countAllByTourId(Long tourId);
    Integer countAllByTourIdAndGenderAndStatusIn(Long tourId, Gender gender, List<PlayerStatus> status);
    Integer countAllByTourIdAndStatusIn(Long tourId, List<PlayerStatus> status);
    List<Player> findAllByTourIdAndUserId(Long tourId, Long userId);

    boolean existsByTourIdAndUserIdAndStatusIn(Long tourId, Long userId, List<PlayerStatus> status);
}
