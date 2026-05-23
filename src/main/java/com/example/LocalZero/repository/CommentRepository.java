package com.example.LocalZero.repository;

import com.example.LocalZero.Model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUpdateIdOrderByCreatedAtAsc(Long updateId);
}
