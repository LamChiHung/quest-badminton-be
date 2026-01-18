package com.quest.badminton.controller.publics;


import com.quest.badminton.config.specifications.filter.BooleanFilter;
import com.quest.badminton.service.TourQueryService;
import com.quest.badminton.service.TourService;
import com.quest.badminton.service.criteria.TourCriteria;
import com.quest.badminton.service.dto.request.RegisterPlayerPairRequestDto;
import com.quest.badminton.service.dto.request.RegisterTourPlayerRequestDto;
import com.quest.badminton.service.dto.response.CheckTourRoleResponseDto;
import com.quest.badminton.service.dto.response.TourResponseDto;
import com.quest.badminton.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/tours")
@RequiredArgsConstructor
@Slf4j
public class TourPublicController {
    private final TourQueryService tourQueryService;
    private final TourService tourService;

    @GetMapping
    public ResponseEntity<Page<TourResponseDto>> search(TourCriteria criteria, @PageableDefault Pageable pageable)
    {
        BooleanFilter isPrivate = new BooleanFilter();
        isPrivate.setEquals(false);
        criteria.setIsPrivate(isPrivate);
        return ResponseEntity.ok(tourQueryService.search(criteria, pageable, false, SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/{code}")
    public ResponseEntity<TourResponseDto> getTour(@PathVariable("code") String code) {
        return ResponseEntity.ok(tourQueryService.getTour(code, false, SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/{id}/check-role")
    public ResponseEntity<CheckTourRoleResponseDto> checkTourRole(@PathVariable("id") Long tourId) {
        Long userId = SecurityUtil.getCurrentUserId();
        return ResponseEntity.ok(tourService.checkTourRole(tourId, userId));
    }

    @PostMapping("/register/players")
    public ResponseEntity<Void> registerPlayer(@Valid @RequestBody RegisterTourPlayerRequestDto request)
    {
        tourService.registerPlayer(SecurityUtil.getCurrentUserId(), request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register/player-pairs")
    public ResponseEntity<Void> registerPlayerPair(@Valid @RequestBody RegisterPlayerPairRequestDto request) {
        tourService.registerPlayerPair(SecurityUtil.getCurrentUserId(), request);
        return ResponseEntity.noContent().build();
    }
}
