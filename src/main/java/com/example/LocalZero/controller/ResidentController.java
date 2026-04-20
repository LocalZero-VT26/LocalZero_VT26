package com.example.LocalZero.controller;

import com.example.LocalZero.dto.UpdateProfileRequest;
import com.example.LocalZero.dto.UserResponse;
import com.example.LocalZero.service.IResidentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/residents")
public class ResidentController {

    private final IResidentService residentService;

    public ResidentController(IResidentService residentService) {
        this.residentService = residentService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getResident(@PathVariable Long id) {
        return ResponseEntity.ok(residentService.getUserById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateProfile(@PathVariable Long id,
                                                      @Valid @RequestBody UpdateProfileRequest request) {
        String callerEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(residentService.updateProfile(id, request, callerEmail));
    }
}
