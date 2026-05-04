package com.example.LocalZero.repository;

import com.example.LocalZero.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoomIdOrderByCreatedAtAsc(Long roomId);

    boolean existsByRoomIdAndReadFalseAndSenderEmailNot(Long roomId, String senderEmail);
}