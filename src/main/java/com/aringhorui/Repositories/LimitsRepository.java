package com.aringhorui.Repositories;

import com.aringhorui.Entities.Limits;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LimitsRepository extends JpaRepository<Limits, Long> {

    // Return all limits for a specific email
    List<Limits> findByEmail(String email);

    // Return all limits for a specific category
    List<Limits> findBySuperCategory(String superCategory);

    // Return a specific limit for a user + category combination
    Optional<Limits> findByEmailAndSuperCategory(String email, String superCategory);
}
