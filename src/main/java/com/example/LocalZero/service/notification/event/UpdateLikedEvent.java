package com.example.LocalZero.service.notification.event;

public record UpdateLikedEvent(
        Long initiativeId,
        String recipientEmail,
        String likerName
) {}
