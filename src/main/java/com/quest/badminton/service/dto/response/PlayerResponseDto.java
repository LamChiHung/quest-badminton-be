package com.quest.badminton.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quest.badminton.entity.enumaration.Gender;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import com.quest.badminton.entity.enumaration.PlayerTier;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlayerResponseDto {
    private Long id;

    private Long userId;

    private String email;

    private String name;

    private Long tourId;

    private Long teamId;

    private PlayerTier tier;

    private PlayerStatus status;

    private Gender gender;

    private String note;
}
