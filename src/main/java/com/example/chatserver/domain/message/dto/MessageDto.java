package com.example.chatserver.domain.message.dto;

import com.example.chatserver.domain.message.entity.Message;
import com.example.chatserver.domain.user.dto.UserSimpleDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.*;

public class MessageDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotNull(message = "roomId는 필수입니다")
        private Long roomId;

        @NotNull(message = "senderId는 필수입니다")
        private Long senderId;

        @NotBlank(message = "메시지 내용은 필수입니다")
        private String content;

        private Message.MessageType type;
    }

    public record Response(
        Long id,
        Long roomId,
        UserSimpleDto sender,
        String content,
        Message.MessageType type,
        LocalDateTime createdAt
    ) {

        public static Response from(Message message) {
            return new Response(
                message.getId(),
                message.getChatRoom().getId(),
                UserSimpleDto.from(message.getSender()),
                message.getContent(),
                message.getType(),
                message.getCreatedAt()
            );
        }
    }
}
