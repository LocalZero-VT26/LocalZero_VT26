package com.example.LocalZero.repository;

import com.example.LocalZero.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existByEmail(String email);
}
