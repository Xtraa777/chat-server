package com.example.chatserver.domain.chatroom.entity;

import com.example.chatserver.domain.message.entity.Message;
import com.example.chatserver.domain.participant.entity.Participant;
import com.example.chatserver.domain.user.entity.User;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Entity
@Table(name = "chat_rooms")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(length = 500)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User creator;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Participant> participants = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    @Builder.Default
    private List<Message> messages = new ArrayList<>();
}
