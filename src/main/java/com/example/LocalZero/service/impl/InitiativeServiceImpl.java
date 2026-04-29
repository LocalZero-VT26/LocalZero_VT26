package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.*;
import com.example.LocalZero.repository.*;
import com.example.LocalZero.service.IInitiativeService;
import com.example.LocalZero.dto.*;
import com.example.LocalZero.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InitiativeServiceImpl implements IInitiativeService {

    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;
    private final UpdateRepository updateRepository;

    @Override
    public List<InitiativeResponse> getAllInitiatives() {
        return initiativeRepository.findAll().stream()
                .map(this::mapToInitiativeResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InitiativeResponse createInitiative(InitiativeCreateRequest request, String userEmail) {
        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found:"));

        Initiative initiative = new Initiative();
        initiative.setTitle(request.getTitle());
        initiative.setDescription(request.getDescription());
        initiative.setLocation(request.getLocation());
        initiative.setCategory(request.getCategory());
        initiative.setDuration(request.getDuration());
        initiative.setVisibility(request.getVisibility());

        return mapToInitiativeResponse(initiativeRepository.save(initiative));
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

        Update update = new Update();
        update.setContent(request.getContent());
        update.setImageUrl(request.getImageUrl());
        update.setInitiative(initiative);
        update.setAuthor(user);

        Update savedUpdate = updateRepository.save(update);
        return new UpdateResponse(savedUpdate.getId(), savedUpdate.getContent(),
                savedUpdate.getImageUrl(), user.getName(), savedUpdate.getCreatedAt());
    }

    private InitiativeResponse mapToInitiativeResponse(Initiative initiative) {
        InitiativeResponse res = new InitiativeResponse();
        res.setId(initiative.getId());
        res.setTitle(initiative.getTitle());
        res.setDescription(initiative.getDescription());
        res.setCategory(initiative.getCategory());
        res.setVisibility(initiative.getVisibility());
        res.setParticipantCount(initiative.getParticipants().size());
        return res;
    }
}