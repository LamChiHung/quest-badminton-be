package com.quest.badminton.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quest.badminton.entity.enumaration.PlayerPairType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerPairResponseDto {
    private Long id;

    private PlayerResponseDto player1;

    private PlayerResponseDto player2;

    private Long tourId;

    private Long teamId;

    private PlayerPairType type;
}
