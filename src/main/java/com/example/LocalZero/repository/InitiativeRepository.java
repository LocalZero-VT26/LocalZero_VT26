package com.example.LocalZero.repository;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.LocalZero.Model.Initiative;

@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {



}
