package com.example.LocalZero.service.notification.event;

public record UpdateCommentedEvent(
        Long initiativeId,
        String commenterEmail,
        String commenterName
) {}
