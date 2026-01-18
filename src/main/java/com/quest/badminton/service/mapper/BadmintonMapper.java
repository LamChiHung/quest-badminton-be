package com.quest.badminton.service.mapper;

import com.quest.badminton.entity.Player;
import com.quest.badminton.entity.Team;
import com.quest.badminton.entity.Tour;
import com.quest.badminton.entity.User;
import com.quest.badminton.entity.enumaration.Gender;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import com.quest.badminton.entity.enumaration.TourStatus;
import com.quest.badminton.repository.PlayerRepository;
import com.quest.badminton.repository.UserRepository;
import com.quest.badminton.service.dto.response.PlayerResponseDto;
import com.quest.badminton.service.dto.response.TeamResponseDto;
import com.quest.badminton.service.dto.response.TourResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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
}
