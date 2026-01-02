package com.quest.badminton.service;

import com.quest.badminton.service.dto.criteria.PlayerCriteria;
import com.quest.badminton.service.dto.response.PlayerResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerQueryService {
    Page <PlayerResponseDto> search(PlayerCriteria criteria, Pageable pageable, boolean isForAdmin);

    PlayerResponseDto getPlayer(Long id, boolean isForAdmin);
}
