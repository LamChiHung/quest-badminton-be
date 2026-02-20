package com.quest.badminton.service;

import com.quest.badminton.config.specifications.QueryService;
import com.quest.badminton.entity.GroupMatch;
import com.quest.badminton.service.criteria.GroupMatchCriteria;
import com.quest.badminton.service.dto.response.GroupMatchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupMatchQueryService {
    Page<GroupMatchResponseDto> search(GroupMatchCriteria criteria, Pageable pageable);
    GroupMatchResponseDto getGroupMatch(Long id);
}
