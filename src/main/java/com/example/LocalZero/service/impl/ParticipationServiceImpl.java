package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.Comment;
import com.example.LocalZero.Model.LikeEntity;
import com.example.LocalZero.Model.Update;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.CommentCreateRequest;
import com.example.LocalZero.dto.CommentResponse;
import com.example.LocalZero.dto.LikeResponse;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.CommentRepository;
import com.example.LocalZero.repository.LikeRepository;
import com.example.LocalZero.repository.UpdateRepository;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.IParticipationService;
import com.example.LocalZero.service.notification.event.UpdateCommentedEvent;
import com.example.LocalZero.service.notification.event.UpdateLikedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;

@Service
public class ParticipationServiceImpl implements IParticipationService {

    private final UpdateRepository updateRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;
    private final ApplicationEventPublisher eventPublisher;

    public ParticipationServiceImpl(UpdateRepository updateRepository,
                                    UserRepository userRepository,
                                    CommentRepository commentRepository,
                                    LikeRepository likeRepository,
                                    ApplicationEventPublisher eventPublisher) {
        this.updateRepository = updateRepository;
        this.userRepository = userRepository;
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

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(user);
        comment.setUpdate(update);

        Comment saved = commentRepository.save(comment);

        // Observer pattern: announce the comment so the in-app notification
        // listener can notify the update's author without coupling to it here.
        String targetEmail = update.getAuthor().getEmail();
        if (!targetEmail.equals(userEmail)) {
            eventPublisher.publishEvent(new UpdateCommentedEvent(
                    update.getInitiative().getId(), targetEmail, user.getName()));
        }

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
                    eventPublisher.publishEvent(new UpdateLikedEvent(
                            update.getInitiative().getId(), targetEmail, user.getName()));
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
}
