package com.example.LocalZero.service.messaging.filter;

import com.example.LocalZero.dto.SendMessageRequest;

public abstract class MessageFilter {
    private MessageFilter nextFilter;

    public void setNext(MessageFilter nextFilter) {
        this.nextFilter = nextFilter;
    }

    public void filter(String senderEmail, SendMessageRequest request) {
        doFilter(senderEmail, request);
        if (nextFilter != null) {
            nextFilter.filter(senderEmail, request);
        }
    }

    protected abstract void doFilter(String senderEmail, SendMessageRequest request);
}
