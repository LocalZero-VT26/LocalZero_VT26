package com.example.LocalZero.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "initiatives")
@Getter
@Setter
public class Initiative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private String location;

    private String duration;
    private String category;
    private String visibility;

    @ManyToMany
    @JoinTable(
            name = "initiative_participants",
            joinColumns = @JoinColumn(name = "initiative_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )

    private List<User> participants = new ArrayList<>();

    @OneToMany(mappedBy = "initiative", cascade = CascadeType.ALL)
    private List<Update> updates = new ArrayList<>();


    public Initiative(String title, String description, String location) {
        this.title = title;
        this.description = description;
        this.location = location;
    }

    public Initiative() {}

}