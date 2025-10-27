package com.example.chatserver.domain.chatroom.service;

import com.example.chatserver.domain.chatroom.dto.ChatRoomDto;
import com.example.chatserver.domain.chatroom.dto.ChatRoomDto.Response;
import com.example.chatserver.domain.chatroom.entity.ChatRoom;
import com.example.chatserver.domain.chatroom.repository.ChatRoomRepository;
import com.example.chatserver.domain.participant.entity.Participant;
import com.example.chatserver.domain.participant.repository.ParticipantRepository;
import com.example.chatserver.domain.user.entity.User;
import com.example.chatserver.domain.user.repository.UserRepository;
import com.example.chatserver.global.exception.BusinessException;
import com.example.chatserver.global.exception.ErrorCode;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public ChatRoomDto.Response createChatRoom(ChatRoomDto.Request chatRoomReqDto) {
        User creator = userRepository.findById(chatRoomReqDto.getCreatorId())
            .orElseThrow(
                () -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        ChatRoom chatRoom = ChatRoom.builder()
            .name(chatRoomReqDto.getName())
            .description(chatRoomReqDto.getDescription())
            .creator(creator)
            .build();

        Participant admin = Participant.builder()
            .user(creator)
            .chatRoom(chatRoom)
            .role(Participant.Role.ADMIN)
            .build();

        chatRoom.addParticipant(admin);
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        log.info("채팅방 생성 완료: id={}, name={}, creator={}",
            savedChatRoom.getId(), chatRoom.getName(), creator.getUsername());

        return ChatRoomDto.Response.from(savedChatRoom);
    }

    public ChatRoomDto.Response getChatRoom(Long id) {
        ChatRoom chatRoom = chatRoomRepository.findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        return ChatRoomDto.Response.from(chatRoom);
    }

    public List<Response> getAllChatRooms() {
        return chatRoomRepository.findAll().stream()
            .map(ChatRoomDto.Response::from)
            .collect(Collectors.toList());
    }

    @Transactional
    public void joinChatRoom(Long roomId, Long userId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
            .orElseThrow(() -> new BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

        if (participantRepository.existsByUserIdAndChatRoomId(userId, roomId)) {
            log.info("사용자가 이미 참여 중입니다: roomId={}, userId={}", roomId, userId);
            return;
        }

        Participant participant = Participant.builder()
            .user(user)
            .chatRoom(chatRoom)
            .role(Participant.Role.MEMBER)
            .build();

        participantRepository.save(participant);

        log.info("채팅방 참여: roomId={}, userId={}, username={}",
            roomId, userId, user.getUsername());
    }

    @Transactional
    public void leaveChatRoom(Long roomId, Long userId) {
        if (!participantRepository.existsByUserIdAndChatRoomId(userId, roomId)) {
            throw new BusinessException(ErrorCode.NOT_A_PARTICIPANT);
        }

        participantRepository.deleteByUserIdAndChatRoomId(userId, roomId);

        log.info("채팅방 퇴장: roomId={}, userId={}", roomId, userId);
    }
}
