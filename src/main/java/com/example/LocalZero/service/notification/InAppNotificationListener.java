package com.example.LocalZero.service.notification;

import com.example.LocalZero.Model.Initiative;
import com.example.LocalZero.Model.Notification;
import com.example.LocalZero.Model.NotificationType;
import com.example.LocalZero.repository.InitiativeRepository;
import com.example.LocalZero.repository.NotificationRepository;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.notification.event.InitiativeCreatedEvent;
import com.example.LocalZero.service.notification.event.NewChatMessageEvent;
import com.example.LocalZero.service.notification.event.UpdateCommentedEvent;
import com.example.LocalZero.service.notification.event.UpdateLikedEvent;
import com.example.LocalZero.service.notification.event.UpdatePostedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Observer that listens for domain events and persists in-app notifications.
 * Decouples initiative/messaging logic from notification logic:
 * publishers only announce what happened, this listener decides who gets notified.
 */
@Component
@RequiredArgsConstructor
public class InAppNotificationListener {

    private static final String INBOX_LINK = "/inbox";

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final InitiativeRepository initiativeRepository;

    @EventListener
    public void onInitiativeCreated(InitiativeCreatedEvent event) {
        String linkTarget = "/initiatives/" + event.initiativeId();
        String title = "New initiative in " + event.location() + ": " + event.title();

        userRepository.findByLocation(event.location()).stream()
                .filter(user -> !user.getEmail().equals(event.creatorEmail()))
                .forEach(user -> notificationRepository.save(new Notification(
                        user.getEmail(), NotificationType.NEW_INITIATIVE, title, linkTarget)));
    }

    @EventListener
    public void onNewChatMessage(NewChatMessageEvent event) {
        // Don't stack multiple unread inbox notifications for the same user.
        if (notificationRepository.existsByRecipientEmailAndTypeAndLinkTargetAndReadFalse(
                event.recipientEmail(), NotificationType.NEW_MESSAGE, INBOX_LINK)) {
            return;
        }
        notificationRepository.save(new Notification(
                event.recipientEmail(), NotificationType.NEW_MESSAGE,
                "New message from " + event.senderName(), INBOX_LINK));
    }

    @EventListener
    public void onUpdatePosted(UpdatePostedEvent event) {
        notifyInitiativeMembers(
                event.initiativeId(),
                event.authorEmail(),
                NotificationType.NEW_UPDATE,
                event.authorName() + " posted a new update");
    }

    @EventListener
    public void onUpdateCommented(UpdateCommentedEvent event) {
        notifyInitiativeMembers(
                event.initiativeId(),
                event.commenterEmail(),
                NotificationType.NEW_COMMENT,
                event.commenterName() + " commented on an update");
    }

    @EventListener
    public void onUpdateLiked(UpdateLikedEvent event) {
        notificationRepository.save(new Notification(
                event.recipientEmail(), NotificationType.NEW_LIKE,
                event.likerName() + " liked your update",
                "/initiatives/" + event.initiativeId()));
    }

    private void notifyInitiativeMembers(Long initiativeId, String actorEmail,
                                         NotificationType type, String title) {
        Initiative initiative = initiativeRepository.findById(initiativeId).orElse(null);
        if (initiative == null) {
            return;
        }

        String linkTarget = "/initiatives/" + initiativeId;
        String fullTitle = title + " in " + initiative.getTitle();

        initiative.getParticipants().stream()
                .filter(user -> !user.getEmail().equals(actorEmail))
                .forEach(user -> notificationRepository.save(new Notification(
                        user.getEmail(), type, fullTitle, linkTarget)));
    }
}
