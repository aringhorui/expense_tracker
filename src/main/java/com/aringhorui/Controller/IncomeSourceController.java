package com.aringhorui.Controller;

import com.aringhorui.Entities.IncomeSource;
import com.aringhorui.Service.IncomeSourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/income")
@CrossOrigin(origins = "*") // optional: allows frontend access from anywhere
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
     * Example query:
     * /api/income/list?userEmail=john@example.com&incomeType=Salary&startDate=2025-01-01&endDate=2025-10-01&sortOrder=desc
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
}
