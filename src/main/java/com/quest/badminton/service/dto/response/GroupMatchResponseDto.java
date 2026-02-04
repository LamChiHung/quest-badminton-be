package com.quest.badminton.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quest.badminton.entity.Tour;
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
public class GroupMatchResponseDto {
    private Long id;
    private String name;
    private Long tourId;
}
