package com.example.LocalZero.service.messaging.filter;

import com.example.LocalZero.dto.SendMessageRequest;
import com.example.LocalZero.exception.ValidationException;
import org.springframework.stereotype.Component;

/**
 * This filter makes sure that the message is not empty.
 * Is one of the chains from MessageFilter's CoR.
 */

@Component
public class EmptyMessageFilter extends MessageFilter {
    @Override
    protected void doFilter(String senderEmail, SendMessageRequest request) {
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            throw new ValidationException("Message cannot be empty.");
        }
    }
}
