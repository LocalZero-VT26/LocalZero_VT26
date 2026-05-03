package com.example.LocalZero.service.joinInitiative;

import com.example.LocalZero.Model.Initiative;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.InitiativeRepository;
import com.example.LocalZero.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service bean that performs the action of joining a user to an initiative.
 * Implemented separately so the operation can be reused from higher-level
 * services or controllers if needed.
 */
@Service
@RequiredArgsConstructor
public class Join {

	private final InitiativeRepository initiativeRepository;
	private final UserRepository userRepository;

	@Transactional
	public void joinInitiative(Long initiativeId, String userEmail) {
		Initiative initiative = initiativeRepository.findById(initiativeId)
				.orElseThrow(() -> new ResourceNotFoundException("Initiative not found with id: " + initiativeId));
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

		initiative.getParticipants().add(user);
		initiativeRepository.save(initiative);
	}
}
