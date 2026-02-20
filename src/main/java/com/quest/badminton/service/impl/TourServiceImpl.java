package com.quest.badminton.service.impl;

import com.quest.badminton.constant.ErrorConstants;
import com.quest.badminton.entity.*;
import com.quest.badminton.entity.enumaration.*;
import com.quest.badminton.exception.BadRequestException;
import com.quest.badminton.repository.*;
import com.quest.badminton.service.TourService;
import com.quest.badminton.service.dto.request.*;
import com.quest.badminton.service.dto.response.CheckTourRoleResponseDto;
import com.quest.badminton.service.dto.response.TourDataSummaryResponseDto;
import com.quest.badminton.util.CodeUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourServiceImpl implements TourService {
    private final TourRepository tourRepository;
    private final PlayerRepository playerRepository;
    private final RefereeRepository refereeRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final GroupMatchRepository groupMatchRepository;
    private final PlayerPairRepository playerPairRepository;
    private final MatchRepository matchRepository;
    private final RoundRepository roundRepository;
    private final MatchHistoryRepository matchHistoryRepository;

    @Override
    @Transactional
    public void createTour(TourRequestDto request) {
        String code = null;

        do {
            code = CodeUtil.randomString(6);
        } while (tourRepository.existsByCode(code));
        tourRepository.save(Tour.builder()
                .name(request.getName())
                .code(code)
                .status(TourStatus.UPCOMING)
                .malePlayers(request.getMalePlayers())
                .femalePlayers(request.getFemalePlayers())
                .type(request.getType())
                .matchType(request.getMatchType())
                .startDate(request.getStartDate())
                .registrationEndDate(request.getRegistrationEndDate())
                .location(request.getLocation())
                .locationUrl(request.getLocationUrl())
                .ruleUrl(request.getRuleUrl())
                .isPrivate(request.getIsPrivate())
                .backgroundUrl(request.getBackgroundUrl())
                .avatarUrl(request.getAvatarUrl())
                .build());
    }

    @Override
    @Transactional
    public void createTeam(TeamRequestDto request) {
        String name = request.getName();
        Long tourId = request.getTourId();

        if (teamRepository.existsByTourIdAndName(tourId, name)) {
            throw new BadRequestException(ErrorConstants.ERR_TEAM_NAME_EXISTS);
        }

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));

        Integer number = teamRepository.countAllByTourId(tourId) + 1;
        teamRepository.save(Team.builder()
                .name(name)
                .number(number)
                .tour(tour)
                .build());
    }

    @Override
    @Transactional
    public void addPlayerToTeam(AddPlayerToTeamRequestDto request) {
        Long teamId = request.getTeamId();
        Long captainId = request.getCaptainId();
        List<Long> playerIds = request.getPlayerIds();

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TEAM_NOT_FOUND));
        Player captain = playerRepository.findById(captainId).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));
        captain.setTeam(team);
        team.setCaptain(captain);

        List<Player> oldPlayers = playerRepository.findAllByTeamId(teamId);
        List<Player> players = playerRepository.findAllById(playerIds);

        List<Player> removePlayers = oldPlayers.stream()
                .filter(player -> !playerIds.contains(player.getId()))
                .toList();

        for (Player player : players) {
            if (!Objects.equals(player.getTour().getId(), team.getTour().getId())) {
                throw new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_IN_TOUR);
            }
            player.setTeam(team);
        }
        for (Player removePlayer : removePlayers) {
            if (!Objects.equals(removePlayer.getTour().getId(), team.getTour().getId())) {
                throw new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_IN_TOUR);
            }
            removePlayer.setTeam(null);
        }

        playerRepository.saveAll(players);
    }

    @Override
    @Transactional(readOnly = true)
    public CheckTourRoleResponseDto checkTourRole(Long tourId, Long userId) {
        List<Player> players = playerRepository.findAllByTourIdAndUserId(tourId, userId)
                .stream()
                .filter(p -> p.getStatus().equals(PlayerStatus.APPROVED))
                .collect(Collectors.toList());
        List<Referee> referees = refereeRepository.findAllByTourIdAndUserId(tourId, userId)
                .stream()
                .filter(p -> p.getStatus().equals(PlayerStatus.APPROVED))
                .collect(Collectors.toList());

        if (players.isEmpty() && referees.isEmpty()) {
            return CheckTourRoleResponseDto.builder()
                    .role(TourRole.GUEST)
                    .build();
        }

        if (!referees.isEmpty()) {
            return CheckTourRoleResponseDto.builder()
                    .role(TourRole.REFEREE)
                    .build();
        }

        if (!players.isEmpty()) {
            Player player = players.get(0);
            if (teamRepository.existsByTourIdAndCaptainId(tourId, player.getId())) {
                return CheckTourRoleResponseDto.builder()
                        .role(TourRole.CAPTAIN)
                        .build();
            }
            return CheckTourRoleResponseDto.builder()
                    .role(TourRole.PLAYER)
                    .build();
        }

        return CheckTourRoleResponseDto.builder()
                .role(TourRole.GUEST)
                .build();
    }

    @Override
    @Transactional
    public void registerPlayer(Long userId, RegisterTourPlayerRequestDto request) {
        Long tourId = request.getTourId();
        PlayerTier tier = request.getTier();
        Gender gender = request.getGender();

        if (refereeRepository.existsByTourIdAndUserIdAndStatusIn(
                tourId,
                userId,
                List.of(PlayerStatus.APPROVED, PlayerStatus.PENDING_APPROVE)))
        {
            throw new BadRequestException(ErrorConstants.ERR_USER_REGISTERED_REFEREE);
        }

        if (playerRepository.existsByTourIdAndUserIdAndStatusIn(
                tourId,
                userId,
                List.of(PlayerStatus.PENDING_APPROVE)))
        {
            throw new BadRequestException(ErrorConstants.ERR_PLAYER_REGISTRATION_PENDING_APPROVE);
        }

        if (playerRepository.existsByTourIdAndUserIdAndStatusIn(
                tourId,
                userId,
                List.of(PlayerStatus.APPROVED)))
        {
            throw new BadRequestException(ErrorConstants.ERR_PLAYER_REGISTRATION_APPROVED);
        }

        if (playerRepository.existsByTourIdAndUserIdAndStatusIn(
                tourId,
                userId,
                List.of(PlayerStatus.BANNED)))
        {
            throw new BadRequestException(ErrorConstants.ERR_PLAYER_BANNED);
        }


        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_USER_NOT_FOUND));
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));

        Integer registeredPlayers = playerRepository.countAllByTourIdAndGenderAndStatusIn(tourId, gender, List.of(PlayerStatus.APPROVED));
        if (gender.equals(Gender.MALE) && registeredPlayers >= tour.getMalePlayers()) {
            throw new BadRequestException(ErrorConstants.ERR_MALE_PLAYER_FULL);
        }
        if (gender.equals(Gender.FEMALE) && registeredPlayers >= tour.getFemalePlayers()) {
            throw new BadRequestException(ErrorConstants.ERR_FEMALE_PLAYER_FULL);
        }

        Player player = playerRepository.save(Player.builder()
                .user(user)
                .tour(tour)
                .tier(tier)
                .status(PlayerStatus.PENDING_APPROVE)
                .gender(gender)
                .build());
    }

    @Override
    @Transactional
    public void registerPlayerPair(Long userId, RegisterPlayerPairRequestDto request) {
        Long tourId = request.getTourId();
        Long player1Id = request.getPlayer1Id();
        Long player2Id = request.getPlayer2Id();
        PlayerPairType type = request.getType();

        Set<PlayerPairType> singleTypes = Set.of(PlayerPairType.SINGLE_MALE, PlayerPairType.SINGLE_FEMALE);
        if (singleTypes.contains(type)) {
            if (player2Id != null) {
                throw new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_INVALID);
            }
        } else {
            if (player2Id == null) {
                throw new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_INVALID);
            }
        }

        if (player2Id == null) {
            if (playerPairRepository.existsByTourIdAndPlayer1IdAndPlayer2IdIsNull(tourId, player1Id)) {
                throw new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_EXISTS);
            }
        } else {
            if (playerPairRepository.existsByTourIdAndPlayer1IdAndPlayer2Id(tourId, player1Id, player2Id) ||
                    playerPairRepository.existsByTourIdAndPlayer1IdAndPlayer2Id(tourId, player2Id, player1Id))
            {
                throw new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_EXISTS);
            }
        }


        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));
        Player player1 = playerRepository.findById(player1Id).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));
        Player player2 = null;
        if (player2Id != null) {
            player2 = playerRepository.findById(player2Id).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));
        }
        Player captain = playerRepository.findAllByTourIdAndUserId(tourId, userId)
                .stream()
                .filter(p -> p.getStatus().equals(PlayerStatus.APPROVED))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));

        Team team = teamRepository.findByTourIdAndCaptainId(tourId, captain.getId())
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TEAM_NOT_FOUND));

        if (!Objects.equals(player1.getTeam().getId(), team.getId())) {
            throw new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_IN_TEAM);
        }
        if (player2 != null && !Objects.equals(player2.getTeam().getId(), team.getId())) {
            throw new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_IN_TEAM);
        }

        playerPairRepository.save(PlayerPair.builder()
                .tour(tour)
                .player1(player1)
                .player2(player2)
                .type(type)
                .team(team)
                .build());
    }

    @Override
    @Transactional
    public void approvePlayer(ApprovePlayerRequestDto request, Long hostId) {
        Long tourId = request.getTourId();
        Long playerId = request.getPlayerId();
        String note = request.getNote();
        boolean isApprove = request.isApprove();

        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));
        Player player = playerRepository.findById(playerId).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));

        if (!isApprove) {
            player.setStatus(PlayerStatus.REJECTED);
            player.setNote(note);
            player.setRejectedBy(hostId);
            playerRepository.save(player);
            return;
        }

        Integer approvedPlayer = playerRepository.countAllByTourIdAndGenderAndStatusIn(tourId, player.getGender(), List.of(PlayerStatus.APPROVED));
        if (player.getGender().equals(Gender.MALE) && approvedPlayer >= tour.getMalePlayers()) {
            throw new BadRequestException(ErrorConstants.ERR_MALE_PLAYER_FULL);
        }
        if (player.getGender().equals(Gender.FEMALE) && approvedPlayer >= tour.getFemalePlayers()) {
            throw new BadRequestException(ErrorConstants.ERR_FEMALE_PLAYER_FULL);
        }

        player.setStatus(PlayerStatus.APPROVED);
        player.setNote(note);
        player.setApprovedBy(hostId);
        playerRepository.save(player);
    }

    @Override
    @Transactional
    public void createMatch(MatchRequestDto request) {
        Long tourId = request.getTourId();
        Long groupMatchId = request.getGroupMatchId();
        Long roundId = request.getRoundId();
        Long playerPair1Id = request.getPlayerPair1Id();
        Long playerPair2Id = request.getPlayerPair2Id();

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));

        Round round = roundRepository.findById(roundId)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_ROUND_NOT_FOUND));

        GroupMatch groupMatch = groupMatchRepository.findById(groupMatchId)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_GROUP_MATCH_NOT_FOUND));

        if (!Objects.equals(groupMatch.getTour().getId(), tour.getId())) {
            throw new BadRequestException(ErrorConstants.ERR_GROUP_MATCH_NOT_IN_TOUR);
        }

        PlayerPair playerPair1 = null;
        if (playerPair1Id != null) {
            playerPair1 = playerPairRepository.findById(playerPair1Id)
                    .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_NOT_FOUND));
            if (!Objects.equals(playerPair1.getTour().getId(), tour.getId())) {
                throw new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_NOT_IN_TOUR);
            }
        }

        PlayerPair playerPair2 = null;
        if (playerPair2Id != null) {
            playerPair2 = playerPairRepository.findById(playerPair2Id)
                    .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_NOT_FOUND));
            if (!Objects.equals(playerPair2.getTour().getId(), tour.getId())) {
                throw new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_NOT_IN_TOUR);
            }
        }


        matchRepository.save(Match.builder()
                .playerPair1(playerPair1)
                .playerPair2(playerPair2)
                .tour(tour)
                .groupMatch(groupMatch)
                .round(round)
                .status(MatchStatus.UPCOMING)
                .build());

    }

    @Override
    @Transactional
    public void deletePlayerPair(Long id) {
        if (matchRepository.existsByPlayerPair1IdOrPlayerPair2Id(id, id)) {
            throw new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_IN_MATCH);
        }
        playerPairRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void createRound(RoundRequestDto request) {
        Long tourId = request.getTourId();
        String name = request.getName();
        if (roundRepository.existsByTourIdAndName(tourId, name))
        {
            throw new BadRequestException(ErrorConstants.ERR_ROUND_EXISTS);
        }
        Tour tour = tourRepository.findById(tourId).orElseThrow(()-> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));
        roundRepository.save(Round.builder()
                .name(name)
                .tour(tour)
                .type(request.getType())
                .build());
    }

    @Override
    @Transactional
    public void createGroupMatch(GroupMatchRequestDto request) {
        Long tourId = request.getTourId();
        Long roundId = request.getRoundId();
        String name = request.getName();

        if (groupMatchRepository.existsByTourIdAndName(tourId, name)) {
            throw new BadRequestException(ErrorConstants.ERR_GROUP_MATCH_EXISTS);
        }

        Tour tour = tourRepository.findById(tourId).orElseThrow(()-> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));
        Round round = roundRepository.findById(roundId).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_ROUND_NOT_FOUND));
        groupMatchRepository.save(GroupMatch.builder()
                .name(name)
                .tour(tour)
                .round(round)
                .build());
    }

    @Override
    @Transactional
    public void startMatch(StartMatchRequestDto request) {
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_MATCH_NOT_FOUND));

        if (!match.getStatus().equals(MatchStatus.UPCOMING)) {
            throw new BadRequestException(ErrorConstants.ERR_MATCH_INVALID);
        }
        match.setStatus(MatchStatus.LIVE);
        Player servePlayer = playerRepository.findById(request.getServePlayerId())
                        .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));
        Player receivePlayer = playerRepository.findById(request.getReceivePlayerId())
                        .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));
        match.setServePlayer(servePlayer);
        match.setReceivePlayer(receivePlayer);
        match.setStartTime(Instant.now());
    }


    @Override
    @Transactional
    public void addPoint(AddPointRequestDto request) {
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_MATCH_NOT_FOUND));

        if (!match.getStatus().equals(MatchStatus.LIVE)) {
            throw new BadRequestException(ErrorConstants.ERR_MATCH_INVALID);
        }

        PlayerPair playerPair1 = match.getPlayerPair1();
        PlayerPair playerPair2 = match.getPlayerPair2();

        Player receivePlayer = match.getReceivePlayer();
        Player servePlayer = match.getServePlayer();

        Integer score1Set1 = match.getScore1Set1();
        Integer score2Set1 = match.getScore2Set1();
        Integer score1Set2 = match.getScore1Set2();
        Integer score2Set2 = match.getScore2Set2();
        Integer score1Set3 = match.getScore1Set3();
        Integer score2Set3 = match.getScore2Set3();

        matchHistoryRepository.save(MatchHistory.builder()
                .playerPair1(playerPair1)
                .playerPair2(playerPair2)
                .receivePlayer(receivePlayer)
                .servePlayer(servePlayer)
                .match(match)
                .score1Set1(score1Set1)
                .score2Set1(score2Set1)
                .score1Set2(score1Set2)
                .score2Set2(score2Set2)
                .score1Set3(score1Set3)
                .score2Set3(score2Set3)
                .set(match.getSet())
                .build());

        Long receiveId = receivePlayer.getId();
        Long serveId = servePlayer.getId();

        Integer lastPairServe = 1;
        if (playerPair2.getPlayer1().getId().equals(serveId) ||
            (playerPair2.getPlayer2() != null && playerPair2.getPlayer2().getId().equals(serveId)))
        {
            lastPairServe = 2;
        }
        Integer lastScore = score1Set1;
        if (match.getSet().equals(1)) {
            lastScore = score1Set1;
            if (lastPairServe.equals(2)) {
                lastScore = score2Set1;
            }
        } else if (match.getSet().equals(2)) {
            lastScore = score1Set2;
            if (lastPairServe.equals(2)) {
                lastScore = score2Set2;
            }
        } else if (match.getSet().equals(3)) {
            lastScore = score1Set3;
            if (lastPairServe.equals(2)) {
                lastScore = match.getScore2Set3();
            }
        }

        boolean lastScoreEven = lastScore % 2 == 0;

        if (playerPair1.getPlayer2() != null && playerPair2.getPlayer2() != null) {
            if (lastPairServe == 1 && request.getPlayerPairId().equals(playerPair1.getId()))
            {
                Long player1Id = playerPair2.getPlayer1().getId();

                if (receivePlayer.getId().equals(player1Id)) {
                    match.setReceivePlayer(playerPair2.getPlayer2());
                } else {
                    match.setReceivePlayer(playerPair2.getPlayer1());
                }
            }

            if (lastPairServe == 2 && request.getPlayerPairId().equals(playerPair2.getId()))
            {
                Long player1Id = playerPair1.getPlayer1().getId();

                if (receivePlayer.getId().equals(player1Id)) {
                    match.setReceivePlayer(playerPair1.getPlayer2());
                } else {
                    match.setReceivePlayer(playerPair1.getPlayer1());
                }
            }

            if (lastPairServe == 1 && request.getPlayerPairId().equals(playerPair2.getId()))
            {
                boolean isNewScoreEven = true;
                if (match.getSet().equals(1)) {
                    isNewScoreEven = score2Set1 % 2 != 0;
                } else if (match.getSet().equals(2)) {
                    isNewScoreEven = score2Set2 % 2 != 0;
                } else {
                    isNewScoreEven = match.getScore2Set3() % 2 != 0;
                }

                if (isNewScoreEven && lastScoreEven || !isNewScoreEven && !lastScoreEven) {
                    match.setReceivePlayer(servePlayer);
                    match.setServePlayer(receivePlayer);
                } else {
                    if (receivePlayer.getId().equals(playerPair2.getPlayer1().getId())) {
                        match.setServePlayer(playerPair2.getPlayer2());
                    } else {
                        match.setServePlayer(playerPair2.getPlayer1());
                    }

                    if (servePlayer.getId().equals(playerPair1.getPlayer1().getId())) {
                        match.setReceivePlayer(playerPair1.getPlayer2());
                    } else {
                        match.setReceivePlayer(playerPair1.getPlayer1());
                    }
                }
            }

            if (lastPairServe == 2 && request.getPlayerPairId().equals(playerPair1.getId()))
            {
                boolean isNewScoreOdd = true;
                if (match.getSet().equals(1)) {
                    isNewScoreOdd = score1Set1 % 2 != 0;
                } else if (match.getSet().equals(2)) {
                    isNewScoreOdd = score1Set2 % 2 != 0;
                } else {
                    isNewScoreOdd = score1Set3 % 2 != 0;
                }

                if (isNewScoreOdd && lastScoreEven) {
                    match.setReceivePlayer(servePlayer);
                    match.setServePlayer(receivePlayer);
                } else {
                    if (receivePlayer.getId().equals(playerPair1.getPlayer1().getId())) {
                        match.setServePlayer(playerPair1.getPlayer2());
                    } else {
                        match.setServePlayer(playerPair1.getPlayer1());
                    }

                    if (servePlayer.getId().equals(playerPair2.getPlayer1().getId())) {
                        match.setReceivePlayer(playerPair2.getPlayer2());
                    } else {
                        match.setReceivePlayer(playerPair2.getPlayer1());
                    }
                }
            }
        } else {
            if (request.getPlayerPairId().equals(playerPair1.getPlayer1().getId())) {
                match.setServePlayer(playerPair1.getPlayer1());
                match.setReceivePlayer(playerPair2.getPlayer1());
            } else {
                match.setServePlayer(playerPair2.getPlayer1());
                match.setReceivePlayer(playerPair1.getPlayer1());
            }
        }

        if (match.getSet().equals(1)) {
            if (request.getPlayerPairId().equals(playerPair1.getId()))
            {
                match.setScore1Set1(score1Set1 + 1);
            } else {
                match.setScore2Set1(score2Set1 + 1);
            }
        } else if (match.getSet().equals(2)) {
            if (request.getPlayerPairId().equals(playerPair1.getId()))
            {
                match.setScore1Set2(score1Set2 + 1);
            } else {
                match.setScore2Set2(score2Set2 + 1);
            }
        } else if (match.getSet().equals(3)) {
            if (request.getPlayerPairId().equals(playerPair1.getId()))
            {
                match.setScore1Set3(score1Set3 + 1);
            } else {
                match.setScore2Set3(match.getScore2Set3() + 1);
            }
        }

    }

    @Override
    @Transactional
    public void endSet(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_MATCH_NOT_FOUND));

        if (!match.getStatus().equals(MatchStatus.LIVE)) {
            throw new BadRequestException(ErrorConstants.ERR_MATCH_INVALID);
        }

        if (match.getSet() >= 3) {
            throw new BadRequestException(ErrorConstants.ERR_MATCH_INVALID);
        }

        PlayerPair playerPair1 = match.getPlayerPair1();
        PlayerPair playerPair2 = match.getPlayerPair2();

        Player receivePlayer = match.getReceivePlayer();
        Player servePlayer = match.getServePlayer();

        Integer score1Set1 = match.getScore1Set1();
        Integer score2Set1 = match.getScore2Set1();
        Integer score1Set2 = match.getScore1Set2();
        Integer score2Set2 = match.getScore2Set2();
        Integer score1Set3 = match.getScore1Set3();
        Integer score2Set3 = match.getScore2Set3();

        matchHistoryRepository.save(MatchHistory.builder()
                .playerPair1(playerPair1)
                .playerPair2(playerPair2)
                .receivePlayer(receivePlayer)
                .servePlayer(servePlayer)
                .match(match)
                .score1Set1(score1Set1)
                .score2Set1(score2Set1)
                .score1Set2(score1Set2)
                .score2Set2(score2Set2)
                .score1Set3(score1Set3)
                .score2Set3(score2Set3)
                .set(match.getSet())
                .build());


        match.setSet(match.getSet() + 1);
    }

    @Override
    @Transactional
    public void undoMatch(Long id) {
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_MATCH_NOT_FOUND));

        if (!match.getStatus().equals(MatchStatus.LIVE)) {
            throw new BadRequestException(ErrorConstants.ERR_MATCH_INVALID);
        }

        MatchHistory matchHistory = matchHistoryRepository.findFirstByMatchIdOrderByIdDesc(id)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_MATCH_HISTORY_NOT_FOUND));

        match.setReceivePlayer(matchHistory.getReceivePlayer());
        match.setServePlayer(matchHistory.getServePlayer());
        match.setSet(matchHistory.getSet());
        match.setScore1Set1(matchHistory.getScore1Set1());
        match.setScore2Set1(matchHistory.getScore2Set1());
        match.setScore1Set2(matchHistory.getScore1Set2());
        match.setScore2Set2(matchHistory.getScore2Set2());
        match.setScore1Set3(matchHistory.getScore1Set3());
        match.setScore2Set3(matchHistory.getScore2Set3());
        matchHistoryRepository.deleteById(matchHistory.getId());
    }

    @Override
    @Transactional
    public void endMatch(EndMatchRequestDto request) {
        Match match = matchRepository.findById(request.getMatchId())
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_MATCH_NOT_FOUND));

        if (!match.getStatus().equals(MatchStatus.LIVE)) {
            throw new BadRequestException(ErrorConstants.ERR_MATCH_INVALID);
        }

        match.setStatus(MatchStatus.END);

        PlayerPair winner = match.getPlayerPair1();
        if (!request.getPlayerPairWinId().equals(winner.getId()))
        {
            winner = match.getPlayerPair2();
        }
        match.setWinner(winner);
        match.setEndTime(Instant.now());
    }

    @Override
    @Transactional
    public TourDataSummaryResponseDto summaryData(Long id)
    {
        List<Match> matches = matchRepository.findAllByTourIdAndStatus(id, MatchStatus.END);

        Map<Long, Integer> teamTotalPoints = new HashMap<>();
        Map<Long, Integer> teamTotalWins = new HashMap<>();

        Map<Long, Integer> singleMalePoints = new HashMap<>();
        Map<Long, Integer> singleMaleWins = new HashMap<>();

        Map<Long, Integer> singleFemalePoints = new HashMap<>();
        Map<Long, Integer> singleFemaleWins = new HashMap<>();

        Map<Long, Integer> doubleMalePoints = new HashMap<>();
        Map<Long, Integer> doubleMaleWins = new HashMap<>();

        Map<Long, Integer> doubleFemalePoints = new HashMap<>();
        Map<Long, Integer> doubleFemaleWins = new HashMap<>();

        Map<Long, Integer> mixPoints = new HashMap<>();
        Map<Long, Integer> mixWins = new HashMap<>();

        for (Match match : matches) {
            if (match.getPlayerPair1() != null) {
                Long pairId = match.getPlayerPair1().getId();
                PlayerPairType type = match.getPlayerPair1().getType();
                Long teamId = match.getPlayerPair1().getTeam().getId();

                if (type.equals(PlayerPairType.SINGLE_MALE)) {
                    calculatePairData(teamTotalPoints, teamTotalWins, singleMalePoints, singleMaleWins, match, pairId, teamId);
                }
                if (type.equals(PlayerPairType.SINGLE_FEMALE)) {
                    calculatePairData(teamTotalPoints, teamTotalWins, singleFemalePoints, singleFemaleWins, match, pairId, teamId);
                }
                if (type.equals(PlayerPairType.DOUBLE_MALE)) {
                    calculatePairData(teamTotalPoints, teamTotalWins, doubleMalePoints, doubleMaleWins, match, pairId, teamId);
                }
                if (type.equals(PlayerPairType.DOUBLE_FEMALE)) {
                    calculatePairData(teamTotalPoints, teamTotalWins, doubleFemalePoints, doubleFemaleWins, match, pairId, teamId);
                }
                if (type.equals(PlayerPairType.MIX)) {
                    calculatePairData(teamTotalPoints, teamTotalWins, mixPoints, mixWins, match, pairId, teamId);
                }
            }
        }

        return TourDataSummaryResponseDto.builder()
                .teamTotalPoints(teamTotalPoints)
                .teamTotalWins(teamTotalWins)
                .singMalePoints(singleMalePoints)
                .singleMaleWins(singleMaleWins)
                .singleFemalePoints(singleFemalePoints)
                .singleFemaleWins(singleFemaleWins)
                .doubleMalePoints(doubleMalePoints)
                .doubleMaleWins(doubleMaleWins)
                .doubleFemalePoints(doubleFemalePoints)
                .doubleFemaleWins(doubleFemaleWins)
                .mixPoints(mixPoints)
                .mixWins(mixWins)
                .build();
    }

    private void calculatePairData(Map<Long, Integer> teamTotalPoints,
                                   Map<Long, Integer> teamTotalWins,
                                   Map<Long, Integer> pairPoints,
                                   Map<Long, Integer> pairWins,
                                   Match match,
                                   Long pairId,
                                   Long teamId)
    {
        pairPoints.compute(pairId, (k, v) -> {
            int totalPoints = match.getScore1Set1() + match.getScore1Set2() + match.getScore1Set3();
            if (v == null) {
                return totalPoints;
            }
            return v + (totalPoints);
        });
        teamTotalPoints.compute(teamId, (k, v) -> {
            int totalPoints = match.getScore1Set1() + match.getScore1Set2() + match.getScore1Set3();
            if (v == null) {
                return totalPoints;
            }
            return v + (totalPoints);
        });

        if (match.getWinner().getId().equals(pairId)) {
            pairWins.compute(pairId, (k, v) -> {
                if (v == null) {
                    return 1;
                }
                return v + 1;
            });
            teamTotalWins.compute(teamId, (k, v) -> {
                if (v == null) {
                    return 1;
                }
                return v + 1;
            });
        }
    }

}
