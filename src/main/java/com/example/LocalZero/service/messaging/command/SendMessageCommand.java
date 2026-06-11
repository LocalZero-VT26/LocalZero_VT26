package com.example.LocalZero.service.messaging.command;

import com.example.LocalZero.Model.ChatMessage;
import com.example.LocalZero.Model.ChatRoom;
import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.ChatMessageResponse;
import com.example.LocalZero.dto.SendMessageRequest;
import com.example.LocalZero.exception.ResourceNotFoundException;
import com.example.LocalZero.repository.ChatMessageRepository;
import com.example.LocalZero.repository.ChatRoomRepository;
import com.example.LocalZero.repository.UserRepository;
import com.example.LocalZero.service.messaging.MessageProcessorTemplate;
import com.example.LocalZero.service.notification.event.NewChatMessageEvent;
import org.springframework.context.ApplicationEventPublisher;

public class SendMessageCommand extends MessageProcessorTemplate implements ChatCommand {

    private final String senderEmail;
    private final SendMessageRequest request;
    
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;
    
    private User recipient;

    public SendMessageCommand(String senderEmail, SendMessageRequest request,
                              ChatRoomRepository chatRoomRepository,
                              ChatMessageRepository chatMessageRepository,
                              UserRepository userRepository,
                              ApplicationEventPublisher eventPublisher) {
        this.senderEmail = senderEmail;
        this.request = request;
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public ChatMessageResponse execute() {
        return process(senderEmail, request);
    }

    @Override
    protected void validate(String senderEmail, SendMessageRequest request) {
        this.recipient = userRepository.findByEmail(request.getRecipientEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Recipient not found"));
    }

    @Override
    protected ChatMessageResponse persist(String senderEmail, SendMessageRequest request) {
        String email1;
        String email2;

        if (senderEmail.compareTo(request.getRecipientEmail()) < 0) {
            email1 = senderEmail;
            email2 = request.getRecipientEmail();
        } else {
            email1 = request.getRecipientEmail();
            email2 = senderEmail;
        }

        ChatRoom chatRoom = chatRoomRepository.findByUser1EmailAndUser2Email(email1, email2)
                .orElseGet(() -> chatRoomRepository.save(new ChatRoom(email1, email2)));

        ChatMessage saved = chatMessageRepository.save(new ChatMessage(chatRoom, senderEmail, request.getContent()));
        
        return new ChatMessageResponse(saved.getId(), saved.getSenderEmail(), saved.getContent(), saved.getCreatedAt(), saved.isRead());
    }

    @Override
    protected void notifyUser(String senderEmail, SendMessageRequest request) {
        User sender = userRepository.findByEmail(senderEmail).orElse(null);
        String senderName = sender != null ? sender.getName() : senderEmail;

        eventPublisher.publishEvent(new NewChatMessageEvent(recipient.getEmail(), senderName));
    }
}
