package com.quest.badminton.service.impl;

import com.quest.badminton.config.specifications.QueryService;
import com.quest.badminton.constant.ErrorConstants;
import com.quest.badminton.entity.*;
import com.quest.badminton.exception.BadRequestException;
import com.quest.badminton.repository.RoundRepository;
import com.quest.badminton.service.RoundQueryService;
import com.quest.badminton.service.criteria.RoundCriteria;
import com.quest.badminton.service.dto.response.RoundResponseDto;
import com.quest.badminton.service.mapper.BadmintonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoundQueryServiceImpl extends QueryService<Round> implements RoundQueryService {

    private final RoundRepository roundRepository;
    private final BadmintonMapper badmintonMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<RoundResponseDto> search(RoundCriteria criteria, Pageable pageable) {
        Specification<Round> specification = createSpecification(criteria);
        return roundRepository.findAll(specification, pageable)
                .map(t -> badmintonMapper.toResponseDto(t));
    }

    @Override
    @Transactional(readOnly = true)
    public RoundResponseDto getRound(Long id) {
        return badmintonMapper.toResponseDto(roundRepository.findById(id).orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_ROUND_NOT_FOUND)));
    }

    private Specification<Round> createSpecification(RoundCriteria criteria) {
        Specification<Round> spec = Specification.anyOf();

        if (criteria == null) {
            return spec;
        }

        if (criteria.getId() != null) {
            spec = spec.and(buildSpecification(criteria.getId(), Round_.id));
        }

        if (criteria.getTourId() != null) {
            spec = spec.and(buildSpecification(criteria.getTourId(), root -> root.get(Round_.tour).get(Tour_.id)));
        }

        if (criteria.getName() != null) {
            spec = spec.and(buildStringSpecification(criteria.getName(), Round_.name));
        }

        return spec;
    }
}
