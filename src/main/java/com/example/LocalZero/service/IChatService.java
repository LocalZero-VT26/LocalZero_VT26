package com.example.LocalZero.service;

import com.example.LocalZero.dto.ChatMessageResponse;
import com.example.LocalZero.dto.ChatRoomSummaryResponse;
import com.example.LocalZero.dto.SendMessageRequest;

import java.util.List;

public interface IChatService {

    void sendMessage(String senderEmail, SendMessageRequest request);

    List<ChatRoomSummaryResponse> getRoomsForUser(String userEmail);

    List<ChatMessageResponse> getRoomMessage(String userEmail, Long roomId);
}
