package com.quest.badminton.controller.publics;


import com.quest.badminton.service.dto.criteria.TourCriteria;
import com.quest.badminton.service.dto.response.TourResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/tours")
@RequiredArgsConstructor
public class TourPublicController {

    @GetMapping
    public ResponseEntity<Page<TourResponseDto>> search(TourCriteria criteria, @PageableDefault Pageable pageable)
    {
        
    }
}
