package com.quest.badminton.repository;

import com.quest.badminton.entity.GroupMatch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupMatchRepository extends JpaRepository<GroupMatch, Long> {
}
