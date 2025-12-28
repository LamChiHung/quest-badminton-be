package com.quest.badminton.controller.commons;

import com.quest.badminton.service.AuthService;
import com.quest.badminton.service.dto.request.LoginRequestDto;
import com.quest.badminton.service.dto.request.RegisterRequestDto;
import com.quest.badminton.service.dto.response.LoginResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/register/confirm")
    public ResponseEntity<Void> confirmRegister(@RequestParam("token") String token)
    {
        authService.activeToken(token);
        return ResponseEntity.noContent().build();
    }

}
