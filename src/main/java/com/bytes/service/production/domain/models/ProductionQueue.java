package com.bytes.service.production.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


public class ProductionQueue {
    private Long orderId;
    private String customerName;
    private ProductionStatus status;
    private Integer queuePosition;
    private LocalDateTime receivedAt;
    private Long waitingTimeMinutes;

    // TODO: Implement ProductionItem class
    private List<ProductionItem> items;

    public ProductionQueue(Long orderId, String customerName, ProductionStatus status, Integer queuePosition, LocalDateTime receivedAt) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.status = status;
        this.queuePosition = queuePosition;
        this.receivedAt = receivedAt;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public void setStatus(ProductionStatus status) {
        this.status = status;
    }

    public Integer getQueuePosition() {
        return queuePosition;
    }

    public void setQueuePosition(Integer queuePosition) {
        this.queuePosition = queuePosition;
    }

    public LocalDateTime getReceivedAt() {
        return receivedAt;
    }

    public void setReceivedAt(LocalDateTime receivedAt) {
        this.receivedAt = receivedAt;
    }

    public Long getWaitingTimeMinutes() {
        return waitingTimeMinutes;
    }

    public void setWaitingTimeMinutes(Long waitingTimeMinutes) {
        this.waitingTimeMinutes = waitingTimeMinutes;
    }

    public List<ProductionItem> getItems() {
        return items;
    }

    public void setItems(List<ProductionItem> items) {
        this.items = items;
    }
}