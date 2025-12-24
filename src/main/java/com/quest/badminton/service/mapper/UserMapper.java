package com.quest.badminton.service.mapper;

import com.quest.badminton.entity.User;
import com.quest.badminton.service.dto.response.MeResponseDto;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public MeResponseDto meResponseDto(User user) {
        return MeResponseDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .roles(user.getRoles())
                .build();
    }
}
