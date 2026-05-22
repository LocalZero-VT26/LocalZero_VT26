package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.EcoActionRepository;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.ISustainabilityService;
import com.example.LocalZero.service.sustainability.ContentValidator;
import com.example.LocalZero.service.sustainability.IEcoCommand;
import com.example.LocalZero.service.sustainability.LogEcoActionCommand;
import com.example.LocalZero.service.sustainability.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SustainabilityServiceImpl implements ISustainabilityService {

    private final UserRepository userRepository;
    private final EcoActionRepository ecoActionRepository;


    @Override
    public void logEcoAction(String description, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        UserValidator userValidator = new UserValidator();
        ContentValidator contentValidator = new ContentValidator();

        userValidator.setNext(contentValidator);

        userValidator.validate(description, user);

        IEcoCommand logCommand = new LogEcoActionCommand(description, user, ecoActionRepository);
        logCommand.execute();
    }
}
