package com.quest.badminton.service.dto.response;

import com.quest.badminton.entity.enumaration.TourStatus;
import com.quest.badminton.entity.enumaration.TourType;

import java.time.Instant;

public class TourResponseDto {
    private Long id;

    private String name;

    private String code;

    private TourStatus status;

    private Integer malePlayers;

    private Integer malePlayerRegisted;

    private Integer femalePlayers;

    private Integer femalePlayerRegisted;

    private TourType type;

    private Instant startDate;

    private Instant registrationEndDate;

    private String location;

    private String locationUrl;

    private String ruleUrl;

}
