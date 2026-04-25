package com.example.LocalZero.service.impl;

import com.example.LocalZero.dto.InitiativeRequest;
import com.example.LocalZero.dto.InitiativeResponse;
import com.example.LocalZero.service.IInitiativeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
 * The class that actually does the work for initiative,
 * it takes requests and makes it into an entity that the
 * database understands, saves it in the repository and
 * sends back the response.
 */

@Service
@RequiredArgsConstructor
/**
 * The "@RequiredArgsConstructor" above generates a
 * constructor with all fields marked with "final"
 * or "@NonNull".
 */
public class InitiatieServiceImpl implements IInitiativeService {

    @Override
    public InitiativeResponse createInitiative(InitiativeRequest request) {
        return null;
    }

    @Override
    public List<InitiativeResponse> getAllInitiatives() {
        return List.of();
    }
}
