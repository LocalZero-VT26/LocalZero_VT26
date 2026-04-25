package com.example.LocalZero.service;

import com.example.LocalZero.dto.InitiativeRequest;
import com.example.LocalZero.dto.InitiativeResponse;
import java.util.List;

/**
 * An interface to help define the "contract" for what the service will be able to do.
 */

public interface IInitiativeService {

    /**
     * When InitiativeRequest has been triggered, InitiativeRespond gets returned.
     * @param request
     * @return Returns the response.
     */
    InitiativeResponse createInitiative(InitiativeRequest request);

    /**
     * A method to help list all the initiatives that are saved.
     * @return All saved initiatives.
     */
    List<InitiativeResponse> getAllInitiatives();

}