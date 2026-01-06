package com.quest.badminton.service;

import com.quest.badminton.service.dto.request.*;
import com.quest.badminton.service.dto.response.CheckTourRoleResponseDto;

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
}
