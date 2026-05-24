package com.example.LocalZero.controller;

import com.example.LocalZero.dto.CommentCreateRequest;
import com.example.LocalZero.dto.CommentResponse;
import com.example.LocalZero.dto.LikeResponse;
import com.example.LocalZero.service.IParticipationService;
import com.example.LocalZero.service.IParticipationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/updates")
@RequiredArgsConstructor
public class UpdateParticipationController {

    private final IParticipationService participationService;

    @PostMapping("/{updateId}/comments")
    public ResponseEntity<CommentResponse> postComment(@PathVariable Long updateId,
                                                       @Valid @RequestBody CommentCreateRequest request,
                                                       @RequestAttribute("email") String email) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(participationService.addComment(updateId, request, email));
    }

    @GetMapping("/{updateId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long updateId) {
        return ResponseEntity.ok(participationService.getComments(updateId));
    }

    @PostMapping("/{updateId}/likes")
    public ResponseEntity<LikeResponse> toggleLike(@PathVariable Long updateId,
                                                   @RequestAttribute("email") String email) {
        return ResponseEntity.ok(participationService.toggleLike(updateId, email));
    }

    @GetMapping("/{updateId}/likes")
    public ResponseEntity<LikeResponse> getLikeInfo(@PathVariable Long updateId,
                                                    @RequestAttribute("email") String email) {
        return ResponseEntity.ok(participationService.getLikeInfo(updateId, email));
    }
}
