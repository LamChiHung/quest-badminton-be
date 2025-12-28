package com.quest.badminton.controller.commons;

import com.quest.badminton.service.AuthService;
import com.quest.badminton.service.dto.request.LoginRequestDto;
import com.quest.badminton.service.dto.request.RegisterRequestDto;
import com.quest.badminton.service.dto.response.LoginResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/common/auth")
@RequiredArgsConstructor
public class AuthCommonController {
    private final AuthService authService;

    @GetMapping("/welcome")
    public ResponseEntity<LoginResponseDto> welcome() {
        return ResponseEntity.ok(LoginResponseDto.builder()
                .token("Welcome")
                .build());
    }


    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto request)
    {
        authService.register(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }

}
