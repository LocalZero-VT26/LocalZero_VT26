package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.CommunityStatResponse;
import com.example.LocalZero.dto.EcoActionResponse;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.EcoActionRepository;
import com.example.LocalZero.repository.InitiativeRepository;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.ISustainabilityService;
import com.example.LocalZero.service.sustainability.ContentValidator;
import com.example.LocalZero.service.sustainability.IEcoCommand;
import com.example.LocalZero.service.sustainability.LogEcoActionCommand;
import com.example.LocalZero.service.sustainability.UserValidator;
import com.example.LocalZero.service.sustainability.dashboard.CommunityDashboardGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SustainabilityServiceImpl implements ISustainabilityService {

    private final UserRepository userRepository;
    private final EcoActionRepository ecoActionRepository;
    private final InitiativeRepository initiativeRepository;

    /**
     * This is where the UserValidator CoR-chain is initiated, which will validate
     * the user and the content of the eco action description before executing
     * the LogEcoActionCommand.
     * Line 51 is where we set off the command pattern!
     */

    @Override
    @Transactional
    public void logEcoAction(String description, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElse(null);

        UserValidator userValidator = new UserValidator();
        ContentValidator contentValidator = new ContentValidator();

        userValidator.setNext(contentValidator);

        userValidator.validate(description, user);

        IEcoCommand logCommand = new LogEcoActionCommand(description, user, ecoActionRepository);
        logCommand.execute();
    }

    /**
     * This is where we check if the user has 9 or less EcoActions to generate mock eco-actions if needed.
     */

    @Override
    @Transactional
    public List<EcoActionResponse> getEcoActionsHistory(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));
        
        List<com.example.LocalZero.Model.EcoAction> actions = ecoActionRepository.findByUserOrderByTimestampDesc(user);
        if (actions.size() <= 9) {
            com.example.LocalZero.service.sustainability.IEcoCommand generateMockCommand = 
                new com.example.LocalZero.service.sustainability.GenerateMockEcoActionsCommand(user, ecoActionRepository);
            generateMockCommand.execute();
            actions = ecoActionRepository.findByUserOrderByTimestampDesc(user);
        }
        
        return actions.stream()
                .map(action -> new EcoActionResponse(action.getDescription(), action.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommunityStatResponse> getCommunityEcoActions(String userEmail) {
        CommunityDashboardGenerator generator = new CommunityDashboardGenerator(userRepository, initiativeRepository, ecoActionRepository);
        return generator.generateDashboard(userEmail);
    }
}
