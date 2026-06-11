package com.example.LocalZero.repository;

import com.example.LocalZero.Model.Notification;
import com.example.LocalZero.Model.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findTop20ByRecipientEmailOrderByCreatedAtDesc(String recipientEmail);

    long countByRecipientEmailAndReadFalse(String recipientEmail);

    boolean existsByRecipientEmailAndTypeAndLinkTargetAndReadFalse(
            String recipientEmail, NotificationType type, String linkTarget);

    @Modifying
    @Query("UPDATE Notification n SET n.read = true WHERE n.recipientEmail = :email AND n.read = false")
    void markAllReadForRecipient(@Param("email") String email);
}
