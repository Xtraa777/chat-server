package com.example.chatserver.domain.message.controller;

import com.example.chatserver.domain.message.dto.MessageDto;
import com.example.chatserver.domain.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatController {

    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(MessageDto.Request messageReqDto) {
        log.info("메시지 수신: roomId={}, senderId={}, content={}",
            messageReqDto.getRoomId(), messageReqDto.getSenderId(), messageReqDto.getContent());

        MessageDto.Response tempResponse = MessageDto.Response.fromRequest(messageReqDto);

        long broadcastStart = System.currentTimeMillis();
        simpMessagingTemplate.convertAndSend(
            "/topic/room." + messageReqDto.getRoomId(), tempResponse);
        long broadcastEnd = System.currentTimeMillis();

        log.info("메시지 브로드캐스트 완료 {}ms - messageId: temp",
            (broadcastEnd - broadcastStart));

        messageService.saveMessageAsync(messageReqDto)
            .thenAccept(savedMessage ->
                log.info("메시지 저장: id={}", savedMessage.id()))
            .exceptionally(ex -> {
                log.error("메시지 저장 실패: {}", ex.getMessage(), ex);
                return null;
            });
    }

    @MessageMapping("/chat.join")
    public void joinMessage(MessageDto.Request messageReqDto) {
        log.info("입장 메시지 수신: roomId={}, senderId={}, content={}",
            messageReqDto.getRoomId(), messageReqDto.getSenderId(), messageReqDto.getContent());

        MessageDto.Response tempResponse = MessageDto.Response.fromRequest(messageReqDto);
        simpMessagingTemplate.convertAndSend(
            "/topic/room." + messageReqDto.getRoomId(), tempResponse);

        messageService.saveMessageAsync(messageReqDto)
            .thenAccept(savedMessage -> log.info("입장 메시지 저장:id={}", savedMessage.id()));
    }

    @MessageMapping("/chat.leave")
    public void leaveMessage(MessageDto.Request messageReqDto) {
        log.info("퇴장 메시지 수신: roomId={}, senderId={}, content={}",
            messageReqDto.getRoomId(), messageReqDto.getSenderId(), messageReqDto.getContent());

        MessageDto.Response tempResponse = MessageDto.Response.fromRequest(messageReqDto);
        simpMessagingTemplate.convertAndSend(
            "/topic/room." + messageReqDto.getRoomId(), tempResponse);

        messageService.saveMessageAsync(messageReqDto)
            .thenAccept(savedMessage -> log.info("퇴장 메시지 저장: id={}", savedMessage.id()));
    }
}
