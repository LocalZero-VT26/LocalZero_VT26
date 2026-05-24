package com.example.LocalZero.service.messaging.filter;

import com.example.LocalZero.dto.SendMessageRequest;
import com.example.LocalZero.exception.ValidationException;
import org.springframework.stereotype.Component;

@Component
public class EmptyMessageFilter extends MessageFilter {
    @Override
    protected void doFilter(String senderEmail, SendMessageRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new ValidationException("Message cannot be empty.");
        }
    }
}
