package com.example.chatserver.domain.message.repository;

import com.example.chatserver.domain.message.entity.Message;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    List<Message> findByChatRoomId(Long roomId, Pageable pageable);

    List<Message> findByChatRoomIdAndIdLessThan(Long roomId, Long lastMessageId, Pageable pageable);
}