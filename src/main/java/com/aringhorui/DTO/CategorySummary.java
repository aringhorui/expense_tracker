package com.aringhorui.DTO;

public class CategorySummary {
    private String category;
    private Double totalAmount;

    public CategorySummary(String category, Double totalAmount) {
        this.category = category;
        this.totalAmount = totalAmount;
    }

    // Getters and Setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
}
