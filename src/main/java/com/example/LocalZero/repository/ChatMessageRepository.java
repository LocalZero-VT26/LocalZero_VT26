package com.example.LocalZero.repository;

import com.example.LocalZero.Model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoomIdOrderByCreatedAtAsc(Long chatRoomId);

    boolean existsByChatRoomIdAndReadFalseAndSenderEmailNot(Long chatRoomId, String senderEmail);
}