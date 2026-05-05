package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.Role;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.UserSummaryResponse;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;


    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserSummaryResponse> getAvailableUsers(String userEmail) {

        User currentUser = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        boolean isOrganizer = currentUser.getRoles().contains(Role.ORGANIZER);

        List<User> users;

        if (isOrganizer) {
            List<User> userInLocation = userRepository.findByLocation(currentUser.getLocation());
            List<User> allOrganizers = userRepository.findByRole(Role.ORGANIZER);

            users = new ArrayList<>(userInLocation);
            for (User organizer : allOrganizers) {
                if (!users.contains(organizer)) {
                    users.add(organizer);
                }
            }
        } else {
            users = userRepository.findByLocation(currentUser.getLocation());
        }

        List<UserSummaryResponse> result = new ArrayList<>();

        for (User user : users) {
            if (!currentUser.getEmail().equals(user.getEmail())) {
                result.add(new UserSummaryResponse(
                        user.getId(),
                        user.getName(),
                        user.getLocation(),
                        user.getRoles(),
                        user.getEmail()
                ));
            }
        }

        return result;
    }

}
