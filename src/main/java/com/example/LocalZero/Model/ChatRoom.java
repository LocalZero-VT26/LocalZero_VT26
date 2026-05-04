package com.example.LocalZero.Model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_rooms", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user1_email", "user2_email"})
})
@Getter
@Setter
@NoArgsConstructor
public class ChatRoom {

    public ChatRoom(String user1Email, String user2Email) {
        this.user1Email = user1Email;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user1_email", nullable = false)
    private String user1Email;

    @Column(name = "user2_email", nullable = false)
    private String user2Email;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
