package com.quest.badminton.controller.privates;

import com.quest.badminton.service.TourService;
import com.quest.badminton.service.dto.request.TourRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/private/tours")
@RequiredArgsConstructor
public class TourPrivateController {
    private final TourService tourService;

    @PostMapping
    public ResponseEntity<Void> createTour(@Valid @RequestBody TourRequestDto request)
    {
        tourService.createTour(request);
        return ResponseEntity.ok().build();
    }



}
