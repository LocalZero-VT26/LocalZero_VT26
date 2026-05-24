package com.example.LocalZero.repository;

import com.example.LocalZero.Model.EcoAction;
import com.example.LocalZero.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EcoActionRepository extends JpaRepository<EcoAction, Long> {
    List<EcoAction> findByUserOrderByTimestampDesc (User user);
}
