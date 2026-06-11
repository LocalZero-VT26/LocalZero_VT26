package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.*;
import com.example.LocalZero.repository.*;
import com.example.LocalZero.service.IInitiativeService;
import com.example.LocalZero.dto.*;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.service.joinInitiative.Join;
import com.example.LocalZero.service.joinInitiative.Leave;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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

    private static final int UPDATES_FEED_LIMIT = 20;

    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;
    private final UpdateRepository updateRepository;
    private final Join joinService;
    private final Leave leaveService;

    @Override
    @Transactional(readOnly = true)
    public List<InitiativeResponse> getAllInitiatives(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        String userLocation = user.getLocation() != null ? user.getLocation() : "";
        List<Initiative> initiatives = initiativeRepository.findVisibleForUser(userEmail, userLocation);
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

        Set<Long> joinedIds = new HashSet<>(initiativeRepository.findIdsJoinedByUser(userEmail));

        return initiatives.stream()
                .map(initiative -> mapToInitiativeResponse(
                        initiative,
                        participantCounts.getOrDefault(initiative.getId(), 0),
                        joinedIds.contains(initiative.getId())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public InitiativeResponse getInitiativeById(Long initiativeId, String userEmail) {
        Initiative initiative = initiativeRepository.findById(initiativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Initiative not found with id: " + initiativeId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        requireVisibleTo(initiative, user);

        boolean joined = initiative.getParticipants().contains(user);
        return mapToInitiativeResponse(initiative, initiative.getParticipants().size(), joined);
    }

    @Override
    @Transactional
    public InitiativeResponse createInitiative(InitiativeCreateRequest request, String userEmail) {
        User creator = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        Initiative initiative = new Initiative(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getDuration(),
                request.getCategory(),
                request.getVisibility(),
                creator
        );

        // The creator takes part in their own initiative from the start,
        // so they can post updates and show up in the participant count.
        initiative.getParticipants().add(creator);

        return mapToInitiativeResponse(initiativeRepository.save(initiative), 1, true);
    }

    @Override
    @Transactional
    public void joinInitiative(Long initiativeId, String userEmail) {
        joinService.joinInitiative(initiativeId, userEmail);
    }

    @Override
    @Transactional
    public void leaveInitiative(Long initiativeId, String userEmail) {
        leaveService.leaveInitiative(initiativeId, userEmail);
    }

    @Override
    @Transactional
    public UpdateResponse postUpdate(Long initiativeId, UpdateCreateRequest request, String userEmail) {
        Initiative initiative = initiativeRepository.findById(initiativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Initiative not found with id: " + initiativeId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        boolean isCreator = initiative.getCreator().getEmail().equals(userEmail);
        if (!isCreator && !initiative.getParticipants().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You must join this initiative before posting updates.");
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

    @Override
    @Transactional(readOnly = true)
    public List<UpdateResponse> getAllUpdates(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        String userLocation = user.getLocation() != null ? user.getLocation() : "";

        PageRequest pageRequest = PageRequest.of(
                0, UPDATES_FEED_LIMIT, Sort.by(Sort.Direction.DESC, "createdAt"));

        return updateRepository.findVisibleForUser(userEmail, userLocation, pageRequest)
                .stream()
                .map(update -> new UpdateResponse(
                        update.getId(),
                        update.getContent(),
                        update.getImageUrl(),
                        update.getAuthor().getName(),
                        update.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UpdateResponse> getUpdatesForInitiative(Long initiativeId, String userEmail) {
        Initiative initiative = initiativeRepository.findById(initiativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Initiative not found with id: " + initiativeId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        requireVisibleTo(initiative, user);

        return updateRepository.findByInitiativeIdOrderByCreatedAtDesc(initiativeId)
                .stream()
                .map(update -> new UpdateResponse(
                        update.getId(),
                        update.getContent(),
                        update.getImageUrl(),
                        update.getAuthor().getName(),
                        update.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Applies the same visibility rules as the repository queries:
     * public initiatives are open to everyone, otherwise the user must be
     * the creator, a participant, or live in the initiative's neighborhood.
     */
    private void requireVisibleTo(Initiative initiative, User user) {
        String visibility = initiative.getVisibility() != null ? initiative.getVisibility().toLowerCase() : "";
        if (visibility.equals("public")) {
            return;
        }
        if (initiative.getCreator().getEmail().equals(user.getEmail())
                || initiative.getParticipants().contains(user)) {
            return;
        }
        boolean isNeighborhoodSpecific = visibility.equals("neighborhood-specific") || visibility.equals("neighborhood");
        String userLocation = user.getLocation() != null ? user.getLocation() : "";
        String initiativeLocation = initiative.getLocation() != null ? initiative.getLocation() : "";
        if (isNeighborhoodSpecific && userLocation.equalsIgnoreCase(initiativeLocation)) {
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You do not have access to this initiative.");
    }

    private InitiativeResponse mapToInitiativeResponse(Initiative initiative, int participantCount, boolean joinedByCurrentUser) {
        InitiativeResponse response = new InitiativeResponse();
        response.setId(initiative.getId());
        response.setTitle(initiative.getTitle());
        response.setDescription(initiative.getDescription());
        response.setLocation(initiative.getLocation());
        response.setDuration(initiative.getDuration());
        response.setCategory(initiative.getCategory());
        response.setVisibility(initiative.getVisibility());
        response.setParticipantCount(participantCount);
        response.setJoinedByCurrentUser(joinedByCurrentUser);
        return response;
    }
}