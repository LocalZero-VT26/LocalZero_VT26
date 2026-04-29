package com.example.LocalZero.service;

import com.example.LocalZero.dto.*;
import java.util.List;

public interface IInitiativeService {
    List<InitiativeResponse> getAllInitiatives();
    InitiativeResponse createInitiative(InitiativeCreateRequest request, String userEmail);
    void joinInitiative(Long initiativeId, String userEmail);
    UpdateResponse postUpdate(Long initiativeId, UpdateCreateRequest request, String userEmail);
}