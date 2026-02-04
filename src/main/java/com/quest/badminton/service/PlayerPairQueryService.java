package com.quest.badminton.service;

import com.quest.badminton.service.criteria.PlayerPairCriteria;
import com.quest.badminton.service.dto.response.PlayerPairResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

public interface PlayerPairQueryService {
    @Transactional(readOnly = true)
    Page<PlayerPairResponseDto> search(PlayerPairCriteria criteria, Pageable pageable, boolean isForAdmin);
}
