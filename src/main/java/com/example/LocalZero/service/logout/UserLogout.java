package com.example.LocalZero.service.logout;


import com.example.LocalZero.Model.BlacklistedToken;
import com.example.LocalZero.repository.BlacklistedTokenRepository;
import com.example.LocalZero.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service("userLogout")
@RequiredArgsConstructor
public class UserLogout extends UserLogoutTemplate {

    private final JwtService jwtService;
    private final BlacklistedTokenRepository blacklistedTokenRepository;

    @Override
    protected Date getExpiration(String token) {
        return jwtService.extractExpiration(token);
    }

    @Override
    protected void saveToBlacklist(String token, Date expiresAt) {
        LocalDateTime expiresAtLDT = expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        blacklistedTokenRepository.save(new BlacklistedToken(token, LocalDateTime.now(), expiresAtLDT));
    }

}
