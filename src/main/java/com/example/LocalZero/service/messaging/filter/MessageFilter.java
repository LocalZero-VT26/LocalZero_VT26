package com.example.LocalZero.service.messaging.filter;

import com.example.LocalZero.dto.SendMessageRequest;

/**
 * Base of the CoR for the messaging filter, implementing CoR.
 * The chain is created in the FilterChainSingleton constructor.
 */

public abstract class MessageFilter {
    private MessageFilter nextFilter;

    /**
     * The filter knows who the next filter is.
     * Implementing CoR.
     */
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
