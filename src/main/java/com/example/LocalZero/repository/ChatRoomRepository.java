package com.example.LocalZero.repository;

import com.example.LocalZero.Model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByUser1EmailAndUser2Email(String user1Email, String user2Email);

    @Query("SELECT c FROM ChatRoom c WHERE c.user1Email = :email OR c.user2Email = :email")
    List<ChatRoom> findAllRoomsForUser(@Param("email") String email);
}
