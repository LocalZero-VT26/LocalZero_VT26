package com.example.LocalZero.service.joinInitiative;

import com.example.LocalZero.Model.Initiative;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.InitiativeRepository;
import com.example.LocalZero.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

/**
 * Service bean that removes a user from an initiative's participant list.
 * Mirrors the Join service so that joining and leaving follow the same
 * structure and can be reused independently.
 */
@Service
@RequiredArgsConstructor
public class Leave {

	private final InitiativeRepository initiativeRepository;
	private final UserRepository userRepository;

	@Transactional
	public void leaveInitiative(Long initiativeId, String userEmail) {
		Initiative initiative = initiativeRepository.findById(initiativeId)
				.orElseThrow(() -> new ResourceNotFoundException("Initiative not found with id: " + initiativeId));
		User user = userRepository.findByEmail(userEmail)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

		if (!initiative.getParticipants().contains(user)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not a participant of this initiative!");
		}

		initiative.getParticipants().remove(user);
		initiativeRepository.save(initiative);
	}
}
