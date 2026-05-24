package com.example.LocalZero.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateCreateRequest {
    @NotBlank private String content;
    private String imageUrl;
}