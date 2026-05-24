package com.example.LocalZero.service;

import com.example.LocalZero.dto.CommentCreateRequest;
import com.example.LocalZero.dto.CommentResponse;
import com.example.LocalZero.dto.LikeResponse;

import java.util.List;

public interface IParticipationService {
    CommentResponse addComment(Long updateId, CommentCreateRequest request, String userEmail);
    List<CommentResponse> getComments(Long updateId);
    LikeResponse toggleLike(Long updateId, String userEmail);
    LikeResponse getLikeInfo(Long updateId, String userEmail);
}
