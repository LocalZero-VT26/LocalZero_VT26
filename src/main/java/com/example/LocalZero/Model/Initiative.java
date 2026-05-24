package com.example.LocalZero.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entity representing an initiative in the system.
 * Manages initiative data including title, description, participants,
 * and associated updates. Each initiative has a creator and can be joined by multiple users.
 */
@Entity
@Table(name = "initiatives")
@Getter
@Setter
@NoArgsConstructor
public class Initiative {

    public Initiative(String title, String description, String location,
                      String duration, String category, String visibility, User creator) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.duration = duration;
        this.category = category;
        this.visibility = visibility;
        this.creator = creator;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Column(nullable = false, length = 1000)
    private String description;

    @NotBlank(message = "Location cannot be blank")
    @Column(nullable = false)
    private String location;

    @NotBlank(message = "Duration cannot be blank")
    @Column(nullable = false)
    private String duration;

    @NotBlank(message = "Category cannot be blank")
    @Column(nullable = false)
    private String category;

    @NotBlank(message = "Visibility cannot be blank")
    @Column(nullable = false)
    private String visibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @ManyToMany
    @JoinTable(
            name = "initiative_participants",
            joinColumns = @JoinColumn(name = "initiative_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private Set<User> participants = new HashSet<>();

    @OneToMany(mappedBy = "initiative", cascade = CascadeType.ALL)
    private List<Update> updates = new ArrayList<>();

}