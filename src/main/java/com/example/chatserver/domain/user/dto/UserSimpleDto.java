package com.example.chatserver.domain.user.dto;

import com.example.chatserver.domain.user.entity.User;

public record UserSimpleDto(
    Long id,
    String username
) {

    public static UserSimpleDto from(User user) {
        return new UserSimpleDto(
            user.getId(),
            user.getUsername()
        );
    }
}
