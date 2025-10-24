package com.example.chatserver.domain.message.service;

import com.example.chatserver.domain.chatroom.entity.ChatRoom;
import com.example.chatserver.domain.chatroom.repository.ChatRoomRepository;
import com.example.chatserver.domain.message.dto.MessageDto;
import com.example.chatserver.domain.message.entity.Message;
import com.example.chatserver.domain.message.entity.Message.MessageType;
import com.example.chatserver.domain.message.repository.MessageRepository;
import com.example.chatserver.domain.user.entity.User;
import com.example.chatserver.domain.user.repository.UserRepository;
import com.example.chatserver.global.exception.NotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MessageService {

    private final MessageRepository messageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageDto.Response saveMessage(MessageDto.Request messageReqDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageReqDto.getRoomId())
            .orElseThrow(
                () -> new NotFoundException("채팅방을 찾을 수 없습니다: " + messageReqDto.getRoomId()));

        User sender = userRepository.findById(messageReqDto.getSenderId())
            .orElseThrow(
                () -> new NotFoundException("사용자를 찾을 수 없습니다: " + messageReqDto.getSenderId()));

        Message message = Message.builder()
            .chatRoom(chatRoom)
            .sender(sender)
            .content(messageReqDto.getContent())
            .type(messageReqDto.getType() != null ?
                messageReqDto.getType() : MessageType.CHAT)
            .build();

        Message savedMessage = messageRepository.save(message);

        log.info("메시지 저장 완료: id={}, roomId={}, senderId={}",
            savedMessage.getId(), chatRoom.getId(), sender.getId());

        return MessageDto.Response.from(savedMessage);
    }

    @Transactional
    public MessageDto.Response joinMessage(MessageDto.Request messageReqDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageReqDto.getRoomId())
            .orElseThrow(
                () -> new NotFoundException("채팅방을 찾을 수 없습니다: " + messageReqDto.getRoomId()));

        User user = userRepository.findById(messageReqDto.getSenderId())
            .orElseThrow(
                () -> new NotFoundException("사용자를 찾을 수 없습니다: " + messageReqDto.getSenderId()));

        Message message = Message.builder()
            .chatRoom(chatRoom)
            .sender(user)
            .content(user.getUsername() + "님이 입장하셨습니다.")
            .type(MessageType.JOIN)
            .build();

        Message savedMessage = messageRepository.save(message);

        return MessageDto.Response.from(savedMessage);
    }

    @Transactional
    public MessageDto.Response leaveMessage(MessageDto.Request messageReqDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(messageReqDto.getRoomId())
            .orElseThrow(
                () -> new NotFoundException("채팅방을 찾을 수 없습니다: " + messageReqDto.getRoomId()));

        User user = userRepository.findById(messageReqDto.getSenderId())
            .orElseThrow(
                () -> new NotFoundException("사용자를 찾을 수 없습니다: " + messageReqDto.getSenderId()));

        Message message = Message.builder()
            .chatRoom(chatRoom)
            .sender(user)
            .content(user.getUsername() + "님이 입장하셨습니다.")
            .type(MessageType.LEAVE)
            .build();

        Message savedMessage = messageRepository.save(message);

        return MessageDto.Response.from(savedMessage);
    }

    public List<MessageDto.Response> getRecentMessages(Long roomId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Direction.DESC, "id"));
        List<Message> messages = messageRepository.findByChatRoomId(roomId, pageable);

        List<MessageDto.Response> responses = messages.stream()
            .map(MessageDto.Response::from)
            .collect(Collectors.toList());

        Collections.reverse(responses);

        return responses;
    }

    public List<MessageDto.Response> getPastMessages(Long roomId, Long lastMessageId, int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "id"));
        List<Message> messages = messageRepository.findByChatRoomIdAndIdLessThan(roomId,
            lastMessageId, pageable);

        List<MessageDto.Response> responses = messages.stream()
            .map(MessageDto.Response::from)
            .collect(Collectors.toList());

        Collections.reverse(responses);

        return responses;
    }
}