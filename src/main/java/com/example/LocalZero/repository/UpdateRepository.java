package com.example.LocalZero.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.LocalZero.Model.Update;
import java.util.List;

@Repository
public interface UpdateRepository extends JpaRepository<Update, Long> {
    List<Update> findByInitiativeId(Long initiativeId);
}
