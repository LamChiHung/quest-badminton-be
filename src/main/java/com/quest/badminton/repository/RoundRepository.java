package com.quest.badminton.repository;

import com.quest.badminton.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RoundRepository extends JpaRepository<Round, Long>, JpaSpecificationExecutor<Round> {
    boolean existsByTourIdAndName(Long tourId, String name);
}
