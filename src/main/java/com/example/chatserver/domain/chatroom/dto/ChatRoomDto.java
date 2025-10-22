package com.example.chatserver.domain.chatroom.dto;

import com.example.chatserver.domain.chatroom.entity.ChatRoom;
import com.example.chatserver.domain.user.dto.UserSimpleDto;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import lombok.*;

public class ChatRoomDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        @NotBlank(message = "채팅방 이름은 필수입니다")
        @Size(min = 1, max = 20, message = "채팅방 이름은 1-20자여야 합니다")
        private String name;

        @Size(max = 50, message = "설명은 50자 이하여야 합니다")
        private String description;

        @NotNull(message = "creatorId는 필수입니다")
        private Long creatorId;
    }

    public record Response(
        Long id,
        String name,
        String description,
        UserSimpleDto creator,
        Integer participantCount,
        LocalDateTime createdAt
    ) {

        public static Response from(ChatRoom chatRoom) {
            return new Response(
                chatRoom.getId(),
                chatRoom.getName(),
                chatRoom.getDescription(),
                UserSimpleDto.from(chatRoom.getCreator()),
                chatRoom.getParticipants().size(),
                chatRoom.getCreatedAt()
            );
        }
    }
}
