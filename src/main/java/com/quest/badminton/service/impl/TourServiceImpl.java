package com.quest.badminton.service.impl;

import com.quest.badminton.entity.Tour;
import com.quest.badminton.entity.enumaration.TourStatus;
import com.quest.badminton.repository.TourRepository;
import com.quest.badminton.service.TourService;
import com.quest.badminton.service.dto.request.TourRequestDto;
import com.quest.badminton.util.CodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;

    @Override
    @Transactional
    public void createTour(TourRequestDto request) {
        String code = null;

        do {
            code = CodeUtil.randomString(6);
        } while (tourRepository.existsByCode(code));
        tourRepository.save(Tour.builder()
                .name(request.getName())
                .code(code)
                .status(TourStatus.UPCOMING)
                .malePlayers(request.getMalePlayers())
                .femalePlayers(request.getFemalePlayers())
                .type(request.getType())
                .startDate(request.getStartDate())
                .registrationEndDate(request.getRegistrationEndDate())
                .location(request.getLocation())
                .locationUrl(request.getLocationUrl())
                .ruleUrl(request.getRuleUrl())
                .isPrivate(request.getIsPrivate())
                .build());
    }
}
