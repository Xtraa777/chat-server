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
        log.info("Message received: roomId={}, senderId={}, content={}",
            messageReqDto.getRoomId(), messageReqDto.getSenderId(), messageReqDto.getContent());

        MessageDto.Response savedMessage = messageService.saveMessage(messageReqDto);

        simpMessagingTemplate.convertAndSend(
            "/topic/room." + messageReqDto.getRoomId(), savedMessage);

        log.info("Message broadcasted: messageId={}", savedMessage.id());
    }

    @MessageMapping("/chat.join")
    public void joinMessage(MessageDto.Request messageReqDto) {
        log.info("Message received: roomId={}, senderId={}, content={}",
            messageReqDto.getRoomId(), messageReqDto.getSenderId(), messageReqDto.getContent());

        MessageDto.Response joinMessage = messageService.joinMessage(messageReqDto);

        simpMessagingTemplate.convertAndSend(
            "/topic/room." + messageReqDto.getRoomId(), joinMessage);

        log.info("Message broadcasted: messageId={}", joinMessage.id());
    }

    @MessageMapping("/chat.leave")
    public void leaveMessage(MessageDto.Request messageReqDto) {
        log.info("Message received: roomId={}, senderId={}, content={}",
            messageReqDto.getRoomId(), messageReqDto.getSenderId(), messageReqDto.getContent());

        MessageDto.Response leaveMessage = messageService.leaveMessage(messageReqDto);

        simpMessagingTemplate.convertAndSend(
            "/topic/room." + messageReqDto.getRoomId(), leaveMessage);

        log.info("Message broadcasted: messageId={}", leaveMessage.id());
    }
}
