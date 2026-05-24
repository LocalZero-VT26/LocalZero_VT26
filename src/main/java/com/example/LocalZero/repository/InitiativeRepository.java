package com.example.LocalZero.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.LocalZero.Model.Initiative;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {

    @Query("SELECT DISTINCT i FROM Initiative i LEFT JOIN i.participants p WHERE i.creator = :user OR p = :user")
    List<Initiative> findInitiativesByUser(@Param("user") com.example.LocalZero.Model.User user);

	@Query("""
			select i.id, count(p)
			from Initiative i
			left join i.participants p
			where i.id in :initiativeIds
			group by i.id
			""")
	List<Object[]> countParticipantsByInitiativeIds(@Param("initiativeIds") List<Long> initiativeIds);

	@Query("""
			SELECT DISTINCT i FROM Initiative i
			WHERE LOWER(i.visibility) = 'public'
			   OR i.creator.email = :userEmail
			   OR EXISTS (SELECT 1 FROM i.participants p WHERE p.email = :userEmail)
			   OR (LOWER(i.visibility) IN ('neighborhood-specific', 'neighborhood')
			       AND LOWER(i.location) = LOWER(:userLocation))
			""")
	List<Initiative> findVisibleForUser(
			@Param("userEmail") String userEmail,
			@Param("userLocation") String userLocation);

}
