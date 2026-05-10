package com.example.LocalZero.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageResponse {

    private Long id;
    private String senderEmail;
    private String content;
    private LocalDateTime createdAt;
    private boolean read;

    public ChatMessageResponse(Long id, String senderEmail, String content, LocalDateTime createdAt, boolean read) {
        this.id = id;
        this.senderEmail = senderEmail;
        this.content = content;
        this.createdAt = createdAt;
        this.read = read;
    }
}