package com.quest.badminton.service;

import com.quest.badminton.service.dto.criteria.TourCriteria;
import com.quest.badminton.service.dto.response.TourResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TourQueryService {
    Page <TourResponseDto> search(TourCriteria criteria, Pageable pageable, boolean isForAdmin);

    TourResponseDto getTour(String code, boolean isForAdmin);

    TourResponseDto getTour(Long id, boolean isForAdmin);
}
