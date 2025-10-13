package com.aringhorui.DTO;

import java.time.LocalDate;

public class ExpenseDTO {
    private Long id;
    private LocalDate date;
    private Double amount;
    private String category;
    private String paymentMethod;
    private String bankAccount;
    private String expenseType;
    private String description;
    private String location;
    private String userEmail;

    // No-args constructor (required by Jackson for deserialization)
    public ExpenseDTO() {
    }

    // All-args constructor (useful for creating objects)
    public ExpenseDTO(Long id, LocalDate date, Double amount, String category,
                      String paymentMethod, String bankAccount, String expenseType,
                      String description, String location, String userEmail) {
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.paymentMethod = paymentMethod;
        this.bankAccount = bankAccount;
        this.expenseType = expenseType;
        this.description = description;
        this.location = location;
        this.userEmail = userEmail;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Double getAmount() {
        return amount;
    }

    public String getCategory() {
        return category;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getUserEmail() {
        return userEmail;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
