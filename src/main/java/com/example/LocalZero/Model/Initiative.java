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

@Entity
@Table(name = "initiatives")
@Getter
@Setter
@NoArgsConstructor
public class Initiative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title cannot be blank")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Column(nullable = false)
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


    public Initiative(String title, String description, String location) {
        this.title = title;
        this.description = description;
        this.location = location;
    }

}