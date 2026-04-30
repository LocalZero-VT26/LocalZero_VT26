package com.example.LocalZero.controller;

import com.example.LocalZero.dto.*;
import com.example.LocalZero.service.IInitiativeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/initiatives")
@RequiredArgsConstructor //
public class InitiativeController {

    private final IInitiativeService initiativeService;

    @GetMapping
    public List<InitiativeResponse> getAll() {
        return initiativeService.getAllInitiatives();
    }

    @PostMapping
    public ResponseEntity<InitiativeResponse> create(@Valid @RequestBody InitiativeCreateRequest request,
                                                     @RequestAttribute("email") String email) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(initiativeService.createInitiative(request, email));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> join(@PathVariable Long id,
                                     @RequestAttribute("email") String email) {
        initiativeService.joinInitiative(id, email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/updates")
    public ResponseEntity<UpdateResponse> postUpdate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCreateRequest request,
            @RequestAttribute("email") String email) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(initiativeService.postUpdate(id, request, email));
    }
}