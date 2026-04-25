package com.example.LocalZero.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "initiatives")
@Getter
@Setter
@NoArgsConstructor
/*
The "@NoArgsConstructor" above automatically generates a
default-constructor for us.
 */
public class Initiative {

    public Initiative(String title, String description, String location, String duration, String category){
        this.title = title;
        this.description = description;
        this.location = location;
        this.duration = duration;
        this.category = category;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column (nullable = false)
    private String location;

    @Column (nullable = false)
    private String duration;

    @Column (nullable = false)
    private String category;
}