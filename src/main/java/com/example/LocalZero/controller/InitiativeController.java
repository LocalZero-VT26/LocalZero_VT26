package com.example.LocalZero.controller;

import com.example.LocalZero.dto.InitiativeRequest;
import com.example.LocalZero.dto.InitiativeResponse;
import com.example.LocalZero.service.IInitiativeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * Controller that handles the HTTP request that is related to initiatives.
 * Uses endpoints to create and retrieve data about the initiatives.
 */

@RestController
@RequestMapping("/initiatives")
@RequiredArgsConstructor
public class InitiativeController {

    private final IInitiativeService initiativeService;

    /**
     * Endpoint to create new initiative.
     * @param initiativeRequest request the validated data from the request body.
     * @return a ResponseEntity that contains the initiative created and HTTP status 201 (created).
     */
    @PostMapping
    public ResponseEntity<InitiativeResponse> createInitiative(@Valid @RequestBody InitiativeRequest initiativeRequest) {
        InitiativeResponse response = initiativeService.createInitiative(initiativeRequest);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve all the initiatives.
     * @return a ResponseEntity that contains a list of all initiatives and HTTP status 200 (for OK).
     */
    @PostMapping
    public ResponseEntity<List<InitiativeResponse>> getAllInitiatives() {
        List<InitiativeResponse> initiatives = initiativeService.getAllInitiatives();
        return ResponseEntity.ok(initiatives);
    }

}
