package com.example.chatserver.domain.chatroom.controller;

import com.example.chatserver.domain.chatroom.dto.ChatRoomDto;
import com.example.chatserver.domain.chatroom.dto.ChatRoomDto.Response;
import com.example.chatserver.domain.chatroom.repository.ChatRoomRepository;
import com.example.chatserver.domain.chatroom.service.ChatRoomService;
import jakarta.validation.Valid;
import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomService chatRoomService;

    @PostMapping
    public ResponseEntity<ChatRoomDto.Response> createChatRoom(
        @Valid @RequestBody ChatRoomDto.Request chatRoomReqDto) {
        ChatRoomDto.Response chatRoomResDto = chatRoomService.createChatRoom(chatRoomReqDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomResDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChatRoomDto.Response> getChatRoom(@PathVariable Long id) {
        ChatRoomDto.Response response = chatRoomService.getChatRoom(id);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<Response>> getAllChatRooms() {
        List<ChatRoomDto.Response> responses = chatRoomService.getAllChatRooms();

        return ResponseEntity.ok(responses);
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<Map<String, String>> joinChatRoom(
        @PathVariable Long roomId,
        @RequestParam Long userId) {
        chatRoomService.joinChatRoom(roomId, userId);

        Map<String, String> response = new HashMap<>();
        response.put("message", "채팅방에 참여했습니다");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{roomId}/leave")
    public ResponseEntity<Void> leaveChatRoom(
        @PathVariable Long roomId,
        @RequestParam Long userId) {
        chatRoomService.leaveChatRoom(roomId, userId);

        return ResponseEntity.noContent().build();
    }

}
