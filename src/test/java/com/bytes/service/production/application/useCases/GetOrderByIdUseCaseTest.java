package com.bytes.service.production.application.useCases;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionStatus;
import com.bytes.service.production.domain.ports.outbound.OrderProductionRepositoryPort;
import com.bytes.service.production.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetOrderByIdUseCaseTest {

    @Mock
    private OrderProductionRepositoryPort repository;

    private GetOrderByIdUseCase getOrderByIdUseCase;

    @BeforeEach
    void setUp() {
        getOrderByIdUseCase = new GetOrderByIdUseCase(repository);
    }

    @Test
    void shouldReturnOrderWhenFound() {
        Long orderId = 123L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction expectedOrder = new OrderProduction(
                orderId, ProductionStatus.RECEIVED, testTime, 1, "John Doe"
        );
        expectedOrder.setId(1L);

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(expectedOrder));

        OrderProduction result = getOrderByIdUseCase.execute(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals(ProductionStatus.RECEIVED, result.getStatus());
        assertEquals("John Doe", result.getCustomerName());
        assertEquals(1, result.getPositionInQueue());
        assertEquals(testTime, result.getStartedAt());

        verify(repository).findByOrderId(eq(orderId));
    }

    @Test
    void shouldThrowResourceNotFoundExceptionWhenOrderNotFound() {
        Long orderId = 999L;

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> getOrderByIdUseCase.execute(orderId)
        );

        assertEquals("Order with ID 999 not found", exception.getMessage());
        verify(repository).findByOrderId(eq(orderId));
    }

    @Test
    void shouldHandleNullOrderId() {
        when(repository.findByOrderId(eq(null))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> getOrderByIdUseCase.execute(null)
        );

        assertEquals("Order with ID null not found", exception.getMessage());
        verify(repository).findByOrderId(eq(null));
    }

    @Test
    void shouldReturnOrderWithAllStatuses() {
        Long orderId = 100L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);

        for (ProductionStatus status : ProductionStatus.values()) {
            OrderProduction order = new OrderProduction(
                    orderId, status, testTime, 1, "Test Customer"
            );
            order.setId(1L);

            when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));

            OrderProduction result = getOrderByIdUseCase.execute(orderId);

            assertNotNull(result);
            assertEquals(status, result.getStatus());
        }

        verify(repository, times(ProductionStatus.values().length)).findByOrderId(eq(orderId));
    }

    @Test
    void shouldReturnOrderWithCompleteData() {
        Long orderId = 456L;
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 11, 0);
        LocalDateTime finishedAt = LocalDateTime.of(2024, 1, 1, 12, 0);
        LocalDateTime deliveredAt = LocalDateTime.of(2024, 1, 1, 13, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.FINISHED, startedAt, 1, "Jane Doe"
        );
        order.setId(10L);
        order.setUpdatedAt(updatedAt);
        order.setFinishedAt(finishedAt);
        order.setDeliveredAt(deliveredAt);

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));

        OrderProduction result = getOrderByIdUseCase.execute(orderId);

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(orderId, result.getOrderId());
        assertEquals(ProductionStatus.FINISHED, result.getStatus());
        assertEquals(startedAt, result.getStartedAt());
        assertEquals(updatedAt, result.getUpdatedAt());
        assertEquals(finishedAt, result.getFinishedAt());
        assertEquals(deliveredAt, result.getDeliveredAt());
        assertEquals("Jane Doe", result.getCustomerName());
        assertEquals(1, result.getPositionInQueue());

        verify(repository).findByOrderId(eq(orderId));
    }

    @Test
    void shouldConstructUseCaseWithRepository() {
        GetOrderByIdUseCase useCase = new GetOrderByIdUseCase(repository);
        assertNotNull(useCase);
    }
}