package com.example.LocalZero.service.sustainability.dashboard;

import com.example.LocalZero.Model.EcoAction;
import com.example.LocalZero.Model.Initiative;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.CommunityStatResponse;
import com.example.LocalZero.repository.EcoActionRepository;
import com.example.LocalZero.repository.InitiativeRepository;
import com.example.LocalZero.repository.UserRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CommunityDashboardGenerator extends AbstractDashboardGenerator<CommunityStatResponse> {

    private final InitiativeRepository initiativeRepository;
    private final EcoActionRepository ecoActionRepository;

    public CommunityDashboardGenerator(UserRepository userRepository, 
                                       InitiativeRepository initiativeRepository, 
                                       EcoActionRepository ecoActionRepository) {
        super(userRepository);
        this.initiativeRepository = initiativeRepository;
        this.ecoActionRepository = ecoActionRepository;
    }

    @Override
    protected List<CommunityStatResponse> fetchStats(User currentUser) {
        List<Initiative> initiatives = initiativeRepository.findInitiativesByUser(currentUser);
        
        Set<User> communityUsers = new HashSet<>();
        for (Initiative init : initiatives) {
            communityUsers.add(init.getCreator());
            communityUsers.addAll(init.getParticipants());
        }
        
        communityUsers.remove(currentUser);

        List<EcoAction> actions = ecoActionRepository.findAll().stream()
                .filter(action -> communityUsers.contains(action.getUser()))
                .sorted((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()))
                .collect(Collectors.toList());
                
        return actions.stream()
                .map(a -> new CommunityStatResponse(a.getUser().getName(), a.getDescription(), a.getTimestamp()))
                .collect(Collectors.toList());
    }
}
