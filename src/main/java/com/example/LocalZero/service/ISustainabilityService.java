package com.example.LocalZero.service;


import com.example.LocalZero.dto.EcoActionResponse;

import java.util.List;

public interface ISustainabilityService {
    void logEcoAction(String description, String userEmail);
    List<EcoActionResponse> getEcoActionsHistory(String userEmail);
}
