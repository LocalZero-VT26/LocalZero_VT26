package com.example.LocalZero.service;

import com.example.LocalZero.dto.*;
import java.util.List;

/**
 * Service interface that defines the contract for initiative-related business logic.
 * Handles operations such as creating, retrieving, and managing initiatives and their updates.
 */
public interface IInitiativeService {
    List<InitiativeResponse> getAllInitiatives();
    InitiativeResponse createInitiative(InitiativeCreateRequest request, String userEmail);
    void joinInitiative(Long initiativeId, String userEmail);
    UpdateResponse postUpdate(Long initiativeId, UpdateCreateRequest request, String userEmail);
}