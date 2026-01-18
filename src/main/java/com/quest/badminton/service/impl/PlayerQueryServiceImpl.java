package com.quest.badminton.service.impl;

import com.quest.badminton.config.specifications.QueryService;
import com.quest.badminton.config.specifications.filter.LongFilter;
import com.quest.badminton.constant.ErrorConstants;
import com.quest.badminton.entity.Player;
import com.quest.badminton.entity.Player_;
import com.quest.badminton.entity.Team_;
import com.quest.badminton.entity.Tour_;
import com.quest.badminton.entity.User;
import com.quest.badminton.entity.User_;
import com.quest.badminton.exception.BadRequestException;
import com.quest.badminton.repository.PlayerRepository;
import com.quest.badminton.repository.UserRepository;
import com.quest.badminton.service.PlayerQueryService;
import com.quest.badminton.service.criteria.PlayerCriteria;
import com.quest.badminton.service.dto.response.PlayerResponseDto;
import com.quest.badminton.service.mapper.BadmintonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerQueryServiceImpl extends QueryService<Player> implements PlayerQueryService {
    private final PlayerRepository playerRepository;
    private final UserRepository userRepository;
    private final BadmintonMapper badmintonMapper;

    @Override
    @Transactional(readOnly = true)
    public Page <PlayerResponseDto> search(PlayerCriteria criteria, Pageable pageable, boolean isForAdmin) {
        Specification <Player> specification = createSpecification(criteria);
        return playerRepository.findAll(specification, pageable)
                .map(t -> badmintonMapper.toResponseDto(t, isForAdmin));
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerResponseDto getPlayer(Long id, boolean isForAdmin) {
        return badmintonMapper.toResponseDto(
                playerRepository.findById(id)
                        .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_PLAYER_NOT_FOUND)),
                isForAdmin);
    }

    private Specification<Player> createSpecification(PlayerCriteria criteria) {
        Specification<Player> spec = Specification.anyOf();

        if (criteria == null) {
            return spec;
        }

        if (criteria.getId() != null) {
            spec = spec.and(buildSpecification(criteria.getId(), Player_.id));
        }

        if (criteria.getTourId() != null) {
            spec = spec.and(buildSpecification(criteria.getTourId(), root -> root.get(Player_.tour).get(Tour_.id)));
        }

        if (criteria.getTeamId() != null) {
            spec = spec.and(buildSpecification(criteria.getTeamId(), root -> root.get(Player_.team).get(Team_.id)));
        }

        if (criteria.getStatus() != null) {
            spec = spec.and(buildSpecification(criteria.getStatus(), Player_.status));
        }

        if (criteria.getTier() != null) {
            spec = spec.and(buildSpecification(criteria.getTier(), Player_.tier));
        }

        if (criteria.getGender() != null) {
            spec = spec.and(buildSpecification(criteria.getGender(), Player_.gender));
        }

        if (criteria.getEmail() != null) {
            String email = criteria.getEmail().getContains();
            List<User> users = userRepository.findAllByEmailContaining(email);
            if (!users.isEmpty()) {
                LongFilter userId = new LongFilter();
                userId.setIn(users.stream().map(User::getId).toList());
                spec = spec.and(buildSpecification(userId, root -> root.get(Player_.user).get(User_.id)));
            }
        }

        if (criteria.getName() != null) {
            String name = criteria.getName().getContains();
            List<User> users = userRepository.findAllByNameContaining(name);
            if (!users.isEmpty()) {
                LongFilter userId = new LongFilter();
                userId.setIn(users.stream().map(User::getId).toList());
                spec = spec.and(buildSpecification(userId, root -> root.get(Player_.user).get(User_.id)));
            }
        }

        if (criteria.getUserId() != null) {
            spec = spec.and(buildSpecification(criteria.getUserId(), root -> root.get(Player_.user).get(User_.id)));
        }

        return spec;

    }
}
