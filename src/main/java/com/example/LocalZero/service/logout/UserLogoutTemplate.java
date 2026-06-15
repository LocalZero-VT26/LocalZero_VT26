package com.example.LocalZero.service.logout;

import java.util.Date;

/**
 * Template method-pattern for logging out.
 */

public abstract class UserLogoutTemplate {

    public final void logout(String token) {
        Date expiresAt = getExpiration(token);
        saveToBlacklist(token, expiresAt);
    }

    protected abstract Date getExpiration(String token);
    protected abstract void saveToBlacklist(String token, Date expiresAt);
}