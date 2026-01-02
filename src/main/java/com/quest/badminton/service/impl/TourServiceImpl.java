package com.quest.badminton.service.impl;

import com.quest.badminton.constant.ErrorConstants;
import com.quest.badminton.entity.GroupMatch;
import com.quest.badminton.entity.Player;
import com.quest.badminton.entity.PlayerPair;
import com.quest.badminton.entity.Referee;
import com.quest.badminton.entity.Team;
import com.quest.badminton.entity.Tour;
import com.quest.badminton.entity.User;
import com.quest.badminton.entity.enumaration.Gender;
import com.quest.badminton.entity.enumaration.PlayerPairType;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import com.quest.badminton.entity.enumaration.PlayerTier;
import com.quest.badminton.entity.enumaration.TourRole;
import com.quest.badminton.entity.enumaration.TourStatus;
import com.quest.badminton.exception.BadRequestException;
import com.quest.badminton.repository.GroupMatchRepository;
import com.quest.badminton.repository.PlayerPairRepository;
import com.quest.badminton.repository.PlayerRepository;
import com.quest.badminton.repository.RefereeRepository;
import com.quest.badminton.repository.TeamRepository;
import com.quest.badminton.repository.TourRepository;
import com.quest.badminton.repository.UserRepository;
import com.quest.badminton.service.TourService;
import com.quest.badminton.service.dto.request.AddPlayerToTeamRequestDto;
import com.quest.badminton.service.dto.request.ApprovePlayerRequestDto;
import com.quest.badminton.service.dto.request.GroupMatchRequestDto;
import com.quest.badminton.service.dto.request.RegisterPlayerPairRequestDto;
import com.quest.badminton.service.dto.request.RegisterTourPlayerRequestDto;
import com.quest.badminton.service.dto.request.TeamRequestDto;
import com.quest.badminton.service.dto.request.TourRequestDto;
import com.quest.badminton.service.dto.response.CheckTourRoleResponseDto;
import com.quest.badminton.util.CodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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
                .startDate(request.getStartDate())
                .registrationEndDate(request.getRegistrationEndDate())
                .location(request.getLocation())
                .locationUrl(request.getLocationUrl())
                .ruleUrl(request.getRuleUrl())
                .isPrivate(request.getIsPrivate())
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

        List<Player> players = playerRepository.findAllById(playerIds);

        for (Player player : players) {
            if (!Objects.equals(player.getTour().getId(), team.getTour().getId())) {
                throw new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_IN_TOUR);
            }
            player.setTeam(team);
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
        if (playerPairRepository.existsByTourIdAndPlayer1IdAndPlayer2Id(tourId, player1Id, player2Id) ||
            playerPairRepository.existsByTourIdAndPlayer1IdAndPlayer2Id(tourId, player2Id, player1Id))
        {
            throw new BadRequestException(ErrorConstants.ERR_PLAYER_PAIR_EXISTS);
        }

        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));
        Player player1 = playerRepository.findById(player1Id).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));
        Player player2 = playerRepository.findById(player2Id).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));

        Player captain = playerRepository.findAllByTourIdAndUserId(tourId, userId)
                .stream()
                .filter(p -> p.getStatus().equals(PlayerStatus.APPROVED))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));

        Team team = teamRepository.findByTourIdAndCaptainId(tourId, captain.getId())
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TEAM_NOT_FOUND));

        if (!Objects.equals(player1.getTeam().getId(), team.getId()) || !Objects.equals(player2.getTeam().getId(), team.getId())) {
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
        Long userId = request.getUserId();
        String note = request.getNote();
        boolean isApprove = request.isApprove();

        Tour tour = tourRepository.findById(tourId).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));
        Player player = playerRepository.findById(userId).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND));

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
    public void createGroupMatch(GroupMatchRequestDto request) {
        Long tourId = request.getTourId();
        String name = request.getName();
        Integer order = request.getOrder();

        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND));

        groupMatchRepository.save(GroupMatch.builder()
                .tour(tour)
                .name(name)
                .orders(order)
                .build());
    }

}
