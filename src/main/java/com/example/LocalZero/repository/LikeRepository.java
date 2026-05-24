package com.example.LocalZero.repository;

import com.example.LocalZero.Model.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    long countByUpdateId(Long updateId);
    Optional<LikeEntity> findByUpdateIdAndUserId(Long updateId, Long userId);
    void deleteByUpdateIdAndUserId(Long updateId, Long userId);
}
