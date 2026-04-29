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
    public ResponseEntity<InitiativeResponse> create(@Valid @RequestBody InitiativeCreateRequest request, Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(initiativeService.createInitiative(request, principal.getName()));
    }

    @PostMapping("/{id}/join")
    public ResponseEntity<Void> join(@PathVariable Long id, Principal principal) {
        initiativeService.joinInitiative(id, principal.getName());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/updates")
    @PreAuthorize("hasRole('ORGANIZER')")
    public ResponseEntity<UpdateResponse> postUpdate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCreateRequest request,
            Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(initiativeService.postUpdate(id, request, principal.getName()));
    }
}