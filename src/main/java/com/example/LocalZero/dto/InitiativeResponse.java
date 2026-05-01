package com.example.LocalZero.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InitiativeResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private String visibility;
    private int participantCount;
}