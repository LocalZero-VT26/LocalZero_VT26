package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.Notification;
import com.example.LocalZero.dto.NotificationResponse;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.NotificationRepository;
import com.example.LocalZero.service.INotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements INotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(String userEmail) {
        return notificationRepository.findTop20ByRecipientEmailOrderByCreatedAtDesc(userEmail)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(String userEmail) {
        return notificationRepository.countByRecipientEmailAndReadFalse(userEmail);
    }

    @Override
    @Transactional
    public void markRead(Long notificationId, String userEmail) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        if (!notification.getRecipientEmail().equals(userEmail)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "This notification belongs to another user.");
        }

        notification.setRead(true);
    }

    @Override
    @Transactional
    public void markAllRead(String userEmail) {
        notificationRepository.markAllReadForRecipient(userEmail);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getType().name(),
                notification.getTitle(),
                notification.getLinkTarget(),
                notification.isRead(),
                notification.getCreatedAt());
    }
}
