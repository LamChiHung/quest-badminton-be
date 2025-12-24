package com.quest.badminton.service.impl;

import com.quest.badminton.constant.MailConstants;
import com.quest.badminton.entity.User;
import com.quest.badminton.entity.enumaration.Role;
import com.quest.badminton.exception.BadRequestException;
import com.quest.badminton.repository.UserRepository;
import com.quest.badminton.service.AuthService;
import com.quest.badminton.service.MailService;
import com.quest.badminton.service.dto.request.LoginRequestDto;
import com.quest.badminton.service.dto.request.RegisterRequestDto;
import com.quest.badminton.service.dto.response.LoginResponseDto;
import com.quest.badminton.service.dto.response.MeResponseDto;
import com.quest.badminton.service.mapper.UserMapper;
import com.quest.badminton.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.quest.badminton.constant.ErrorConstants.ERR_CREDENTIALS_INVALID;
import static com.quest.badminton.constant.ErrorConstants.ERR_EMAIL_EXISTED;
import static com.quest.badminton.constant.ErrorConstants.ERR_USER_INACTIVE;
import static com.quest.badminton.constant.ErrorConstants.ERR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    /* Repository */
    private final UserRepository userRepository;

    /* Service */
    private final MailService mailService;

    /* Others */
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${application.redirect.register-confirm}")
    public String registerConfirmUrl;

    @Override
    @Transactional
    public void register(RegisterRequestDto request) {
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new BadRequestException(ERR_EMAIL_EXISTED);
        }
        String registerToken = UUID.randomUUID().toString();

        userRepository.save(User.builder()
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .club(request.getClub())
                .roles(Role.ROLE_USER.name())
                .registerToken(registerToken)
                .build());

        String url = registerConfirmUrl + registerToken;
        Map<String, String> messageParams = new HashMap <>();
        messageParams.put(MailConstants.NAME, request.getName());
        messageParams.put(MailConstants.URL, url);
        mailService.send(request.getEmail(), "register", null, messageParams);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto request) {
        User user = userRepository.findByEmailIgnoreCase(request.getEmail())
                .orElseThrow(() -> new BadRequestException(ERR_CREDENTIALS_INVALID));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestException(ERR_CREDENTIALS_INVALID);
        }

        if (!user.isEnabled()) {
            throw new BadRequestException(ERR_USER_INACTIVE);
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return LoginResponseDto.builder()
                .token(token)
                .build();
    }

    @Override
    @Transactional
    public void activeToken(String token) {
        User user = userRepository.findByRegisterToken(token)
                .orElse(null);
        if (Objects.isNull(user)) {
            log.error("User not found, token: {}", token);
            return;
        }

        user.setEnabled(true);
    }

    @Override
    public MeResponseDto getMe(String email) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new BadRequestException(ERR_USER_NOT_FOUND));

        return userMapper.meResponseDto(user);
    }
}
