package com.aringhorui.Controller;

import com.aringhorui.Entities.IncomeSource;
import com.aringhorui.Service.IncomeSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/income")
@CrossOrigin(origins = "*")
public class IncomeSourceController {

    private final IncomeSourceService incomeSourceService;

    @Autowired
    public IncomeSourceController(IncomeSourceService incomeSourceService) {
        this.incomeSourceService = incomeSourceService;
    }

    // === Add new income ===
    @PostMapping("/add")
    public IncomeSource addIncome(@RequestBody IncomeSource incomeSource) {
        return incomeSourceService.addIncome(incomeSource);
    }

    /**
     * Get incomes for a user with optional filters
     */
    @GetMapping("/list")
    public List<IncomeSource> getIncomes(
            @RequestParam String userEmail,
            @RequestParam(required = false) String incomeType,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, defaultValue = "asc") String sortOrder
    ) {
        return incomeSourceService.getIncomes(userEmail, incomeType, description, startDate, endDate, sortOrder);
    }

    /**
     * Get income by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<IncomeSource> getIncomeById(@PathVariable Long id) {
        Optional<IncomeSource> income = incomeSourceService.getIncomeById(id);
        return income.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update an existing income
     */
    @PutMapping("/{id}")
    public ResponseEntity<IncomeSource> updateIncome(
            @PathVariable Long id,
            @RequestBody IncomeSource incomeSource
    ) {
        try {
            IncomeSource updated = incomeSourceService.updateIncome(id, incomeSource);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Delete an income
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        boolean deleted = incomeSourceService.deleteIncome(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}