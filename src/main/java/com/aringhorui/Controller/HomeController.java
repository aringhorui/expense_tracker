package com.aringhorui.Controller;

import com.aringhorui.Entities.Expense;
import com.aringhorui.Repositories.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import com.aringhorui.DTO.CategorySummary;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class HomeController {

    @Autowired
    private ExpenseRepository expenseRepository;

    // Fetch expenses with optional filters
    @GetMapping("/expenses")
    public List<Expense> getExpenses(
            @RequestParam String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) String expenseType
    ) {
        return expenseRepository.findExpensesByFilters(email, startDate, endDate, category, paymentMethod, expenseType);
    }

    @GetMapping("/expenses/category-summary")
    public List<CategorySummary> getExpensesByCategory(
            @RequestParam String email,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return expenseRepository.findExpensesByCategory(email, startDate, endDate);
    }

}
