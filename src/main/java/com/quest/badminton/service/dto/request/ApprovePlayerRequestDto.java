package com.quest.badminton.service.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Min;
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
public class ApprovePlayerRequestDto {
    @NotNull
    @Min(1)
    private Long tourId;

    @NotNull
    @Min(1)
    private Long playerId;

    private boolean isApprove;

    private String note;
}
