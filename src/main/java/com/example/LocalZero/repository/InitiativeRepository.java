package com.example.LocalZero.repository;

import com.example.LocalZero.Model.Initiative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * An interface to help with communication with the database. Extends JpaRepository, and gives
 * us existing methods to save and see data.
 */
@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {



}
