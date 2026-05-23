package com.example.LocalZero.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeResponse {
    private int count;
    private boolean likedByCurrentUser;
}
