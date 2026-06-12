package com.example.LocalZero.repository;

import com.example.LocalZero.Model.Update;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UpdateRepository extends JpaRepository<Update, Long> {
    List<Update> findByInitiativeId(Long initiativeId);

    List<Update> findByInitiativeIdOrderByCreatedAtDesc(Long initiativeId);

    @Query("SELECT u.initiative.id FROM Update u WHERE u.id = :updateId")
    Long findInitiativeIdByUpdateId(@Param("updateId") Long updateId);

    @Query("""
            SELECT DISTINCT u FROM Update u
            JOIN FETCH u.author
            JOIN u.initiative i
            WHERE LOWER(i.visibility) = 'public'
               OR i.creator.email = :userEmail
               OR EXISTS (SELECT 1 FROM i.participants p WHERE p.email = :userEmail)
               OR (LOWER(i.visibility) IN ('neighborhood-specific', 'neighborhood')
                   AND LOWER(i.location) = LOWER(:userLocation))
            """)
    List<Update> findVisibleForUser(
            @Param("userEmail") String userEmail,
            @Param("userLocation") String userLocation,
            Pageable pageable);
}
