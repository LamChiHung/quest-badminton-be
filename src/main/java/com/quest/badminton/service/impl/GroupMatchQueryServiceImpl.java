package com.quest.badminton.service.impl;

import com.quest.badminton.config.specifications.QueryService;
import com.quest.badminton.entity.*;
import com.quest.badminton.repository.GroupMatchRepository;
import com.quest.badminton.service.GroupMatchQueryService;
import com.quest.badminton.service.criteria.GroupMatchCriteria;
import com.quest.badminton.service.dto.response.GroupMatchResponseDto;
import com.quest.badminton.service.mapper.BadmintonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupMatchQueryServiceImpl extends QueryService<GroupMatch> implements GroupMatchQueryService {
    private final GroupMatchRepository groupMatchRepository;
    private final BadmintonMapper badmintonMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<GroupMatchResponseDto> search(GroupMatchCriteria criteria, Pageable pageable) {
        Specification <GroupMatch> specification = createSpecification(criteria);
        return groupMatchRepository.findAll(specification, pageable)
                .map(t -> badmintonMapper.toResponseDto(t));
    }

    private Specification<GroupMatch> createSpecification(GroupMatchCriteria criteria) {
        Specification<GroupMatch> spec = Specification.anyOf();

        if (criteria == null) {
            return spec;
        }

        if (criteria.getId() != null) {
            spec = spec.and(buildSpecification(criteria.getId(), GroupMatch_.id));
        }

        if (criteria.getTourId() != null) {
            spec = spec.and(buildSpecification(criteria.getTourId(), root -> root.get(GroupMatch_.tour).get(Tour_.id)));
        }

        if (criteria.getName() != null) {
            spec = spec.and(buildStringSpecification(criteria.getName(), GroupMatch_.name));
        }

        return spec;
    }
}
