package com.example.LocalZero.service.messaging.filter;

import com.example.LocalZero.dto.SendMessageRequest;
import com.example.LocalZero.exception.ValidationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class ProfanityFilter extends MessageFilter {
    private final List<String> badWords = Arrays.asList("badword1", "badword2"); // Example words

    @Override
    protected void doFilter(String senderEmail, SendMessageRequest request) {
        if (request.getContent() != null) {
            String lowerContent = request.getContent().toLowerCase();
            for (String word : badWords) {
                if (lowerContent.contains(word)) {
                    throw new ValidationException("Message contains inappropriate content.");
                }
            }
        }
    }
}
