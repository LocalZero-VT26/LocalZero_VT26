package com.example.LocalZero.controller;

import com.example.LocalZero.Model.Initiative;
import com.example.LocalZero.Model.Update;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.repository.InitiativeRepository;
import com.example.LocalZero.repository.UpdateRepository;
import com.example.LocalZero.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/initiatives")
public class InitiativeController {

    private final InitiativeRepository initiativeRepository;
    private final UpdateRepository updateRepository;
    private final UserRepository userRepository;

    public InitiativeController(InitiativeRepository initiativeRepository, UpdateRepository updateRepository, UserRepository userRepository) {
        this.initiativeRepository = initiativeRepository;
        this.updateRepository = updateRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Initiative> getAll() {
        return initiativeRepository.findAll();
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<String> join(@PathVariable Long id, @RequestParam Long userId) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!initiative.getParticipants().contains(user)) {
            initiative.getParticipants().add(user);
            initiativeRepository.save(initiative);
            return ResponseEntity.ok("You have now entered the initiative!");
        }
        return ResponseEntity.badRequest().body("You are already a part of this initiative.");
    }

    @PostMapping("/{id}/updates")
    public ResponseEntity<Update> postUpdate(@PathVariable Long id,
                                             @RequestBody Update update,
                                             @RequestParam Long userId) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        update.setInitiative(initiative);
        update.setAuthor(user);

        return ResponseEntity.ok(updateRepository.save(update));
    }
}
