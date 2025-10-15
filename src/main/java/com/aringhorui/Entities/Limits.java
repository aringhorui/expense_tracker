package com.aringhorui.Entities;

import jakarta.persistence.*;

@Entity
@Table(name = "limits")
public class Limits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "super_category", nullable = false)
    private String superCategory;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "email", nullable = false)
    private String email;

    // Constructors
    public Limits() {}

    public Limits(String superCategory, Double amount, String email) {
        this.superCategory = superCategory;
        this.amount = amount;
        this.email = email;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(String superCategory) {
        this.superCategory = superCategory;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // toString()
    @Override
    public String toString() {
        return "Limits{" +
                "id=" + id +
                ", superCategory='" + superCategory + '\'' +
                ", amount=" + amount +
                ", email='" + email + '\'' +
                '}';
    }
}
