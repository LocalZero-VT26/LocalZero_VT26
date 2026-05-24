package com.example.LocalZero.service.messaging.filter;


public class FilterChainSingleton {
    private static FilterChainSingleton instance;
    private final MessageFilter rootFilter;

    private FilterChainSingleton() {
        MessageFilter emptyFilter = new EmptyMessageFilter();
        MessageFilter profanityFilter = new ProfanityFilter();
        
        emptyFilter.setNext(profanityFilter);
        this.rootFilter = emptyFilter;
    }

    public static synchronized FilterChainSingleton getInstance() {
        if (instance == null) {
            instance = new FilterChainSingleton();
        }
        return instance;
    }

    public MessageFilter getFilterChain() {
        return rootFilter;
    }
}
