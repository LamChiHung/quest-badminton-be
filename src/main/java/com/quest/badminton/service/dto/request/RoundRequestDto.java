package com.quest.badminton.service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quest.badminton.entity.enumaration.RoundType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoundRequestDto {
    @NotNull
    @Min(1)
    private Long tourId;

    @NotBlank
    private String name;

    @NotNull
    private RoundType type;
}
