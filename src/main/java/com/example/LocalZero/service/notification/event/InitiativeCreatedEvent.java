package com.example.LocalZero.service.notification.event;

/**
 * Domain event published when a new initiative is created.
 * Consumed by listeners (Observer pattern) that notify users
 * living in the initiative's neighborhood.
 */
public record InitiativeCreatedEvent(Long initiativeId, String title, String location, String creatorEmail) {
}
