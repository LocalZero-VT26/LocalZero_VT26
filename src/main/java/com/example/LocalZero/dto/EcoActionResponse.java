package com.example.LocalZero.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
public class EcoActionResponse {
    private String description;
    private LocalDateTime timestamp;

}