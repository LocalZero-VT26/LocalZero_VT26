package com.example.LocalZero.controller;


import com.example.LocalZero.dto.CommunityStatResponse;
import com.example.LocalZero.dto.EcoActionRequest;
import com.example.LocalZero.dto.EcoActionResponse;
import com.example.LocalZero.service.ISustainabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/history")
    public ResponseEntity<List<EcoActionResponse>> getHistory(
            @RequestAttribute("email") String userEmail
    ) {
        List<EcoActionResponse> history = sustainabilityService.getEcoActionsHistory(userEmail);
        return ResponseEntity.ok(history);
    }

    @GetMapping("/dashboard/community")
    public ResponseEntity<List<CommunityStatResponse>> getCommunityStats(
            @RequestAttribute("email") String userEmail
    ) {
        List<CommunityStatResponse> stats = sustainabilityService.getCommunityEcoActions(userEmail);
        return ResponseEntity.ok(stats);
    }
}
