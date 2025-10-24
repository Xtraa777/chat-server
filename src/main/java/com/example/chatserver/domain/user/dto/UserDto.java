package com.example.chatserver.domain.user.dto;

import com.example.chatserver.domain.user.entity.User;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

public class UserDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "사용자명은 필수입니다")
        @Size(min = 2, max = 10, message = "사용자명은 2-10자여야 합니다")
        private String username;

        @NotBlank(message = "이메일은 필수입니다")
        @Email(message = "올바른 이메일 형식이 아닙니다")
        private String email;

        public User toEntity() {
            return User.builder()
                .username(username)
                .email(email)
                .build();
        }
    }

    public record Response(
        Long id,
        String username,
        String email,
        LocalDateTime createdAt
    ) {

        public static Response from(User user) {
            return new Response(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getCreatedAt()
            );
        }
    }
}
