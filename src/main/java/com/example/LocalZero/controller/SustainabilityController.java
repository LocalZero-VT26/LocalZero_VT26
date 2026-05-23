package com.example.LocalZero.controller;


import com.example.LocalZero.dto.EcoActionRequest;
import com.example.LocalZero.service.ISustainabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/sustainability")
@RequiredArgsConstructor
public class SustainabilityController {

    private final ISustainabilityService sustainabilityService;

    @PostMapping("/log")
    public ResponseEntity<String> logEcoAction(
            @RequestBody EcoActionRequest request,
            @RequestAttribute("email") String userEmail
    ) {
        sustainabilityService.logEcoAction(request.getDescription(), userEmail);

        return ResponseEntity.ok("Eco-action logged successfully!");
    }
}
