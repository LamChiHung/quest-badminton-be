package com.quest.badminton.repository;

import com.quest.badminton.entity.MatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchHistoryRepository extends JpaRepository<MatchHistory, Long> {
    Optional<MatchHistory> findFirstByMatchIdOrderByIdDesc(Long id);
}
