package com.example.chatserver.domain.user.entity;

import com.example.chatserver.domain.chatroom.entity.ChatRoom;
import com.example.chatserver.domain.message.entity.Message;
import com.example.chatserver.domain.participant.entity.Participant;
import com.example.chatserver.global.common.BaseEntity;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "creator")
    @Builder.Default
    private List<ChatRoom> createdRooms = new ArrayList<>();
}