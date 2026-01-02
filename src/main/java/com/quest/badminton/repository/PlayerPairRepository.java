package com.quest.badminton.repository;

import com.quest.badminton.entity.PlayerPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerPairRepository extends JpaRepository<PlayerPair, Long> {
    boolean existsByTourIdAndPlayer1IdAndPlayer2Id(Long tourId, Long player1Id, Long player2Id);

}
