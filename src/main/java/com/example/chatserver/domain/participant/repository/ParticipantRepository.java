package com.example.chatserver.domain.participant.repository;

import com.example.chatserver.domain.participant.entity.Participant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participant, Long> {

    boolean existsByUserIdAndChatRoomId(Long userId, Long roomId);

    void deleteByUserIdAndChatRoomId(Long userId, Long roomId);
}
