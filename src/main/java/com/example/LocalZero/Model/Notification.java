package com.example.LocalZero.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * In-app notification shown in the notification bell.
 * Created when something relevant happens for a user, e.g. a new
 * initiative in their neighborhood or a new chat message in their inbox.
 */
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class Notification {

    public Notification(String recipientEmail, NotificationType type, String title, String linkTarget) {
        this.recipientEmail = recipientEmail;
        this.type = type;
        this.title = title;
        this.linkTarget = linkTarget;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String recipientEmail;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType type;

    @Column(nullable = false)
    private String title;

    /** Frontend route to navigate to when the notification is clicked, e.g. "/initiatives/42" or "/inbox". */
    @Column(nullable = false)
    private String linkTarget;

    @Column(nullable = false)
    private boolean read = false;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
