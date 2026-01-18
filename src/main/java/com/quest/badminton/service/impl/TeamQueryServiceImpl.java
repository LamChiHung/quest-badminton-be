package com.quest.badminton.service.impl;

import com.quest.badminton.config.specifications.QueryService;
import com.quest.badminton.constant.ErrorConstants;
import com.quest.badminton.entity.Team;
import com.quest.badminton.entity.Team_;
import com.quest.badminton.entity.Tour_;
import com.quest.badminton.exception.BadRequestException;
import com.quest.badminton.repository.TeamRepository;
import com.quest.badminton.service.TeamQueryService;
import com.quest.badminton.service.criteria.TeamCriteria;
import com.quest.badminton.service.dto.response.TeamResponseDto;
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
public class TeamQueryServiceImpl extends QueryService <Team> implements TeamQueryService {
    private final TeamRepository teamRepository;
    private final BadmintonMapper badmintonMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponseDto> search(TeamCriteria criteria, Pageable pageable, boolean isForAdmin) {
        Specification<Team> specification = createSpecification(criteria);
        return teamRepository.findAll(specification, pageable)
                .map(t -> badmintonMapper.toResponseDto(t, isForAdmin));
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponseDto getTeam(Long id, boolean isForAdmin) {
        return badmintonMapper.toResponseDto(
                teamRepository.findById(id)
                        .orElseThrow(() -> new BadRequestException(ErrorConstants.ERR_TOUR_NOT_FOUND)),
                isForAdmin);
    }

    private Specification<Team> createSpecification(TeamCriteria criteria) {
        Specification<Team> spec = Specification.anyOf();

        if (criteria == null) {
            return spec;
        }

        if (criteria.getId() != null) {
            spec = spec.and(buildSpecification(criteria.getId(), Team_.id));
        }

        if (criteria.getTourId() != null) {
            spec = spec.and(buildSpecification(criteria.getTourId(),
                    root -> root.get(Team_.tour).get(Tour_.id)));
        }


        if (criteria.getName() != null) {
            spec = spec.and(buildStringSpecification(criteria.getName(), Team_.name));
        }

        return spec;
    }
}
