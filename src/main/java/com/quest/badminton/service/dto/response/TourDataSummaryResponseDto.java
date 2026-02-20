package com.quest.badminton.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class TourDataSummaryResponseDto {
    Map<Long, Integer> teamTotalPoints;
    Map<Long, Integer> teamTotalWins;

    Map<Long, Integer> singMalePoints;
    Map<Long, Integer> singleMaleWins;

    Map<Long, Integer> singleFemalePoints;
    Map<Long, Integer> singleFemaleWins;

    Map<Long, Integer> doubleMalePoints;
    Map<Long, Integer> doubleMaleWins;

    Map<Long, Integer> doubleFemalePoints;
    Map<Long, Integer> doubleFemaleWins;

    Map<Long, Integer> mixPoints;
    Map<Long, Integer> mixWins;
}
