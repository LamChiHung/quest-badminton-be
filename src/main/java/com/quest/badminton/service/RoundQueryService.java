package com.quest.badminton.service;

import com.quest.badminton.service.criteria.RoundCriteria;
import com.quest.badminton.service.dto.response.RoundResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoundQueryService {
    Page<RoundResponseDto> search(RoundCriteria criteria, Pageable pageable);
    RoundResponseDto getRound(Long id);
}
