package com.aringhorui.Repositories;

import com.aringhorui.Entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    // Get all expenses for a user
    List<Expense> findByUserEmail(String email);

    // ✅ PostgreSQL-safe query with explicit casting
    @Query("SELECT e FROM Expense e WHERE e.userEmail = :email "
            + "AND e.date >= COALESCE(:startDate, e.date) "
            + "AND e.date <= COALESCE(:endDate, e.date) "
            + "AND (:category IS NULL OR e.category = :category) "
            + "AND (:paymentMethod IS NULL OR e.paymentMethod = :paymentMethod) "
            + "AND (:expenseType IS NULL OR e.expenseType = :expenseType)")
    List<Expense> findExpensesByFilters(
            @Param("email") String email,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("category") String category,
            @Param("paymentMethod") String paymentMethod,
            @Param("expenseType") String expenseType
    );



}
