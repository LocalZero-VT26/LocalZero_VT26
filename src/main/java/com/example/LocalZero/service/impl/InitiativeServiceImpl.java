package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.Initiative;
import com.example.LocalZero.dto.InitiativeRequest;
import com.example.LocalZero.dto.InitiativeResponse;
import com.example.LocalZero.repository.InitiativeRepository;
import com.example.LocalZero.service.IInitiativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation that handles the business logic for initiatives.
 * It transforms requests into database entities, persists them,
 * and maps the results back to response objects.
 */


@Service
@RequiredArgsConstructor
public class InitiativeServiceImpl implements IInitiativeService {

    private final InitiativeRepository initiativeRepository;

    /**
     * Creates a new Initiative based on the provided request data and saves it to the database.
     * @param request the data for the new initiative.
     * @return an {@link InitiativeResponse} containing the saved initiative's data.
     */
    @Override
    public InitiativeResponse createInitiative(InitiativeRequest request) {
        Initiative initiative = new Initiative(
                request.getTitle(),
                request.getDescription(),
                request.getLocation(),
                request.getDuration(),
                request.getCategory()
        );

        Initiative savedInitiative = initiativeRepository.save(initiative);
        return mapToResponse(savedInitiative);
    }

    /**
     * Gets all initiatives from the database and transforms them into a list of responses.
     * @return a list of all {@link InitiativeResponse} objects.
     */
    @Override
    public List<InitiativeResponse> getAllInitiatives() {
        return initiativeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Helper method to map an {@link Initiative} entity to an {@link InitiativeResponse} DTO.
     * @param initiative the entity to map.
     * @return a DTO representing the initiative.
     */
    private InitiativeResponse mapToResponse(Initiative initiative) {
        return new InitiativeResponse(
                initiative.getId(),
                initiative.getTitle(),
                initiative.getDescription(),
                initiative.getLocation(),
                initiative.getDuration(),
                initiative.getCategory()
        );
    }
}