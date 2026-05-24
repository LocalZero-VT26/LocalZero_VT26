package com.example.LocalZero.service.sustainability.dashboard;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.exception.ResourceNotFoundException;

import java.util.List;

public abstract class AbstractDashboardGenerator<T> {
    
    protected final UserRepository userRepository;
    
    public AbstractDashboardGenerator(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public final List<T> generateDashboard(String userEmail) {
        User user = getUser(userEmail);
        return fetchStats(user);
    }
    
    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
    }
    
    protected abstract List<T> fetchStats(User user);
}
