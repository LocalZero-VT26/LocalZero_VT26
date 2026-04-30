package com.example.LocalZero.service.deleteaccount;

import com.example.LocalZero.Model.User;
import com.example.LocalZero.dto.DeleteAccountRequest;

public abstract class UserDeleteAccountTemplate {

    public final void deleteAccount(String token, DeleteAccountRequest request) {
        String email = extractEmail(token);
        User user = findUser(email);
        verifyPassword(request.getPassword(), user.getPassword());
        blacklistToken(token);
        deleteUser(user);
    }

    protected abstract String extractEmail(String token);
    protected abstract User findUser(String email);
    protected abstract void verifyPassword(String rawPassword, String hashPassword);
    protected abstract void blacklistToken(String token);
    protected abstract void deleteUser(User user);
}
