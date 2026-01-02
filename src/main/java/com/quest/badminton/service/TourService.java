package com.quest.badminton.service;

import com.quest.badminton.service.dto.request.AddPlayerToTeamRequestDto;
import com.quest.badminton.service.dto.request.ApprovePlayerRequestDto;
import com.quest.badminton.service.dto.request.GroupMatchRequestDto;
import com.quest.badminton.service.dto.request.RegisterPlayerPairRequestDto;
import com.quest.badminton.service.dto.request.RegisterTourPlayerRequestDto;
import com.quest.badminton.service.dto.request.TeamRequestDto;
import com.quest.badminton.service.dto.request.TourRequestDto;
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
}
