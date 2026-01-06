package com.quest.badminton.controller.privates;

import com.quest.badminton.entity.Match;
import com.quest.badminton.service.PlayerQueryService;
import com.quest.badminton.service.TourQueryService;
import com.quest.badminton.service.TourService;
import com.quest.badminton.service.dto.criteria.PlayerCriteria;
import com.quest.badminton.service.dto.criteria.TourCriteria;
import com.quest.badminton.service.dto.request.*;
import com.quest.badminton.service.dto.response.PlayerResponseDto;
import com.quest.badminton.service.dto.response.TourResponseDto;
import com.quest.badminton.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/private/tours")
@RequiredArgsConstructor
public class TourPrivateController {
    private final TourService tourService;
    private final TourQueryService tourQueryService;
    private final PlayerQueryService playerQueryService;

    @PostMapping
    public ResponseEntity<Void> createTour(@Valid @RequestBody TourRequestDto request)
    {
        tourService.createTour(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page <TourResponseDto>> searchTour(TourCriteria criteria, @PageableDefault Pageable pageable)
    {
        return ResponseEntity.ok(tourQueryService.search(criteria, pageable, true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourResponseDto> getTour(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tourQueryService.getTour(id, true));
    }

    @GetMapping("/players")
    public ResponseEntity<Page<PlayerResponseDto>> searchPlayer(PlayerCriteria criteria, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(playerQueryService.search(criteria, pageable, true));
    }

    @PostMapping("/players/approve")
    public ResponseEntity<Void> approvePlayers(@Valid @RequestBody ApprovePlayerRequestDto request) {
        tourService.approvePlayer(request, SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/teams")
    public ResponseEntity<Void> createTeam(@Valid @RequestBody TeamRequestDto request) {
        tourService.createTeam(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/teams/players")
    public ResponseEntity<Void> addPlayersToTeam(@Valid @RequestBody AddPlayerToTeamRequestDto request) {
        tourService.addPlayerToTeam(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/group-matchs")
    public ResponseEntity<Void> createGroupMatch(@Valid @RequestBody GroupMatchRequestDto request) {
        tourService.createGroupMatch(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/matchs")
    public ResponseEntity<Void> createMatch(@Valid @RequestBody MatchRequestDto request) {
        tourService.createMatch(request);
        return ResponseEntity.noContent().build();
    }
}
