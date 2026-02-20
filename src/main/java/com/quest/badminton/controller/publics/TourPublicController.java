package com.quest.badminton.controller.publics;


import com.quest.badminton.config.specifications.filter.BooleanFilter;
import com.quest.badminton.config.specifications.filter.LongFilter;
import com.quest.badminton.entity.enumaration.PlayerStatus;
import com.quest.badminton.service.*;
import com.quest.badminton.service.criteria.*;
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
import org.springframework.data.domain.Sort;
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
    private final GroupMatchQueryService groupMatchQueryService;
    private final MatchQueryService matchQueryService;
    private final RoundQueryService roundQueryService;

    @GetMapping
    public ResponseEntity<Page<TourResponseDto>> search(TourCriteria criteria, @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable)
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

    @GetMapping("/by-id/{id}")
    public ResponseEntity<TourResponseDto> getTourById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(tourQueryService.getTour(id, false, SecurityUtil.getCurrentUserId()));
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
        return ResponseEntity.ok(teamQueryService.search(teamCriteria, PageRequest.of(0, 999999, Sort.by(Sort.Direction.DESC,"id")), false));
    }

    @GetMapping("/{id}/players")
    public ResponseEntity<Page<PlayerResponseDto>> getTourPlayers(@PathVariable("id") Long id) {
        PlayerCriteria playerCriteria = new PlayerCriteria();
        LongFilter tourId = new LongFilter();
        tourId.setEquals(id);
        playerCriteria.setTourId(tourId);
        return ResponseEntity.ok(playerQueryService.search(playerCriteria, PageRequest.of(0, 999999, Sort.by(Sort.Direction.DESC,"id")), false));
    }

    @GetMapping("/{id}/player-pairs")
    public ResponseEntity<Page<PlayerPairResponseDto>> searchPairs(@PathVariable Long id)
    {
        PlayerPairCriteria criteria = new PlayerPairCriteria();
        LongFilter tourId = new LongFilter();
        tourId.setEquals(id);
        criteria.setTourId(tourId);
        Pageable pageable = PageRequest.of(0,999999, Sort.by(Sort.Direction.DESC, "id"));
        return ResponseEntity.ok(playerPairQueryService.search(criteria, pageable, false));
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

        Pageable pageable = PageRequest.of(0,999999, Sort.by(Sort.Direction.DESC,"id"));
        return ResponseEntity.ok(playerQueryService.search(criteria, pageable, false));
    }

    @GetMapping("/teams/{id}/player-pairs")
    public ResponseEntity<Page<PlayerPairResponseDto>> getPlayerPairs(@PathVariable("id") Long id) {
        PlayerPairCriteria criteria = new PlayerPairCriteria();
        LongFilter teamId = new LongFilter();
        teamId.setEquals(id);
        criteria.setTeamId(teamId);
        return ResponseEntity.ok(playerPairQueryService.search(criteria, PageRequest.of(0, 999999, Sort.by(Sort.Direction.DESC,"id")), false));
    }

    @DeleteMapping("/player-pairs/{id}")
    public ResponseEntity<Void> deletePlayerPair(@PathVariable("id") Long id) {
        tourService.deletePlayerPair(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/group-matches")
    public ResponseEntity<Page<GroupMatchResponseDto>> searchGroupMatches(GroupMatchCriteria criteria, @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(groupMatchQueryService.search(criteria, pageable));
    }

    @GetMapping("/rounds")
    public ResponseEntity<Page<RoundResponseDto>> searchRounds(RoundCriteria criteria, @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(roundQueryService.search(criteria, pageable));
    }

    @GetMapping("/group-matches/{id}")
    public ResponseEntity<GroupMatchResponseDto> getGroupMatch(@PathVariable Long id) {
        return ResponseEntity.ok(groupMatchQueryService.getGroupMatch(id));
    }

    @GetMapping("/rounds/{id}")
    public ResponseEntity<RoundResponseDto> getRound(@PathVariable Long id) {
        return ResponseEntity.ok(roundQueryService.getRound(id));
    }

    @GetMapping("/matches")
    public ResponseEntity<Page<MatchResponseDto>> searchMatches(MatchCriteria criteria, @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(matchQueryService.search(criteria, pageable, true));
    }

    @GetMapping("/matches/{id}")
    public ResponseEntity<MatchResponseDto> getMatch(@PathVariable Long id) {
        return ResponseEntity.ok(matchQueryService.getMatch(id, true));
    }

    @GetMapping("/{id}/summary")
    public ResponseEntity<TourDataSummaryResponseDto> tourSummary(@PathVariable Long id) {
        return ResponseEntity.ok(tourService.summaryData(id));
    }


}
