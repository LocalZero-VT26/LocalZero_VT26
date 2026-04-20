package com.example.LocalZero.service;

import com.example.LocalZero.dto.AssignRoleRequest;
import com.example.LocalZero.dto.RegisterRequest;
import com.example.LocalZero.dto.UserResponse;
import com.example.LocalZero.model.Role;
import com.example.LocalZero.model.User;
import com.example.LocalZero.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OrganizerServiceImpl extends AbstractUserService implements IOrganizerService {

    public OrganizerServiceImpl(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public synchronized UserResponse updateProfile(Long id,
                                                   RegisterRequest request,
                                                   String callerEmail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        if (!user.getEmail().equals(callerEmail)) {
            throw new SecurityException("You can only update your own profile");
        }

        user.setName(request.getName());
        user.setLocation(request.getLocation());
        return new UserResponse(userRepository.save(user));
    }

    @Override
    public synchronized void assignRole(AssignRoleRequest request, String callerEmail) {
        User caller = userRepository.findByEmail(callerEmail)
                .orElseThrow(() -> new RuntimeException("Caller not found"));

        if (!caller.getRoles().contains(Role.ORGANIZER)) {
            throw new SecurityException("Only organizers can assign roles");
        }

        User target = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        target.getRoles().add(request.getRole());
        userRepository.save(target);
    }
}
