package com.quest.badminton.service.impl;

import com.quest.badminton.config.specifications.QueryService;
import com.quest.badminton.constant.ErrorConstants;
import com.quest.badminton.entity.*;
import com.quest.badminton.exception.BadRequestException;
import com.quest.badminton.repository.MatchRepository;
import com.quest.badminton.service.MatchQueryService;
import com.quest.badminton.service.criteria.MatchCriteria;
import com.quest.badminton.service.dto.response.MatchResponseDto;
import com.quest.badminton.service.mapper.BadmintonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MatchQueryServiceImpl extends QueryService<Match> implements MatchQueryService {
    private final MatchRepository matchRepository;
    private final BadmintonMapper badmintonMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<MatchResponseDto> search(MatchCriteria criteria, Pageable pageable, boolean isForAdmin) {
        Specification<Match> specification = createSpecification(criteria);
        return matchRepository.findAll(specification, pageable)
                .map(t -> badmintonMapper.toResponseDto(t, isForAdmin));
    }

    @Override
    @Transactional(readOnly = true)
    public MatchResponseDto getMatch(Long id, boolean isForAdmin) {
        return badmintonMapper.toResponseDto(
                matchRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_MATCH_NOT_FOUND)),
                isForAdmin);
    }

    private Specification<Match> createSpecification(MatchCriteria criteria) {
        Specification<Match> spec = Specification.anyOf();

        if (criteria == null) {
            return spec;
        }

        if (criteria.getId() != null) {
            spec = spec.and(buildSpecification(criteria.getId(), Match_.id));
        }

        if (criteria.getTourId() != null) {
            spec = spec.and(buildSpecification(criteria.getTourId(), root -> root.get(Match_.tour).get(Tour_.id)));
        }

        if (criteria.getGroupMatchId() != null) {
            spec = spec.and(buildSpecification(criteria.getGroupMatchId(), root -> root.get(Match_.groupMatch).get(GroupMatch_.id)));
        }

        if (criteria.getRoundId() != null) {
            spec = spec.and(buildSpecification(criteria.getRoundId(), root -> root.get(Match_.round).get(Round_.id)));
        }

        return spec;
    }
}
