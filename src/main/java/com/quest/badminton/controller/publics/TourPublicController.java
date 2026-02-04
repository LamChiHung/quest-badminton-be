package com.quest.badminton.controller.publics;


import com.quest.badminton.config.specifications.filter.BooleanFilter;
import com.quest.badminton.config.specifications.filter.LongFilter;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import com.quest.badminton.service.*;
import com.quest.badminton.service.criteria.PlayerCriteria;
import com.quest.badminton.service.criteria.PlayerPairCriteria;
import com.quest.badminton.service.criteria.TeamCriteria;
import com.quest.badminton.service.criteria.TourCriteria;
import com.quest.badminton.service.dto.request.RegisterPlayerPairRequestDto;
import com.quest.badminton.service.dto.request.RegisterTourPlayerRequestDto;
import com.quest.badminton.service.dto.response.*;
import com.quest.badminton.util.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/tours")
@RequiredArgsConstructor
@Slf4j
public class TourPublicController {
    private final TourQueryService tourQueryService;
    private final TourService tourService;
    private final TeamQueryService teamQueryService;
    private final PlayerQueryService playerQueryService;
    private final PlayerPairQueryService playerPairQueryService;

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

    @GetMapping("/{id}/my-team")
    public ResponseEntity<TeamResponseDto> getMyTeam(@PathVariable("id") Long id) {
        return ResponseEntity.ok(teamQueryService.getMyTeam(id, SecurityUtil.getCurrentUserId()));
    }

    @GetMapping("/{id}/teams")
    public ResponseEntity<Page<TeamResponseDto>> getTourTeams(@PathVariable("id") Long id) {
        TeamCriteria teamCriteria = new TeamCriteria();
        LongFilter tourId = new LongFilter();
        tourId.setEquals(id);
        teamCriteria.setTourId(tourId);
        return ResponseEntity.ok(teamQueryService.search(teamCriteria, PageRequest.of(0, 999999), false));
    }

    @GetMapping("/{id}/players")
    public ResponseEntity<Page<PlayerResponseDto>> getTourPlayers(@PathVariable("id") Long id) {
        PlayerCriteria playerCriteria = new PlayerCriteria();
        LongFilter tourId = new LongFilter();
        tourId.setEquals(id);
        playerCriteria.setTourId(tourId);
        return ResponseEntity.ok(playerQueryService.search(playerCriteria, PageRequest.of(0, 999999), false));
    }


    @GetMapping("/teams/{id}/players")
    public ResponseEntity<Page<PlayerResponseDto>> getTeamPlayers(@PathVariable("id") Long id) {
        PlayerCriteria criteria = new PlayerCriteria();
        LongFilter teamId = new LongFilter();
        teamId.setEquals(id);
        PlayerCriteria.StatusFilter status = new PlayerCriteria.StatusFilter();
        status.setEquals(PlayerStatus.APPROVED);

        criteria.setTeamId(teamId);
        criteria.setStatus(status);

        Pageable pageable = PageRequest.of(0,999999);
        return ResponseEntity.ok(playerQueryService.search(criteria, pageable, false));
    }

    @GetMapping("/teams/{id}/player-pairs")
    public ResponseEntity<Page<PlayerPairResponseDto>> getPlayerPairs(@PathVariable("id") Long id) {
        PlayerPairCriteria criteria = new PlayerPairCriteria();
        LongFilter teamId = new LongFilter();
        teamId.setEquals(id);
        criteria.setTeamId(teamId);
        return ResponseEntity.ok(playerPairQueryService.search(criteria, PageRequest.of(0, 999999), false));
    }

    @DeleteMapping("/player-pairs/{id}")
    public ResponseEntity<Void> deletePlayerPair(@PathVariable("id") Long id) {
        tourService.deletePlayerPair(id);
        return ResponseEntity.noContent().build();
    }
}
