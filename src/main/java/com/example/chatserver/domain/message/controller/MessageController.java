package com.example.chatserver.domain.message.controller;

import com.example.chatserver.domain.message.dto.MessageDto;
import com.example.chatserver.domain.message.service.MessageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rooms/{roomId}/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ResponseEntity<List<MessageDto.Response>> getRecentMessages(
        @PathVariable Long roomId,
        @RequestParam(defaultValue = "50") int limit) {
        List<MessageDto.Response> messages = messageService.getRecentMessages(roomId, limit);

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/past")
    public ResponseEntity<List<MessageDto.Response>> getPastMessages(
            @PathVariable Long roomId,
            @RequestParam Long lastMessageId,
            @RequestParam(defaultValue = "50") int limit) {
        List<MessageDto.Response> messages = messageService.getPastMessages(roomId, lastMessageId, limit);
        return ResponseEntity.ok(messages);
    }
}
