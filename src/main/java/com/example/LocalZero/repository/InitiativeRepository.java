package com.example.LocalZero.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.LocalZero.Model.Initiative;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {
	@Query("""
			select i.id, count(p)
			from Initiative i
			left join i.participants p
			where i.id in :initiativeIds
			group by i.id
			""")
	List<Object[]> countParticipantsByInitiativeIds(@Param("initiativeIds") List<Long> initiativeIds);

}
