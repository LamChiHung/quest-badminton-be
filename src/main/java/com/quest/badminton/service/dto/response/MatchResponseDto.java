package com.quest.badminton.service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.quest.badminton.entity.*;
import com.quest.badminton.entity.enumaration.MatchStatus;
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
public class MatchResponseDto {
    private Long id;

    private PlayerPairResponseDto playerPair1;

    private PlayerPairResponseDto playerPair2;

    private Long groupMatchId;

    private Long roundId;

    private Long tourId;

    private Integer score1 = 0;

    private Integer score2 = 0;

    private MatchStatus status;

    private Long servePlayerId;

    private Long receivePlayerId;

    private Long winnerId;

    private RefereeResponseDto referee;

    private Long parentMatch1Id;

    private Long parentMatch2Id;
}
