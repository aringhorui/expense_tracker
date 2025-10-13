package com.aringhorui.Controller;

import com.aringhorui.DTO.ExpenseDTO;
import com.aringhorui.Entities.Expense;
import com.aringhorui.Service.ExpenseService;
import com.aringhorui.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/expenses")
@CrossOrigin(origins = "*")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserService userService;

    // -------------------------
    // Get expense by ID
    // -------------------------
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long id) {
        Optional<Expense> expense = expenseService.getExpenseById(id);
        if (expense.isPresent()) {
            return ResponseEntity.ok(expense.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Expense not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // -------------------------
    // Get all expenses for a user
    // -------------------------
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUserExpenses(@PathVariable String email) {
        try {
            List<Expense> expenses = expenseService.getExpensesByUserEmail(email);
            List<ExpenseDTO> dtos = expenses.stream()
                    .map(e -> new ExpenseDTO(
                            e.getId(),
                            e.getDate(),
                            e.getAmount(),
                            e.getCategory(),
                            e.getPaymentMethod(),
                            e.getBankAccount(),
                            e.getExpenseType(),
                            e.getDescription(),
                            e.getLocation(),
                            e.getUserEmail()
                    ))
                    .collect(Collectors.toList());
            return ResponseEntity.ok(dtos);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to fetch expenses");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // -------------------------
    // Log a new expense
    // -------------------------
    @PostMapping("/log")
    public ResponseEntity<?> logExpense(@RequestBody ExpenseRequest request) {
        try {
            Expense expense = new Expense();
            expense.setUserEmail(request.getEmail());
            expense.setAmount(request.getAmount());
            expense.setCategory(request.getCategory());
            expense.setPaymentMethod(request.getPaymentMethod());
            expense.setBankAccount(request.getBankAccount());
            expense.setExpenseType(request.getExpenseType());
            expense.setDescription(request.getDescription());
            expense.setLocation(request.getLocation());
            expense.setDate(LocalDate.parse(request.getDate()));

            Expense saved = expenseService.logExpense(expense, request.getEmail());
            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to log expense");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // -------------------------
    // Update an existing expense
    // -------------------------
    @PutMapping("/id/{id}")
    public ResponseEntity<?> updateExpense(@PathVariable Long id, @RequestBody ExpenseRequest request) {
        try {
            Expense updatedExpense = expenseService.updateExpense(id, request);
            return ResponseEntity.ok(updatedExpense);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update expense");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // -------------------------
    // Delete expense by ID
    // -------------------------
    @DeleteMapping("/id/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpense(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Expense deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete expense");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // -------------------------
    // DTO for request
    // -------------------------
    public static class ExpenseRequest {
        private String email;
        private Double amount;
        private String category;
        private String paymentMethod;
        private String bankAccount;
        private String expenseType;
        private String description;
        private String location;
        private String date;

        // Getters & setters
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
        public String getBankAccount() { return bankAccount; }
        public void setBankAccount(String bankAccount) { this.bankAccount = bankAccount; }
        public String getExpenseType() { return expenseType; }
        public void setExpenseType(String expenseType) { this.expenseType = expenseType; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getLocation() { return location; }
        public void setLocation(String location) { this.location = location; }
        public String getDate() { return date; }
        public void setDate(String date) { this.date = date; }
    }
}
