package com.aringhorui.Service;

import com.aringhorui.Controller.ExpenseController.ExpenseRequest;
import com.aringhorui.Entities.Expense;
import com.aringhorui.Repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;  // CORRECTED: Use java.util.Optional

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;


    // -------------------------
    // Log a new expense
    // -------------------------
    public Expense logExpense(Expense expense, String userEmail) {
        expense.setUserEmail(userEmail);
        return expenseRepository.save(expense);
    }

    // -------------------------
    // Get expense by ID
    // -------------------------
    public Optional<Expense> getExpenseById(Long id) {
        return expenseRepository.findById(id);
    }

    // -------------------------
    // Get expenses for a user with optional filters and date range
    // -------------------------
    public List<Expense> getExpensesWithFilters(
            String userEmail,
            LocalDate startDate,
            LocalDate endDate,
            String category,
            String paymentMethod,
            String expenseType
    ) {
        if (startDate == null || endDate == null) {
            LocalDate now = LocalDate.now();
            startDate = now.withDayOfMonth(1);
            endDate = now.withDayOfMonth(now.lengthOfMonth());
        }

        return expenseRepository.findExpensesByFilters(
                userEmail, startDate, endDate, category, paymentMethod, expenseType
        );
    }

    // -------------------------
    // Get all expenses for a user
    // -------------------------
    public List<Expense> getExpensesByUserEmail(String userEmail) {
        return expenseRepository.findByUserEmail(userEmail);
    }

    // -------------------------
    // Delete expense by ID
    // -------------------------
    public void deleteExpense(Long id) {
        if (!expenseRepository.existsById(id)) {
            throw new RuntimeException("Expense not found with id: " + id);
        }
        expenseRepository.deleteById(id);
    }

    // -------------------------
    // Update expense
    // -------------------------
    public Expense updateExpense(Long id, ExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found with id: " + id));

        // Set updated fields
        expense.setUserEmail(request.getEmail());
        expense.setAmount(request.getAmount());
        expense.setCategory(request.getCategory());
        expense.setPaymentMethod(request.getPaymentMethod());
        expense.setBankAccount(request.getBankAccount());
        expense.setExpenseType(request.getExpenseType());
        expense.setDescription(request.getDescription());
        expense.setLocation(request.getLocation());
        expense.setDate(LocalDate.parse(request.getDate()));

        return expenseRepository.save(expense);
    }


}
