package com.quest.badminton.repository;

import com.quest.badminton.entity.GroupMatchRound;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMatchRoundRepository extends JpaRepository<GroupMatchRound, Long> {
}
