package com.example.LocalZero.config;

import com.example.LocalZero.Model.Initiative;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.repository.InitiativeRepository;
import com.example.LocalZero.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
public class LocationMigrationRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final InitiativeRepository initiativeRepository;

    private static final Map<String, String> CITY_TO_AREA_MAP = Map.of(
            "stockholm", "Norrmalm",
            "gothenburg", "Haga",
            "malmö", "södervärn",
            "uppsala", "Rosengården"
    );

    @Override
    public void run(String... args) throws Exception {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String currentLocation = user.getLocation();
            if (currentLocation != null) {
                String normalized = currentLocation.trim().toLowerCase();
                if (CITY_TO_AREA_MAP.containsKey(normalized)) {
                    user.setLocation(CITY_TO_AREA_MAP.get(normalized));
                    userRepository.save(user);
                }
            }
        }

        List<Initiative> initiatives = initiativeRepository.findAll();
        for (Initiative initiative : initiatives) {
            String currentLocation = initiative.getLocation();
            if (currentLocation != null) {
                String normalized = currentLocation.trim().toLowerCase();
                if (CITY_TO_AREA_MAP.containsKey(normalized)) {
                    initiative.setLocation(CITY_TO_AREA_MAP.get(normalized));
                    initiativeRepository.save(initiative);
                }
            }
        }

    }
}
