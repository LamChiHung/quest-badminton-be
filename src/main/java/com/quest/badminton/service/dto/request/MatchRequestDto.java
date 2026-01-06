package com.quest.badminton.service.dto.request;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class MatchRequestDto {
    private Long playerPair1Id;
    private Long playerPair2Id;

    @NotNull
    private Long groupMatchId;

    @NotNull
    private Long tourId;
}
