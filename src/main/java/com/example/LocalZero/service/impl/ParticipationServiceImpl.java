package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.Comment;
import com.example.LocalZero.Model.Initiative;
import com.example.LocalZero.Model.LikeEntity;
import com.example.LocalZero.Model.Update;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.CommentCreateRequest;
import com.example.LocalZero.dto.CommentResponse;
import com.example.LocalZero.dto.LikeResponse;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.CommentRepository;
import com.example.LocalZero.repository.InitiativeRepository;
import com.example.LocalZero.repository.LikeRepository;
import com.example.LocalZero.repository.UpdateRepository;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.IParticipationService;
import com.example.LocalZero.service.notification.event.UpdateCommentedEvent;
import com.example.LocalZero.service.notification.event.UpdateLikedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class ParticipationServiceImpl implements IParticipationService {

    private final UpdateRepository updateRepository;
    private final UserRepository userRepository;
    private final InitiativeRepository initiativeRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ParticipationServiceImpl(UpdateRepository updateRepository,
                                    UserRepository userRepository,
                                    InitiativeRepository initiativeRepository,
                                    CommentRepository commentRepository,
                                    LikeRepository likeRepository,
                                    ApplicationEventPublisher eventPublisher) {
        this.updateRepository = updateRepository;
        this.userRepository = userRepository;
        this.initiativeRepository = initiativeRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CommentResponse addComment(Long updateId, CommentCreateRequest request, String userEmail) {
        Update update = updateRepository.findById(updateId)
                .orElseThrow(() -> new ResourceNotFoundException("Update not found with id: " + updateId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        requireInitiativeMember(updateId, user);

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(user);
        comment.setUpdate(update);

        Comment saved = commentRepository.save(comment);

        Long initiativeId = updateRepository.findInitiativeIdByUpdateId(updateId);
        eventPublisher.publishEvent(new UpdateCommentedEvent(
                initiativeId, user.getEmail(), user.getName()));

        return new CommentResponse(saved.getId(), saved.getContent(), user.getName(), saved.getCreatedAt());
    }

    @Override
    public List<CommentResponse> getComments(Long updateId) {
        Update update = updateRepository.findById(updateId)
            .orElseThrow(() -> new ResourceNotFoundException("Update not found with id: " + updateId));
        List<Comment> comments = commentRepository.findByUpdateIdOrderByCreatedAtAsc(updateId);
        return comments.stream()
            .map(c -> new CommentResponse(c.getId(), c.getContent(), c.getAuthor().getName(), c.getCreatedAt()))
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public LikeResponse toggleLike(Long updateId, String userEmail) {
        Update update = updateRepository.findById(updateId)
                .orElseThrow(() -> new ResourceNotFoundException("Update not found with id: " + updateId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        requireInitiativeMember(updateId, user);

        var existing = likeRepository.findByUpdateIdAndUserId(updateId, user.getId());
        boolean liked;
        if (existing.isPresent()) {
            likeRepository.deleteByUpdateIdAndUserId(updateId, user.getId());
            liked = false;
        } else {
            LikeEntity like = new LikeEntity(user, update);
            try {
                likeRepository.save(like);
                liked = true;
                String targetEmail = update.getAuthor().getEmail();
                if (!targetEmail.equals(userEmail)) {
                    Long initiativeId = updateRepository.findInitiativeIdByUpdateId(updateId);
                    eventPublisher.publishEvent(new UpdateLikedEvent(
                            initiativeId, targetEmail, user.getName()));
                }
            } catch (DataIntegrityViolationException ex) {
                // Another concurrent request inserted the same (user_id, update_id) unique row.
                // Treat as already liked: re-read state and set liked=true.
                liked = likeRepository.findByUpdateIdAndUserId(updateId, user.getId()).isPresent();
            }
        }

        long count = likeRepository.countByUpdateId(updateId);
        return new LikeResponse(count, liked);
    }

    @Override
    public LikeResponse getLikeInfo(Long updateId, String userEmail) {
        Update update = updateRepository.findById(updateId)
                .orElseThrow(() -> new ResourceNotFoundException("Update not found with id: " + updateId));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + userEmail));

        long count = likeRepository.countByUpdateId(updateId);
        boolean liked = likeRepository.findByUpdateIdAndUserId(updateId, user.getId()).isPresent();
        return new LikeResponse(count, liked);
    }

    private void requireInitiativeMember(Long updateId, User user) {
        Long initiativeId = updateRepository.findInitiativeIdByUpdateId(updateId);
        if (initiativeId == null) {
            throw new ResourceNotFoundException("Update not found with id: " + updateId);
        }

        Initiative initiative = initiativeRepository.findByIdWithParticipants(initiativeId)
                .orElseThrow(() -> new ResourceNotFoundException("Initiative not found for update: " + updateId));

        boolean isCreator = initiative.getCreator().getEmail().equals(user.getEmail());
        if (!isCreator && !initiative.getParticipants().contains(user)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You must join this initiative before interacting with updates.");
        }
    }
}
