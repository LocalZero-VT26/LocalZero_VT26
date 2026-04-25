package com.example.LocalZero.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

/**
 * With this class, we can control exactly which information that leaves the system.
 */

@Getter
@AllArgsConstructor
public class InitiativeResponse {

    private Long id;
    private String title;
    private String description;
    private String location;
    private String duration;
    private String Category;

}
