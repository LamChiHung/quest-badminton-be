package com.quest.badminton.repository;

import com.quest.badminton.entity.GroupMatch;
import com.quest.badminton.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
}
