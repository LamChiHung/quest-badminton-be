package com.quest.badminton.service.impl;

import com.quest.badminton.config.specifications.QueryService;
import com.quest.badminton.entity.*;
import com.quest.badminton.repository.PlayerPairRepository;
import com.quest.badminton.service.PlayerPairQueryService;
import com.quest.badminton.service.criteria.PlayerPairCriteria;
import com.quest.badminton.service.dto.response.PlayerPairResponseDto;
import com.quest.badminton.service.mapper.BadmintonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerPairQueryServiceImpl extends QueryService<PlayerPair> implements PlayerPairQueryService {
    private final PlayerPairRepository playerPairRepository;
    private final BadmintonMapper badmintonMapper;

    @Transactional(readOnly = true)
    @Override
    public Page<PlayerPairResponseDto> search(PlayerPairCriteria criteria, Pageable pageable, boolean isForAdmin) {
        Specification <PlayerPair> specification = createSpecification(criteria);
        return playerPairRepository.findAll(specification, pageable)
                .map(t -> badmintonMapper.toResponseDto(t, isForAdmin));
    }

    private Specification<PlayerPair> createSpecification(PlayerPairCriteria criteria) {
        Specification<PlayerPair> spec = Specification.anyOf();

        if (criteria == null) {
            return spec;
        }

        if (criteria.getId() != null) {
            spec = spec.and(buildSpecification(criteria.getId(), PlayerPair_.id));
        }

        if (criteria.getTourId() != null) {
            spec = spec.and(buildSpecification(criteria.getTourId(), root -> root.get(PlayerPair_.tour).get(Tour_.id)));
        }

        if (criteria.getTeamId() != null) {
            spec = spec.and(buildSpecification(criteria.getTeamId(), root -> root.get(PlayerPair_.team).get(Team_.id)));
        }

        if (criteria.getType() != null) {
            spec = spec.and(buildSpecification(criteria.getType(), PlayerPair_.type));
        }

        if (criteria.getPlayerId() != null) {
            Specification<PlayerPair> spec1 = Specification.anyOf();
            Specification<PlayerPair> spec2 = Specification.anyOf();

            spec1 = spec1.and(buildSpecification(criteria.getPlayerId(), root -> root.get(PlayerPair_.player1).get(Player_.id)));
            spec2 = spec2.and(buildSpecification(criteria.getPlayerId(), root -> root.get(PlayerPair_.player2).get(Player_.id)));
            spec1 = spec1.or(spec2);
            spec = spec.and(spec1);
        }

        return spec;

    }
}
