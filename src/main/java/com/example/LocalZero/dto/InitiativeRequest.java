package com.example.LocalZero.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * This is a request class that suits the model-class "Initiative".
 */

@Getter
@Setter
public class InitiativeRequest {

    @NotBlank (message = "Title is required")
    private String title;

    @NotBlank (message = "Description is required")
    @Size (max = 1000, message = "Description can be at most 1000 characters")
    private String description;

    @NotBlank (message = "Location is required")
    private String location;

    @NotBlank (message = "Duration is required")
    private String duration;

    @NotBlank (message = "Category is required")
    private String category;
}
