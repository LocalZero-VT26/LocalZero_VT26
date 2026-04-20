package com.example.LocalZero.service;

import com.example.LocalZero.dto.UpdateProfileRequest;
import com.example.LocalZero.dto.UserResponse;
import com.example.LocalZero.model.User;
import com.example.LocalZero.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ResidentServiceImpl extends AbstractUserService implements IResidentService {

    public ResidentServiceImpl(UserRepository userRepository) {
        super(userRepository);
    }

    @Override
    public synchronized UserResponse updateProfile(Long id, UpdateProfileRequest request, String callerEmail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found: " + id));

        if (!user.getEmail().equals(callerEmail)) {
            throw new SecurityException("Residents can only update their own profile");
        }

        user.setName(request.getName());
        user.setLocation(request.getLocation());
        return new UserResponse(userRepository.save(user));
    }
}
