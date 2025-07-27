package com.bytes.service.production.domain.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

public class OrderProduction {
    private Long id;
    
    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductionStatus status;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
    
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;

    @Column(name = "finished_at")
    private LocalDateTime DeliveredAt;
    
    @Column(name = "position_in_queue")
    private Integer positionInQueue;
    
    @Column(name = "customer_name")
    private String customerName;


    public OrderProduction(Long orderId, ProductionStatus status, LocalDateTime startedAt, Integer positionInQueue, String customerName) {
        this.orderId = orderId;
        this.status = status;
        this.startedAt = startedAt;
        this.positionInQueue = positionInQueue;
        this.customerName = customerName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public ProductionStatus getStatus() {
        return status;
    }

    public void setStatus(ProductionStatus status) {
        this.status = status;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Integer getPositionInQueue() {
        return positionInQueue;
    }

    public void setPositionInQueue(Integer positionInQueue) {
        this.positionInQueue = positionInQueue;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public LocalDateTime getDeliveredAt() {
        return DeliveredAt;
    }

    public void setDeliveredAt(LocalDateTime deliveredAt) {
        DeliveredAt = deliveredAt;
    }
}