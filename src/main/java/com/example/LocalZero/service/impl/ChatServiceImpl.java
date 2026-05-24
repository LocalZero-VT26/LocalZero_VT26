package com.example.LocalZero.service.impl;

import com.example.LocalZero.Model.ChatMessage;
import com.example.LocalZero.Model.ChatRoom;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.ChatMessageResponse;
import com.example.LocalZero.dto.ChatRoomSummaryResponse;
import com.example.LocalZero.dto.SendMessageRequest;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.ChatMessageRepository;
import com.example.LocalZero.repository.ChatRoomRepository;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.IChatService;
import com.example.LocalZero.service.INotification;
import com.example.LocalZero.service.OnlineUserRegistery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class ChatServiceImpl implements IChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final INotification chatMessageNotification;
    private final OnlineUserRegistery onlineUserRegistery;

    public ChatServiceImpl(ChatRoomRepository chatRoomRepository,
                           ChatMessageRepository chatMessageRepository,
                           UserRepository userRepository,
                           @Qualifier("chatMessageNotification") INotification chatMessageNotification,
                           OnlineUserRegistery onlineUserRegistery) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.chatMessageNotification = chatMessageNotification;
        this.onlineUserRegistery = onlineUserRegistery;
    }

    @Override
    @Transactional
    public ChatMessageResponse sendMessage(String senderEmail, SendMessageRequest request) {
        com.example.LocalZero.service.messaging.command.ChatCommand command = 
                new com.example.LocalZero.service.messaging.command.SendMessageCommand(
                senderEmail, request, chatRoomRepository, chatMessageRepository, userRepository, 
                chatMessageNotification, onlineUserRegistery);
                
        return command.execute();
    }

    @Override
    public List<ChatRoomSummaryResponse> getRoomsForUser(String userEmail) {
        List<ChatRoom> chatRooms = chatRoomRepository.findAllRoomsForUser(userEmail);
        List<ChatRoomSummaryResponse> responseList = new ArrayList<>();


        for (ChatRoom chatRoom : chatRooms) {
            String otherEmail;

            if (chatRoom.getUser1Email().equals(userEmail)) {
                otherEmail = chatRoom.getUser2Email();
            } else {
                otherEmail = chatRoom.getUser1Email();
            }

            User otherUser = userRepository.findByEmail(otherEmail)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            boolean hasUnseen = chatMessageRepository.existsByChatRoomIdAndReadFalseAndSenderEmailNot(chatRoom.getId(), userEmail);
            boolean otherUserOnline = onlineUserRegistery.isOnline(otherEmail);

            responseList.add(new ChatRoomSummaryResponse(
                    chatRoom.getId(),
                    otherUser.getEmail(),
                    otherUser.getName(),
                    hasUnseen,
                    otherUserOnline
            ));
        }

        return responseList;
    }

    @Override
    @Transactional
    public List<ChatMessageResponse> getRoomMessage(String userEmail, Long roomId) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        boolean isMember = chatRoom.getUser1Email().equals(userEmail) || chatRoom.getUser2Email().equals(userEmail);

        if (!isMember) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }

        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId);

        List<ChatMessageResponse> messageResponses = new ArrayList<>();

        for (ChatMessage chatMessage : messages) {
            if (!chatMessage.getSenderEmail().equals(userEmail) && !chatMessage.isRead()) {
                chatMessage.setRead(true);
            }
            messageResponses.add(new ChatMessageResponse(
                    chatMessage.getId(),
                    chatMessage.getSenderEmail(),
                    chatMessage.getContent(),
                    chatMessage.getCreatedAt(),
                    chatMessage.isRead()
            ));
        }

        return messageResponses;
    }
}
