package com.quest.badminton.service.mapper;

import com.quest.badminton.entity.Player;
import com.quest.badminton.entity.Team;
import com.quest.badminton.entity.Tour;
import com.quest.badminton.entity.User;
import com.quest.badminton.entity.enumaration.Gender;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import com.quest.badminton.repository.PlayerRepository;
import com.quest.badminton.service.dto.response.PlayerResponseDto;
import com.quest.badminton.service.dto.response.TourResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadmintonMapper {
    private final PlayerRepository playerRepository;

    public TourResponseDto toResponseDto(Tour entity, boolean isForAdmin) {
        Integer maleRegistered = playerRepository.countAllByTourIdAndGenderAndStatusIn(entity.getId(), Gender.MALE, List.of(PlayerStatus.APPROVED));
        Integer femaleRegistered = playerRepository.countAllByTourIdAndGenderAndStatusIn(entity.getId(), Gender.FEMALE, List.of(PlayerStatus.APPROVED));
        Integer pendingApprovePlayers = null;
        if (isForAdmin) {
            pendingApprovePlayers = playerRepository.countAllByTourIdAndStatusIn(entity.getId(), List.of(PlayerStatus.PENDING_APPROVE));
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
                .startDate(entity.getStartDate())
                .registrationEndDate(entity.getRegistrationEndDate())
                .location(entity.getLocation())
                .locationUrl(entity.getLocationUrl())
                .ruleUrl(entity.getRuleUrl())
                .pendingApprovePlayers(pendingApprovePlayers)
                .build();
    }

    public PlayerResponseDto toResponseDto(Player entity, boolean isForAdmin) {
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
                .build();
    }
}
