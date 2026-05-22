package com.example.LocalZero.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class EcoAction {

    @Id
    @GeneratedValue(strategy = GeneratedValue.IDENTITY)
    private Long id;

    private String description;
    private LocalDateTime timestamp;

    @ManyToOne
    private User user;

    public EcoAction(String description, User user) {
        this.description = description;
        this.user = user;
        this.timestamp = LocalDateTime.now();
    }




}
