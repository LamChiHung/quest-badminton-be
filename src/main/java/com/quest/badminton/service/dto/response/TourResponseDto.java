package com.quest.badminton.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quest.badminton.entity.enumaration.TourMatchType;
import com.quest.badminton.entity.enumaration.TourStatus;
import com.quest.badminton.entity.enumaration.TourType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourResponseDto {
    private Long id;

    private String name;

    private String code;

    private TourStatus status;

    private Integer malePlayers;

    private Integer malePlayerRegistered;

    private Integer femalePlayers;

    private Integer femalePlayerRegistered;

    private TourType type;

    private TourMatchType matchType;

    private Instant startDate;

    private Instant registrationEndDate;

    private String location;

    private String locationUrl;

    private String ruleUrl;

    private Integer pendingApprovePlayers;

    private String backgroundUrl;

    private String avatarUrl;

    private Boolean isAvailableToRegister;

}
