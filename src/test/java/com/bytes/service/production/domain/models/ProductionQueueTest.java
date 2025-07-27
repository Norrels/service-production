package com.bytes.service.production.domain.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class ProductionQueueTest {

    private ProductionQueue productionQueue;
    private LocalDateTime testTime;

    @BeforeEach
    void setUp() {
        testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        productionQueue = new ProductionQueue(1L, "John Doe", ProductionStatus.RECEIVED, 1, testTime);
    }

    @Test
    void shouldCreateProductionQueueWithValidData() {
        assertNotNull(productionQueue);
        assertEquals(1L, productionQueue.getOrderId());
        assertEquals("John Doe", productionQueue.getCustomerName());
        assertEquals(ProductionStatus.RECEIVED, productionQueue.getStatus());
        assertEquals(1, productionQueue.getQueuePosition());
        assertEquals(testTime, productionQueue.getReceivedAt());
    }

    @Test
    void shouldSetAndGetOrderId() {
        productionQueue.setOrderId(999L);
        assertEquals(999L, productionQueue.getOrderId());
    }

    @Test
    void shouldSetAndGetCustomerName() {
        productionQueue.setCustomerName("Jane Smith");
        assertEquals("Jane Smith", productionQueue.getCustomerName());
    }

    @Test
    void shouldSetAndGetStatus() {
        productionQueue.setStatus(ProductionStatus.IN_PREPARATION);
        assertEquals(ProductionStatus.IN_PREPARATION, productionQueue.getStatus());
    }

    @Test
    void shouldSetAndGetQueuePosition() {
        productionQueue.setQueuePosition(5);
        assertEquals(5, productionQueue.getQueuePosition());
    }

    @Test
    void shouldSetAndGetReceivedAt() {
        LocalDateTime newTime = LocalDateTime.of(2024, 2, 1, 12, 30);
        productionQueue.setReceivedAt(newTime);
        assertEquals(newTime, productionQueue.getReceivedAt());
    }

    @Test
    void shouldSetAndGetWaitingTimeMinutes() {
        productionQueue.setWaitingTimeMinutes(30L);
        assertEquals(30L, productionQueue.getWaitingTimeMinutes());
    }

    @Test
    void shouldSetAndGetItems() {
        List<ProductionItem> items = new ArrayList<>();
        ProductionItem item1 = new ProductionItem("Pizza", 1, "Food", "No onions");
        ProductionItem item2 = new ProductionItem("Soda", 2, "Drinks", "Cold");
        items.add(item1);
        items.add(item2);

        productionQueue.setItems(items);
        
        assertEquals(2, productionQueue.getItems().size());
        assertEquals("Pizza", productionQueue.getItems().get(0).getProductName());
        assertEquals("Soda", productionQueue.getItems().get(1).getProductName());
    }

    @Test
    void shouldHandleNullValues() {
        ProductionQueue nullQueue = new ProductionQueue(null, null, null, null, null);
        assertNull(nullQueue.getOrderId());
        assertNull(nullQueue.getCustomerName());
        assertNull(nullQueue.getStatus());
        assertNull(nullQueue.getQueuePosition());
        assertNull(nullQueue.getReceivedAt());
    }

    @Test
    void shouldHandleEmptyItemsList() {
        List<ProductionItem> emptyItems = new ArrayList<>();
        productionQueue.setItems(emptyItems);
        assertTrue(productionQueue.getItems().isEmpty());
    }

    @Test
    void shouldHandleNullItemsList() {
        productionQueue.setItems(null);
        assertNull(productionQueue.getItems());
    }

    @Test
    void shouldHandleZeroQueuePosition() {
        productionQueue.setQueuePosition(0);
        assertEquals(0, productionQueue.getQueuePosition());
    }

    @Test
    void shouldHandleNegativeQueuePosition() {
        productionQueue.setQueuePosition(-1);
        assertEquals(-1, productionQueue.getQueuePosition());
    }
}