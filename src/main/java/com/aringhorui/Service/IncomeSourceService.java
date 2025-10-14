package com.aringhorui.Service;

import com.aringhorui.Entities.IncomeSource;
import com.aringhorui.Repositories.IncomeSourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IncomeSourceService {

    private final IncomeSourceRepository incomeSourceRepository;

    @Autowired
    public IncomeSourceService(IncomeSourceRepository incomeSourceRepository) {
        this.incomeSourceRepository = incomeSourceRepository;
    }

    // Add a new income
    public IncomeSource addIncome(IncomeSource incomeSource) {
        return incomeSourceRepository.save(incomeSource);
    }

    /**
     * Get all incomes for a user with optional filters
     */
    public List<IncomeSource> getIncomes(String userEmail,
                                         String incomeType,
                                         String description,
                                         LocalDate startDate,
                                         LocalDate endDate,
                                         String sortOrder) {

        // Start with all incomes for user
        List<IncomeSource> incomes = incomeSourceRepository.findByUserEmail(userEmail);

        // Filter by incomeType if provided
        if (incomeType != null && !incomeType.isBlank()) {
            incomes = incomes.stream()
                    .filter(i -> i.getIncomeType().equalsIgnoreCase(incomeType))
                    .collect(Collectors.toList());
        }

        // Filter by description keyword if provided
        if (description != null && !description.isBlank()) {
            incomes = incomes.stream()
                    .filter(i -> i.getDescription() != null &&
                            i.getDescription().toLowerCase().contains(description.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // Filter by date range
        if (startDate != null) {
            incomes = incomes.stream()
                    .filter(i -> i.getDateReceived() != null && !i.getDateReceived().isBefore(startDate))
                    .collect(Collectors.toList());
        }
        if (endDate != null) {
            incomes = incomes.stream()
                    .filter(i -> i.getDateReceived() != null && !i.getDateReceived().isAfter(endDate))
                    .collect(Collectors.toList());
        }

        // Sort by amount
        if (sortOrder != null) {
            if (sortOrder.equalsIgnoreCase("asc")) {
                incomes.sort((a, b) -> a.getAmount().compareTo(b.getAmount()));
            } else if (sortOrder.equalsIgnoreCase("desc")) {
                incomes.sort((a, b) -> b.getAmount().compareTo(a.getAmount()));
            }
        }

        return incomes;
    }
}
