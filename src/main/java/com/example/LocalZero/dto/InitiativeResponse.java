package com.example.LocalZero.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for returning initiative information to the client.
 * This class controls exactly which information leaves the system,
 * protecting sensitive data and providing a clean API contract.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InitiativeResponse {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String duration;
    private String category;
    private String visibility;
    private int participantCount;
}