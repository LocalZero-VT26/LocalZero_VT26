package com.example.LocalZero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Data Transfer Object for creating a new initiative.
 * Contains validated input from the client to ensure data integrity.
 */
@Getter
@Setter
public class InitiativeCreateRequest {
    
    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description can be at most 1000 characters")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Duration is required")
    private String duration;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Visibility is required")
    private String visibility;
}