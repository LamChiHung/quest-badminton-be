package com.quest.badminton.service;

import com.quest.badminton.service.dto.request.*;
import com.quest.badminton.service.dto.response.CheckTourRoleResponseDto;
import com.quest.badminton.service.dto.response.TourDataSummaryResponseDto;

public interface TourService {
    void createTour(TourRequestDto request);

    void createTeam(TeamRequestDto request);

    void addPlayerToTeam(AddPlayerToTeamRequestDto request);

    CheckTourRoleResponseDto checkTourRole(Long tourId, Long userId);

    void registerPlayer(Long userId, RegisterTourPlayerRequestDto request);

    void registerPlayerPair(Long userId, RegisterPlayerPairRequestDto request);

    void approvePlayer(ApprovePlayerRequestDto request, Long hostId);

    void createGroupMatch(GroupMatchRequestDto request);

    void createMatch(MatchRequestDto request);

    void deletePlayerPair(Long id);

    void createRound(RoundRequestDto request);

    void startMatch(StartMatchRequestDto request);

    void addPoint(AddPointRequestDto request);

    void endSet(Long id);

    void undoMatch(Long id);

    void endMatch(EndMatchRequestDto request);

    TourDataSummaryResponseDto summaryData(Long id);
}
