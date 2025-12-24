package com.quest.badminton.service;

import com.quest.badminton.service.dto.request.LoginRequestDto;
import com.quest.badminton.service.dto.request.RegisterRequestDto;
import com.quest.badminton.service.dto.response.LoginResponseDto;
import com.quest.badminton.service.dto.response.MeResponseDto;

public interface AuthService {
    void register(RegisterRequestDto request);

    LoginResponseDto login(LoginRequestDto request);

    void activeToken(String token);

    MeResponseDto getMe(String email);
}
