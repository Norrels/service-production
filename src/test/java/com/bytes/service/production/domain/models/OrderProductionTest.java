package com.bytes.service.production.domain.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class OrderProductionTest {

    private OrderProduction orderProduction;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        orderProduction = new OrderProduction(1L, ProductionStatus.RECEIVED, testTime, 1, "John Doe");
    }

    @Test
    void shouldCreateOrderProductionWithValidData() {
        assertNotNull(orderProduction);
        assertEquals(1L, orderProduction.getOrderId());
        assertEquals(ProductionStatus.RECEIVED, orderProduction.getStatus());
        assertEquals(testTime, orderProduction.getStartedAt());
        assertEquals(1, orderProduction.getPositionInQueue());
        assertEquals("John Doe", orderProduction.getCustomerName());
    }

    @Test
    void shouldSetAndGetId() {
        orderProduction.setId(100L);
        assertEquals(100L, orderProduction.getId());
    }

    @Test
    void shouldSetAndGetOrderId() {
        orderProduction.setOrderId(999L);
        assertEquals(999L, orderProduction.getOrderId());
    }

    @Test
    void shouldSetAndGetStatus() {
        orderProduction.setStatus(ProductionStatus.IN_PREPARATION);
        assertEquals(ProductionStatus.IN_PREPARATION, orderProduction.getStatus());
    }

    @Test
    void shouldSetAndGetStartedAt() {
        LocalDateTime newTime = LocalDateTime.of(2024, 2, 1, 12, 30);
        orderProduction.setStartedAt(newTime);
        assertEquals(newTime, orderProduction.getStartedAt());
    }

    @Test
    void shouldSetAndGetUpdatedAt() {
        LocalDateTime updateTime = LocalDateTime.of(2024, 1, 1, 11, 0);
        orderProduction.setUpdatedAt(updateTime);
        assertEquals(updateTime, orderProduction.getUpdatedAt());
    }

    @Test
    void shouldSetAndGetFinishedAt() {
        LocalDateTime finishTime = LocalDateTime.of(2024, 1, 1, 15, 0);
        orderProduction.setFinishedAt(finishTime);
        assertEquals(finishTime, orderProduction.getFinishedAt());
    }

    @Test
    void shouldSetAndGetDeliveredAt() {
        LocalDateTime deliveryTime = LocalDateTime.of(2024, 1, 1, 16, 0);
        orderProduction.setDeliveredAt(deliveryTime);
        assertEquals(deliveryTime, orderProduction.getDeliveredAt());
    }

    @Test
    void shouldSetAndGetPositionInQueue() {
        orderProduction.setPositionInQueue(5);
        assertEquals(5, orderProduction.getPositionInQueue());
    }

    @Test
    void shouldSetAndGetCustomerName() {
        orderProduction.setCustomerName("Jane Smith");
        assertEquals("Jane Smith", orderProduction.getCustomerName());
    }

    @Test
    void shouldHandleNullValues() {
        OrderProduction nullOrder = new OrderProduction(null, null, null, null, null);
        assertNull(nullOrder.getOrderId());
        assertNull(nullOrder.getStatus());
        assertNull(nullOrder.getStartedAt());
        assertNull(nullOrder.getPositionInQueue());
        assertNull(nullOrder.getCustomerName());
    }
}