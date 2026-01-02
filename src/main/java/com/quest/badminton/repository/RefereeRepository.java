package com.quest.badminton.repository;

import com.quest.badminton.entity.Referee;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RefereeRepository extends JpaRepository<Referee, Long> {

    Integer countAllByTourId(Long tourId);

    List<Referee> findAllByTourIdAndUserId(Long tourId, Long userId);

    boolean existsByTourIdAndUserIdAndStatusIn(Long tourId, Long userId, List<PlayerStatus> status);
}
