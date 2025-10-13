package com.aringhorui.Controller;

import com.aringhorui.DTO.ExpenseDTO;
import com.aringhorui.Entities.Expense;
import com.aringhorui.Service.ExpenseService;
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

    // -------------------------
    // Get expenses with filters
    // -------------------------
    @GetMapping("/list/{email}")
    public ResponseEntity<List<Expense>> getExpenses(
            @PathVariable String email,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) String expenseType
    ) {
        List<Expense> expenses = expenseService.getExpensesWithFilters(
                email, startDate, endDate, category, paymentMethod, expenseType
        );
        return ResponseEntity.ok(expenses);
    }

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
            error.put("message", "No expense exists with id: " + id);
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
            // Log incoming request
            System.out.println("========== EXPENSE LOG REQUEST ==========");
            System.out.println("Email: " + request.getEmail());
            System.out.println("Amount: " + request.getAmount());
            System.out.println("Category: " + request.getCategory());
            System.out.println("Payment Method: " + request.getPaymentMethod());
            System.out.println("Bank Account: " + request.getBankAccount());
            System.out.println("Expense Type: " + request.getExpenseType());
            System.out.println("Description: " + request.getDescription());
            System.out.println("Location: " + request.getLocation());
            System.out.println("Date: " + request.getDate());
            System.out.println("=========================================");

            // Manual validation
            Map<String, String> validationErrors = validateExpenseRequest(request);
            if (!validationErrors.isEmpty()) {
                System.err.println("VALIDATION FAILED: " + validationErrors);
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Validation Failed");
                response.put("validationErrors", validationErrors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            System.out.println("Validation passed, creating expense entity...");

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

            System.out.println("Calling service to save expense...");
            Expense saved = expenseService.logExpense(expense, request.getEmail());
            System.out.println("SUCCESS! Expense saved with ID: " + saved.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            System.err.println("========== ERROR SAVING EXPENSE ==========");
            System.err.println("Exception Type: " + e.getClass().getName());
            System.err.println("Exception Message: " + e.getMessage());
            System.err.println("Stack Trace:");
            e.printStackTrace();
            System.err.println("==========================================");

            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to log expense");
            error.put("message", e.getMessage() != null ? e.getMessage() : "Unknown error");
            error.put("type", e.getClass().getSimpleName());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // -------------------------
    // Update an existing expense
    // -------------------------
    @PutMapping("/{id}")
    public ResponseEntity<?> updateExpense(
            @PathVariable Long id,
            @RequestBody ExpenseRequest request) {
        try {
            // Manual validation
            Map<String, String> validationErrors = validateExpenseRequest(request);
            if (!validationErrors.isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("error", "Validation Failed");
                response.put("validationErrors", validationErrors);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            Expense updatedExpense = expenseService.updateExpense(id, request);
            return ResponseEntity.ok(updatedExpense);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update expense");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to update expense");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    // -------------------------
    // Delete expense by ID
    // -------------------------
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        try {
            expenseService.deleteExpense(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Expense deleted successfully");
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Expense not found");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Failed to delete expense");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    // -------------------------
    // Manual validation helper method
    // -------------------------
    private Map<String, String> validateExpenseRequest(ExpenseRequest request) {
        Map<String, String> errors = new HashMap<>();

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            errors.put("email", "Email is required");
        } else if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            errors.put("email", "Email should be valid");
        }

        if (request.getAmount() == null) {
            errors.put("amount", "Amount is required");
        } else if (request.getAmount() <= 0) {
            errors.put("amount", "Amount must be greater than 0");
        }

        if (request.getCategory() == null || request.getCategory().trim().isEmpty()) {
            errors.put("category", "Category is required");
        }

        if (request.getPaymentMethod() == null || request.getPaymentMethod().trim().isEmpty()) {
            errors.put("paymentMethod", "Payment method is required");
        }

        if (request.getExpenseType() == null || request.getExpenseType().trim().isEmpty()) {
            errors.put("expenseType", "Expense type is required");
        }

        if (request.getDate() == null || request.getDate().trim().isEmpty()) {
            errors.put("date", "Date is required");
        } else if (!request.getDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
            errors.put("date", "Date must be in YYYY-MM-DD format");
        }

        return errors;
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
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getBankAccount() {
            return bankAccount;
        }

        public void setBankAccount(String bankAccount) {
            this.bankAccount = bankAccount;
        }

        public String getExpenseType() {
            return expenseType;
        }

        public void setExpenseType(String expenseType) {
            this.expenseType = expenseType;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
}