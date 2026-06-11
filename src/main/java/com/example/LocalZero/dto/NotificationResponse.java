package com.example.LocalZero.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class NotificationResponse {

    private Long id;
    private String type;
    private String title;
    private String linkTarget;
    private boolean read;
    private LocalDateTime createdAt;

    public NotificationResponse(Long id, String type, String title, String linkTarget, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.linkTarget = linkTarget;
        this.read = read;
        this.createdAt = createdAt;
    }
}
