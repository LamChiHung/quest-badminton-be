package com.quest.badminton.service;

import com.quest.badminton.service.criteria.TeamCriteria;
import com.quest.badminton.service.criteria.TourCriteria;
import com.quest.badminton.service.dto.response.TeamResponseDto;
import com.quest.badminton.service.dto.response.TourResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamQueryService {
    Page<TeamResponseDto> search(TeamCriteria criteria, Pageable pageable, boolean isForAdmin);

    TeamResponseDto getTeam(Long id, boolean isForAdmin);

}