package com.quest.badminton.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quest.badminton.entity.Tour;
import com.quest.badminton.entity.User;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RefereeResponseDto {
    private Long id;

    private String name;

    private String email;

    private PlayerStatus status;

    private Long tourId;

    private String note;

    private Long approvedBy;

    private Long rejectedBy;
}
