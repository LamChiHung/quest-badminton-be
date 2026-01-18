package com.quest.badminton.service.impl;

import com.quest.badminton.config.specifications.QueryService;
import com.quest.badminton.constant.ErrorConstants;
import com.quest.badminton.entity.Tour;
import com.quest.badminton.entity.Tour_;
import com.quest.badminton.exception.BadRequestException;
import com.quest.badminton.repository.TourRepository;
import com.quest.badminton.service.TourQueryService;
import com.quest.badminton.service.criteria.TourCriteria;
import com.quest.badminton.service.dto.response.TourResponseDto;
import com.quest.badminton.service.mapper.BadmintonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TourQueryServiceImpl extends QueryService <Tour> implements TourQueryService {
    private final TourRepository tourRepository;
    private final BadmintonMapper badmintonMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TourResponseDto> search(TourCriteria criteria, Pageable pageable, boolean isForAdmin, Long userId) {
        Specification<Tour> specification = createSpecification(criteria);
        return tourRepository.findAll(specification, pageable)
                .map(t -> badmintonMapper.toResponseDto(t, isForAdmin, userId));
    }

    @Override
    @Transactional(readOnly = true)
    public TourResponseDto getTour(String code, boolean isForAdmin, Long userId) {
        return badmintonMapper.toResponseDto(
                tourRepository.findByCode(code)
                        .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND)),
                isForAdmin,
                userId);
    }

    @Override
    @Transactional(readOnly = true)
    public TourResponseDto getTour(Long id, boolean isForAdmin, Long userId) {
        return badmintonMapper.toResponseDto(
                tourRepository.findById(id)
                        .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND)),
                isForAdmin,
                userId);
    }

    private Specification<Tour> createSpecification(TourCriteria criteria) {
        Specification<Tour> spec = Specification.anyOf();

        if (criteria == null) {
            return spec;
        }

        if (criteria.getId() != null) {
            spec = spec.and(buildSpecification(criteria.getId(), Tour_.id));
        }

        if (criteria.getCode() != null) {
            spec = spec.and(buildStringSpecification(criteria.getCode(), Tour_.code));
        }

        if (criteria.getStatus() != null) {
            spec = spec.and(buildSpecification(criteria.getStatus(), Tour_.status));
        }

        if (criteria.getType() != null) {
            spec = spec.and(buildSpecification(criteria.getType(), Tour_.type));
        }

        if (criteria.getMatchType() != null) {
            spec = spec.and(buildSpecification(criteria.getMatchType(), Tour_.matchType));
        }

        if (criteria.getName() != null) {
            spec = spec.and(buildStringSpecification(criteria.getName(), Tour_.name));
        }

        if (criteria.getLocation() != null) {
            spec = spec.and(buildStringSpecification(criteria.getLocation(), Tour_.location));
        }

        if (criteria.getFromStartDate() != null) {
            spec = spec.and(buildRangeSpecification(criteria.getFromStartDate(), Tour_.startDate));
        }

        if (criteria.getToStartDate() != null) {
            spec = spec.and(buildRangeSpecification(criteria.getToStartDate(), Tour_.startDate));
        }

        if (criteria.getIsPrivate() != null) {
            spec = spec.and(buildSpecification(criteria.getIsPrivate(), Tour_.isPrivate));
        }

        return spec;


    }
}
