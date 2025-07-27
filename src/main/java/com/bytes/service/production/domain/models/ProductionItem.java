package com.bytes.service.production.domain.models;


public class ProductionItem {
    private String productName;
    private Integer quantity;
    private String category;
    private String observations;

    public ProductionItem(String productName, Integer quantity, String category, String observations) {
        this.productName = productName;
        this.quantity = quantity;
        this.category = category;
        this.observations = observations;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }
}