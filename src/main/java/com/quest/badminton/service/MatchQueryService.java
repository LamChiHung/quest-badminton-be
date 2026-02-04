package com.quest.badminton.service;

import com.quest.badminton.service.criteria.GroupMatchCriteria;
import com.quest.badminton.service.criteria.MatchCriteria;
import com.quest.badminton.service.dto.response.GroupMatchResponseDto;
import com.quest.badminton.service.dto.response.MatchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MatchQueryService {
    Page<MatchResponseDto> search(MatchCriteria criteria, Pageable pageable, boolean isForAdmin);

    MatchResponseDto getMatch(Long id, boolean isForAdmin);
}
