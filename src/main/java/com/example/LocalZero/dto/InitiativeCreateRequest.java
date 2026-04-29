package com.example.LocalZero.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class InitiativeCreateRequest {
    @NotBlank private String title;
    @NotBlank private String description;
    @NotBlank private String location;
    @NotBlank private String duration;
    @NotBlank private String category;
    @NotBlank private String visibility;
}