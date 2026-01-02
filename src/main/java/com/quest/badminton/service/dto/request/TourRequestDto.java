package com.quest.badminton.service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quest.badminton.entity.enumaration.TourMatchType;
import com.quest.badminton.entity.enumaration.TourType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class TourRequestDto {

    @NotBlank
    private String name;

    @Min(1)
    private Integer malePlayers;

    @Min(1)
    private Integer femalePlayers;

    @NotNull
    private TourType type;

    @NotNull
    private TourMatchType matchType;

    @NotNull
    private Instant startDate;

    @NotNull
    private Instant registrationEndDate;

    @NotBlank
    private String location;

    @NotBlank
    private String locationUrl;

    @NotBlank
    private String ruleUrl;

    @NotNull
    @Builder.Default
    private Boolean isPrivate = false;
}
