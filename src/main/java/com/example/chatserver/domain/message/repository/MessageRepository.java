package com.example.chatserver.domain.message.repository;

import com.example.chatserver.domain.message.entity.Message;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m " +
        "JOIN FETCH m.sender " +
        "WHERE m.chatRoom.id = :roomId " +
        "ORDER BY m.id DESC")
    List<Message> findByChatRoomIdWithSender(
        @Param("roomId") Long roomId,
        Pageable pageable);

    @Query("SELECT m FROM Message m " +
        "JOIN FETCH m.sender " +
        "WHERE m.chatRoom.id = :roomId AND m.id < :lastMessageId " +
        "ORDER BY m.id DESC")
    List<Message> findPastMessagesWithSender(
        @Param("roomId") Long roomId,
        @Param("lastMessageId") Long lastMessageId,
        Pageable pageable);

    List<Message> findByChatRoomId(Long roomId, Pageable pageable);

    List<Message> findByChatRoomIdAndIdLessThan(Long roomId, Long lastMessageId, Pageable pageable);
}