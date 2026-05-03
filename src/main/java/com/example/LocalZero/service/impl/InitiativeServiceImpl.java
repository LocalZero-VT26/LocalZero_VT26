package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.*;
import com.example.LocalZero.repository.*;
import com.example.LocalZero.service.IInitiativeService;
import com.example.LocalZero.dto.*;
import com.example.LocalZero.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service implementation that handles the business logic for initiatives.
 * Transforms requests into database entities, persists them,
 * and maps results back to response objects. Manages initiative lifecycle
 * including creation, participation, and updates.
 */
@Service
@RequiredArgsConstructor
public class InitiativeServiceImpl implements IInitiativeService {

    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;
    private final UpdateRepository updateRepository;

    @Override
    public List<InitiativeResponse> getAllInitiatives() {
        List<Initiative> initiatives = initiativeRepository.findAll();
        if (initiatives.isEmpty()) {
            return List.of();
        }

        List<Long> initiativeIds = initiatives.stream()
                .map(Initiative::getId)
                .toList();

        Map<Long, Integer> participantCounts = new HashMap<>();
        for (Object[] row : initiativeRepository.countParticipantsByInitiativeIds(initiativeIds)) {
            Long initiativeId = (Long) row[0];
            Integer count = ((Long) row[1]).intValue();
            participantCounts.put(initiativeId, count);
        }

        return initiatives.stream()
                .map(initiative -> mapToInitiativeResponse(initiative, participantCounts.getOrDefault(initiative.getId(), 0)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InitiativeResponse createInitiative(InitiativeCreateRequest request, String userEmail) {
        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found:"));

        Initiative initiative = new Initiative(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getDuration(),
                request.getCategory(),
                request.getVisibility(),
                creator
        );

        return mapToInitiativeResponse(initiativeRepository.save(initiative), 0);
    }

    @Override
    @Transactional
    public void joinInitiative(Long initiativeId, String userEmail) {
        Initiative initiative = initiativeRepository.findById(initiativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Initiative not found:"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found:"));

        initiative.getParticipants().add(user);
        initiativeRepository.save(initiative);
    }

    @Override
    @Transactional
    public UpdateResponse postUpdate(Long initiativeId, UpdateCreateRequest request, String userEmail) {
        Initiative initiative = initiativeRepository.findById(initiativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Initiative not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found:"));

        if (!user.getRoles().contains(Role.ORGANIZER)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only organizers can post updates");
        }

        Update update = new Update();
        update.setContent(request.getContent());
        update.setImageUrl(request.getImageUrl());
        update.setInitiative(initiative);
        update.setAuthor(user);

        Update savedUpdate = updateRepository.save(update);
        return new UpdateResponse(savedUpdate.getId(), savedUpdate.getContent(),
                savedUpdate.getImageUrl(), user.getName(), savedUpdate.getCreatedAt());
    }

    private InitiativeResponse mapToInitiativeResponse(Initiative initiative, int participantCount) {
        InitiativeResponse response = new InitiativeResponse();
        response.setId(initiative.getId());
        response.setTitle(initiative.getTitle());
        response.setDescription(initiative.getDescription());
        response.setLocation(initiative.getLocation());
        response.setDuration(initiative.getDuration());
        response.setCategory(initiative.getCategory());
        response.setVisibility(initiative.getVisibility());
        response.setParticipantCount(participantCount);
        return response;
    }
}