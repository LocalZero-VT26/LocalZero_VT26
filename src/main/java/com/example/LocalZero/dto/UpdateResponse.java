package com.example.LocalZero.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor
public class UpdateResponse {
    private Long id;
    private String content;
    private String imageUrl;
    private String authorName;
    private LocalDateTime createdAt;
}