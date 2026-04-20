package com.example.LocalZero.repository;

import com.example.LocalZero.model.BlacklistedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {

    boolean existByToken(String token);
}
