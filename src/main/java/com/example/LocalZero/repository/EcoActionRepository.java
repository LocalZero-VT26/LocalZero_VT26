package com.example.LocalZero.repository;

import com.example.LocalZero.Model.EcoAction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EcoActionRepository extends JpaRepository<EcoAction, Long> {
}
