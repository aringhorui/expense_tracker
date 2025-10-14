package com.aringhorui.Entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "income_sources")
public class IncomeSource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "income_type", nullable = false, length = 50)
    private String incomeType;  // stored as plain String

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(length = 10)
    private String currency;

    private LocalDate dateReceived;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(length = 255)
    private String description;  // 🆕 new field for additional details

    // === Constructors ===
    public IncomeSource() {}

    public IncomeSource(String name, String incomeType, BigDecimal amount, String currency,
                        LocalDate dateReceived, String userEmail, String description) {
        this.name = name;
        this.incomeType = incomeType;
        this.amount = amount;
        this.currency = currency;
        this.dateReceived = dateReceived;
        this.userEmail = userEmail;
        this.description = description;
    }

    // === Getters and Setters ===
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIncomeType() { return incomeType; }
    public void setIncomeType(String incomeType) { this.incomeType = incomeType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public LocalDate getDateReceived() { return dateReceived; }
    public void setDateReceived(LocalDate dateReceived) { this.dateReceived = dateReceived; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
