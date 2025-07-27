package com.bytes.service.production.mappers;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionQueue;
import com.bytes.service.production.domain.models.ProductionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProductionOrderMapperTest {

    private ProductionOrderMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ProductionOrderMapper();
    }

    @Test
    void shouldMapOrderProductionToProductionQueue() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction orderProduction = new OrderProduction(
                123L, ProductionStatus.RECEIVED, testTime, 1, "John Doe"
        );
        orderProduction.setId(10L);

        ProductionQueue result = mapper.toProductionQueueDTO(orderProduction);

        assertNotNull(result);
        assertEquals(123L, result.getOrderId());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(ProductionStatus.RECEIVED, result.getStatus());
        assertEquals(1, result.getQueuePosition());
        assertEquals(testTime, result.getReceivedAt());
    }

    @Test
    void shouldMapWithInPreparationStatus() {
        LocalDateTime testTime = LocalDateTime.of(2024, 2, 1, 14, 30);
        
        OrderProduction orderProduction = new OrderProduction(
                456L, ProductionStatus.IN_PREPARATION, testTime, 2, "Jane Smith"
        );

        ProductionQueue result = mapper.toProductionQueueDTO(orderProduction);

        assertNotNull(result);
        assertEquals(456L, result.getOrderId());
        assertEquals("Jane Smith", result.getCustomerName());
        assertEquals(ProductionStatus.IN_PREPARATION, result.getStatus());
        assertEquals(2, result.getQueuePosition());
        assertEquals(testTime, result.getReceivedAt());
    }

    @Test
    void shouldMapWithNullValues() {
        OrderProduction orderProduction = new OrderProduction(
                null, null, null, null, null
        );

        ProductionQueue result = mapper.toProductionQueueDTO(orderProduction);

        assertNotNull(result);
        assertNull(result.getOrderId());
        assertNull(result.getCustomerName());
        assertNull(result.getStatus());
        assertNull(result.getQueuePosition());
        assertNull(result.getReceivedAt());
    }

    @Test
    void shouldMapWithAllProductionStatuses() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);

        for (ProductionStatus status : ProductionStatus.values()) {
            OrderProduction orderProduction = new OrderProduction(
                    100L, status, testTime, 1, "Test Customer"
            );

            ProductionQueue result = mapper.toProductionQueueDTO(orderProduction);

            assertNotNull(result);
            assertEquals(status, result.getStatus());
            assertEquals(100L, result.getOrderId());
            assertEquals("Test Customer", result.getCustomerName());
        }
    }

    @Test
    void shouldMapWithZeroQueuePosition() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction orderProduction = new OrderProduction(
                789L, ProductionStatus.READY, testTime, 0, "Alice Johnson"
        );

        ProductionQueue result = mapper.toProductionQueueDTO(orderProduction);

        assertNotNull(result);
        assertEquals(0, result.getQueuePosition());
    }

    @Test
    void shouldMapWithNegativeQueuePosition() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction orderProduction = new OrderProduction(
                999L, ProductionStatus.CANCELLED, testTime, -1, "Bob Wilson"
        );

        ProductionQueue result = mapper.toProductionQueueDTO(orderProduction);

        assertNotNull(result);
        assertEquals(-1, result.getQueuePosition());
    }

    @Test
    void shouldMapWithEmptyCustomerName() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction orderProduction = new OrderProduction(
                111L, ProductionStatus.RECEIVED, testTime, 1, ""
        );

        ProductionQueue result = mapper.toProductionQueueDTO(orderProduction);

        assertNotNull(result);
        assertEquals("", result.getCustomerName());
    }

    @Test
    void shouldPreserveAllFieldValues() {
        LocalDateTime specificTime = LocalDateTime.of(2024, 12, 25, 15, 30, 45);
        
        OrderProduction orderProduction = new OrderProduction(
                987654321L, ProductionStatus.FINISHED, specificTime, 999, "Very Long Customer Name With Special Characters !@#$%"
        );

        ProductionQueue result = mapper.toProductionQueueDTO(orderProduction);

        assertNotNull(result);
        assertEquals(987654321L, result.getOrderId());
        assertEquals("Very Long Customer Name With Special Characters !@#$%", result.getCustomerName());
        assertEquals(ProductionStatus.FINISHED, result.getStatus());
        assertEquals(999, result.getQueuePosition());
        assertEquals(specificTime, result.getReceivedAt());
    }

    @Test
    void shouldCreateMapperInstance() {
        ProductionOrderMapper mapper = new ProductionOrderMapper();
        assertNotNull(mapper);
    }
}