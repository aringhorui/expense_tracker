package com.aringhorui.Repositories;

import com.aringhorui.Entities.IncomeSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface IncomeSourceRepository extends JpaRepository<IncomeSource, Long> {

    // Find all income sources for a specific user
    List<IncomeSource> findByUserEmail(String userEmail);

    // Find by income type (e.g., "Salary", "Business", etc.)
    List<IncomeSource> findByIncomeType(String incomeType);

    // Find by description containing a keyword (case-insensitive)
    List<IncomeSource> findByDescriptionContainingIgnoreCase(String keyword);

    // Find all income sources received after a specific date
    List<IncomeSource> findByDateReceivedAfter(LocalDate date);

    // Find all income sources within a date range
    List<IncomeSource> findByDateReceivedBetween(LocalDate startDate, LocalDate endDate);
}
