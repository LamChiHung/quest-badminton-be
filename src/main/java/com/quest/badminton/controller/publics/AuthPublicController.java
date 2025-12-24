package com.quest.badminton.controller.publics;

import com.quest.badminton.service.AuthService;
import com.quest.badminton.service.dto.response.MeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public/auth")
@RequiredArgsConstructor
public class AuthPublicController {
    private final AuthService authService;

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @GetMapping("/me")
    public ResponseEntity<MeResponseDto> getMe() {
        return ResponseEntity.ok(authService.getMe(SecurityContextHolder.getContext().getAuthentication().getName()));
    }
}
