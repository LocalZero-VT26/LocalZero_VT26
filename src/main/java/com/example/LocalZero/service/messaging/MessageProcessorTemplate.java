package com.example.LocalZero.service.messaging;

import com.example.LocalZero.dto.ChatMessageResponse;
import com.example.LocalZero.dto.SendMessageRequest;
import com.example.LocalZero.service.messaging.filter.FilterChainSingleton;

public abstract class MessageProcessorTemplate {
    
    public final ChatMessageResponse process(String senderEmail, SendMessageRequest request) {
        validate(senderEmail, request);
        filter(senderEmail, request);
        ChatMessageResponse response = persist(senderEmail, request);
        notifyUser(senderEmail, request);
        return response;
    }

    protected abstract void validate(String senderEmail, SendMessageRequest request);
    
    protected void filter(String senderEmail, SendMessageRequest request) {
        FilterChainSingleton.getInstance().getFilterChain().filter(senderEmail, request);
    }
    
    protected abstract ChatMessageResponse persist(String senderEmail, SendMessageRequest request);
    protected abstract void notifyUser(String senderEmail, SendMessageRequest request);
}
