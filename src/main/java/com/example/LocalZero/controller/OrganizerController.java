package com.example.LocalZero.controller;

import com.example.LocalZero.dto.AssignRoleRequest;
import com.example.LocalZero.dto.UpdateProfileRequest;
import com.example.LocalZero.dto.UserResponse;
import com.example.LocalZero.service.IOrganizerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organizers")
public class OrganizerController {

    private final IOrganizerService organizerService;

    public OrganizerController(IOrganizerService organizerService) {
        this.organizerService = organizerService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(organizerService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(organizerService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateProfileRequest request) {
        String callerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(organizerService.updateProfile(id, request, callerEmail));
    }

    @PostMapping("/assign-role")
    public ResponseEntity<Void> assignRole(@Valid @RequestBody AssignRoleRequest request) {
        String callerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        organizerService.assignRole(request, callerEmail);
        return ResponseEntity.noContent().build();
    }
}
