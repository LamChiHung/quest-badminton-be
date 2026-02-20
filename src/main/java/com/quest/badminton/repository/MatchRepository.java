package com.quest.badminton.repository;

import com.quest.badminton.entity.GroupMatch;
import com.quest.badminton.entity.Match;
import com.quest.badminton.entity.Match_;
import com.quest.badminton.entity.enumaration.MatchStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long>, JpaSpecificationExecutor<Match> {
    boolean existsByPlayerPair1IdOrPlayerPair2Id(Long playerPair1Id, Long playerPair2Id);

    @EntityGraph(attributePaths = {"playerPair1", "playerPair2", "playerPair1.team", "playerPair2.team", "winner"})
    List<Match> findAllByTourIdAndStatus(Long tourId, MatchStatus status);
}
