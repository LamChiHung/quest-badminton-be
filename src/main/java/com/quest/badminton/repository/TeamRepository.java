package com.quest.badminton.repository;

import com.quest.badminton.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    boolean existsByTourIdAndName(Long tourId, String name);
    boolean existsByTourIdAndNameAndIdNot(Long tourId, String name, Long id);
    Integer countAllByTourId(Long tourId);

    boolean existsByTourIdAndCaptainId(Long tourId, Long playerId);

    Optional<Team> findByTourIdAndCaptainId(Long tourId, Long captainId);
}
