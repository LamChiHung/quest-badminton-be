package com.quest.badminton.service.mapper;

import com.quest.badminton.entity.*;
import com.quest.badminton.entity.enumaration.Gender;
import com.quest.badminton.entity.enumaration.MatchStatus;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import com.quest.badminton.entity.enumaration.TourStatus;
import com.quest.badminton.repository.PlayerRepository;
import com.quest.badminton.repository.UserRepository;
import com.quest.badminton.service.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadmintonMapper {
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;

    public TourResponseDto toResponseDto(Tour entity, boolean isForAdmin, Long userId) {
        if (entity == null) return null;
        Integer maleRegistered = playerRepository.countAllByTourIdAndGenderAndStatusIn(entity.getId(), Gender.MALE, List.of(PlayerStatus.APPROVED));
        Integer femaleRegistered = playerRepository.countAllByTourIdAndGenderAndStatusIn(entity.getId(), Gender.FEMALE, List.of(PlayerStatus.APPROVED));
        Integer pendingApprovePlayers = null;
        boolean isAvailableToRegister = entity.getStatus().equals(TourStatus.UPCOMING);
        if (isAvailableToRegister) {
            isAvailableToRegister = maleRegistered < entity.getMalePlayers() || femaleRegistered < entity.getFemalePlayers();
        }
        if (isForAdmin) {
            pendingApprovePlayers = playerRepository.countAllByTourIdAndStatusIn(entity.getId(), List.of(PlayerStatus.PENDING_APPROVE));
        } else {
            if (isAvailableToRegister && userId != null) {
                List<Player> players = playerRepository.findAllByTourIdAndUserId(entity.getId(), userId)
                        .stream()
                        .filter(player -> {
                            PlayerStatus status = player.getStatus();
                            return status.equals(PlayerStatus.APPROVED) ||
                                    status.equals(PlayerStatus.PENDING_APPROVE) ||
                                    status.equals(PlayerStatus.BANNED);
                        })
                        .collect(Collectors.toList());
                if (!players.isEmpty()) {
                    isAvailableToRegister = false;
                }
            }
        }

        return TourResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .code(entity.getCode())
                .status(entity.getStatus())
                .malePlayers(entity.getMalePlayers())
                .malePlayerRegistered(maleRegistered)
                .femalePlayers(entity.getFemalePlayers())
                .femalePlayerRegistered(femaleRegistered)
                .type(entity.getType())
                .matchType(entity.getMatchType())
                .startDate(entity.getStartDate())
                .registrationEndDate(entity.getRegistrationEndDate())
                .location(entity.getLocation())
                .locationUrl(entity.getLocationUrl())
                .ruleUrl(entity.getRuleUrl())
                .pendingApprovePlayers(pendingApprovePlayers)
                .backgroundUrl(entity.getBackgroundUrl())
                .avatarUrl(entity.getAvatarUrl())
                .isAvailableToRegister(isAvailableToRegister)
                .isPrivate(entity.getIsPrivate())
                .build();
    }

    public PlayerResponseDto toResponseDto(Player entity, boolean isForAdmin) {
        if (entity == null) return null;
        Team team = entity.getTeam();
        User user = entity.getUser();
        Tour tour = entity.getTour();
        return PlayerResponseDto.builder()
                .id(entity.getId())
                .userId(user !=null ? user.getId() : null)
                .email(user !=null ? user.getEmail() : null)
                .name(user !=null ? user.getName() : null)
                .tourId(tour != null ? tour.getId() : null)
                .teamId(team != null ? team.getId() : null)
                .tier(entity.getTier())
                .status(entity.getStatus())
                .gender(entity.getGender())
                .note(entity.getNote())
                .club(user!=null ? user.getClub() : null)
                .build();
    }

    public TeamResponseDto toResponseDto(Team entity, boolean isForAdmin) {
        if (entity == null) return null;
        return TeamResponseDto.builder()
                .id(entity.getId())
                .tourId(entity.getTour().getId())
                .number(entity.getNumber())
                .name(entity.getName())
                .captain(toResponseDto(entity.getCaptain(), isForAdmin))
                .build();
    }

    public PlayerPairResponseDto toResponseDto(PlayerPair entity, boolean isForAdmin) {
        if (entity == null) return null;
        return PlayerPairResponseDto.builder()
                .id(entity.getId())
                .player1(toResponseDto(entity.getPlayer1(), isForAdmin))
                .player2(toResponseDto(entity.getPlayer2(), isForAdmin))
                .tourId(entity.getTour().getId())
                .teamId(entity.getTeam().getId())
                .type(entity.getType())
                .build();
    }

    public GroupMatchResponseDto toResponseDto(GroupMatch entity) {
        if (entity == null) return null;
        return GroupMatchResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .tourId(entity.getTour().getId())
                .roundId(entity.getRound().getId())
                .build();
    }

    public RoundResponseDto toResponseDto(Round entity) {
        if (entity == null) return null;
        return RoundResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .tourId(entity.getTour().getId())
                .build();
    }

    public RefereeResponseDto toResponseDto(Referee entity, boolean isForAdmin) {
        if (entity == null) return null;
        return RefereeResponseDto.builder()
                .id(entity.getId())
                .name(entity.getUser().getName())
                .email(entity.getUser().getEmail())
                .status(entity.getStatus())
                .tourId(entity.getTour().getId())
                .note(entity.getNote())
                .approvedBy(isForAdmin ? entity.getApprovedBy() : null)
                .rejectedBy(isForAdmin? entity.getRejectedBy() : null)
                .build();
    }

    public MatchResponseDto toResponseDto(Match entity, boolean isForAdmin) {
        if (entity == null) return null;
        return MatchResponseDto.builder()
                .id(entity.getId())
                .playerPair1(toResponseDto(entity.getPlayerPair1(), isForAdmin))
                .playerPair2(toResponseDto(entity.getPlayerPair2(), isForAdmin))
                .groupMatchId(entity.getGroupMatch().getId())
                .roundId(entity.getRound().getId())
                .tourId(entity.getTour().getId())
                .score1(entity.getScore1())
                .score2(entity.getScore2())
                .status(entity.getStatus())
                .servePlayerId(entity.getServePlayer().getId())
                .receivePlayerId(entity.getReceivePlayer().getId())
                .winnerId(entity.getWinner() != null ? entity.getWinner().getId() : null)
                .referee(entity.getReferee() != null ? toResponseDto(entity.getReferee(), isForAdmin) : null)
                .parentMatch1Id(entity.getParentMatch1() != null ? entity.getParentMatch1().getId() : null)
                .parentMatch2Id(entity.getParentMatch2() != null ? entity.getParentMatch2().getId() : null)
                .build();
    }
}
