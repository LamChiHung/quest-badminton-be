package com.quest.badminton.service;

import com.quest.badminton.service.dto.request.TourRequestDto;

public interface TourService {
    void createTour(TourRequestDto request);
}
