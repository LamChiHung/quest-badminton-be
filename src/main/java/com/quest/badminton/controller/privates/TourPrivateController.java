package com.quest.badminton.controller.privates;

import com.quest.badminton.config.specifications.filter.LongFilter;
import com.quest.badminton.service.*;
import com.quest.badminton.service.criteria.*;
import com.quest.badminton.service.dto.request.*;
import com.quest.badminton.service.dto.response.*;
import com.quest.badminton.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
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
    private final TeamQueryService teamQueryService;
    private final PlayerPairQueryService playerPairQueryService;
    private final GroupMatchQueryService groupMatchQueryService;
    private final RoundQueryService roundQueryService;
    private final MatchQueryService matchQueryService;

    @PostMapping
    public ResponseEntity<Void> createTour(@Valid @RequestBody TourRequestDto request)
    {
        tourService.createTour(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Page <TourResponseDto>> searchTours(TourCriteria criteria, @PageableDefault Pageable pageable)
    {
        return ResponseEntity.ok(tourQueryService.search(criteria, pageable, true, SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TourResponseDto> getTour(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tourQueryService.getTour(id, true, SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/players")
    public ResponseEntity<Page<PlayerResponseDto>> searchPlayers(PlayerCriteria criteria, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(playerQueryService.search(criteria, pageable, true));
    }

    @PostMapping("/players/approve")
    public ResponseEntity<Void> approvePlayers(@Valid @RequestBody ApprovePlayerRequestDto request) {
        tourService.approvePlayer(request, SecurityUtil.getCurrentUserId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/teams")
    public ResponseEntity<Page<TeamResponseDto>> searchTeams(TeamCriteria criteria, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(teamQueryService.search(criteria, pageable, true));
    }

    @GetMapping("/teams/{id}")
    public ResponseEntity<TeamResponseDto> getTeam(@PathVariable("id") Long id) {
        return ResponseEntity.ok(teamQueryService.getTeam(id, true));
    }

    @GetMapping("/player-pairs")
    public ResponseEntity<Page<PlayerPairResponseDto>> searchPairs(PlayerPairCriteria criteria, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(playerPairQueryService.search(criteria, pageable, true));
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

    @PostMapping("/group-matches")
    public ResponseEntity<Void> createGroupMatch(@Valid @RequestBody GroupMatchRequestDto request) {
        tourService.createGroupMatch(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/rounds")
    public ResponseEntity<Void> createRound(@Valid @RequestBody RoundRequestDto request) {
        tourService.createRound(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/matchs")
    public ResponseEntity<Void> createMatch(@Valid @RequestBody MatchRequestDto request) {
        tourService.createMatch(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/group-matches")
    public ResponseEntity<Page<GroupMatchResponseDto>> searchGroupMatches(GroupMatchCriteria criteria, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(groupMatchQueryService.search(criteria, pageable));
    }

    @GetMapping("/rounds")
    public ResponseEntity<Page<RoundResponseDto>> searchRounds(RoundCriteria criteria, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(roundQueryService.search(criteria, pageable));
    }

    @GetMapping("/matches")
    public ResponseEntity<Page<MatchResponseDto>> searchMatches(MatchCriteria criteria, @PageableDefault Pageable pageable) {
        return ResponseEntity.ok(matchQueryService.search(criteria, pageable, true));
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<MatchResponseDto> getMatch(@PathVariable Long id) {
        return ResponseEntity.ok(matchQueryService.getMatch(id, true));
    }
}
