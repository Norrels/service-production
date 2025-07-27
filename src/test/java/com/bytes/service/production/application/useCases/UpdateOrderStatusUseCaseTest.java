package com.bytes.service.production.application.useCases;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionStatus;
import com.bytes.service.production.domain.ports.outbound.OrderProductionRepositoryPort;
import com.bytes.service.production.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateOrderStatusUseCaseTest {

    @Mock
    private OrderProductionRepositoryPort repository;

    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @BeforeEach
    void setUp() {
        updateOrderStatusUseCase = new UpdateOrderStatusUseCase(repository);
    }

    @Test
    void shouldUpdateStatusFromReceivedToInPreparation() {
        Long orderId = 123L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.RECEIVED, testTime, 1, "John Doe"
        );

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));
        when(repository.findByStatusOrderByStartedAtAsc(eq(ProductionStatus.RECEIVED)))
                .thenReturn(Arrays.asList());

        updateOrderStatusUseCase.execute(orderId, ProductionStatus.IN_PREPARATION);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository, atLeast(1)).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertEquals(ProductionStatus.IN_PREPARATION, savedOrder.getStatus());
        assertNotNull(savedOrder.getUpdatedAt());
        assertNotNull(savedOrder.getStartedAt());

        verify(repository).findByOrderId(eq(orderId));
    }

    @Test
    void shouldUpdateStatusFromInPreparationToReady() {
        Long orderId = 456L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.IN_PREPARATION, testTime, 1, "Jane Smith"
        );

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));

        updateOrderStatusUseCase.execute(orderId, ProductionStatus.READY);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertEquals(ProductionStatus.READY, savedOrder.getStatus());
        assertNotNull(savedOrder.getUpdatedAt());
        assertNotNull(savedOrder.getFinishedAt());
        assertNull(savedOrder.getPositionInQueue());

        verify(repository).findByOrderId(eq(orderId));
    }

    @Test
    void shouldUpdateStatusFromReadyToFinished() {
        Long orderId = 789L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.READY, testTime, null, "Alice Johnson"
        );

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));

        updateOrderStatusUseCase.execute(orderId, ProductionStatus.FINISHED);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertEquals(ProductionStatus.FINISHED, savedOrder.getStatus());
        assertNotNull(savedOrder.getUpdatedAt());
        assertNotNull(savedOrder.getDeliveredAt());

        verify(repository).findByOrderId(eq(orderId));
    }

    @Test
    void shouldThrowExceptionWhenOrderNotFound() {
        Long orderId = 999L;

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> updateOrderStatusUseCase.execute(orderId, ProductionStatus.IN_PREPARATION)
        );

        assertEquals("Order not found: 999", exception.getMessage());
        verify(repository).findByOrderId(eq(orderId));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldThrowExceptionForInvalidStatusTransition() {
        Long orderId = 111L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.FINISHED, testTime, null, "Bob Wilson"
        );

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> updateOrderStatusUseCase.execute(orderId, ProductionStatus.IN_PREPARATION)
        );

        assertEquals("Cannot transition from FINISHED to IN_PREPARATION", exception.getMessage());
        verify(repository).findByOrderId(eq(orderId));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldAllowCancellationFromReceivedStatus() {
        Long orderId = 222L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.RECEIVED, testTime, 1, "Charlie Brown"
        );

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));

        updateOrderStatusUseCase.execute(orderId, ProductionStatus.CANCELLED);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertEquals(ProductionStatus.CANCELLED, savedOrder.getStatus());
        assertNotNull(savedOrder.getUpdatedAt());

        verify(repository).findByOrderId(eq(orderId));
    }

    @Test
    void shouldAllowCancellationFromInPreparationStatus() {
        Long orderId = 333L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.IN_PREPARATION, testTime, 1, "Diana Prince"
        );

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));

        updateOrderStatusUseCase.execute(orderId, ProductionStatus.CANCELLED);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertEquals(ProductionStatus.CANCELLED, savedOrder.getStatus());

        verify(repository).findByOrderId(eq(orderId));
    }

    @Test
    void shouldRecalculateQueuePositionsWhenMovingToInPreparation() {
        Long orderId = 444L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.RECEIVED, testTime, 2, "Edward Stark"
        );
        
        OrderProduction queuedOrder1 = new OrderProduction(
                555L, ProductionStatus.RECEIVED, testTime, 1, "Customer 1"
        );
        OrderProduction queuedOrder2 = new OrderProduction(
                666L, ProductionStatus.RECEIVED, testTime, 3, "Customer 2"
        );

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));
        when(repository.findByStatusOrderByStartedAtAsc(eq(ProductionStatus.RECEIVED)))
                .thenReturn(Arrays.asList(queuedOrder1, queuedOrder2));

        updateOrderStatusUseCase.execute(orderId, ProductionStatus.IN_PREPARATION);

        verify(repository).findByStatusOrderByStartedAtAsc(eq(ProductionStatus.RECEIVED));
        verify(repository, times(3)).save(any(OrderProduction.class));
        
        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository, times(3)).save(orderCaptor.capture());
        
        List<OrderProduction> savedOrders = orderCaptor.getAllValues();
        assertTrue(savedOrders.stream().anyMatch(o -> o.getOrderId().equals(555L) && o.getPositionInQueue() == 1));
        assertTrue(savedOrders.stream().anyMatch(o -> o.getOrderId().equals(666L) && o.getPositionInQueue() == 2));
    }

    @Test
    void shouldNotAllowInvalidTransitions() {
        Long orderId = 777L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.CANCELLED, testTime, null, "Test Customer"
        );

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> updateOrderStatusUseCase.execute(orderId, ProductionStatus.RECEIVED)
        );

        assertTrue(exception.getMessage().contains("Cannot transition from CANCELLED"));
        verify(repository, never()).save(any());
    }

    @Test
    void shouldConstructUseCaseWithRepository() {
        UpdateOrderStatusUseCase useCase = new UpdateOrderStatusUseCase(repository);
        assertNotNull(useCase);
    }

    @Test
    void shouldUpdateTimestampOnEveryStatusChange() {
        Long orderId = 888L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(
                orderId, ProductionStatus.RECEIVED, testTime, 1, "Time Test Customer"
        );

        when(repository.findByOrderId(eq(orderId))).thenReturn(Optional.of(order));
        when(repository.findByStatusOrderByStartedAtAsc(any())).thenReturn(Arrays.asList());

        updateOrderStatusUseCase.execute(orderId, ProductionStatus.IN_PREPARATION);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository, atLeast(1)).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertNotNull(savedOrder.getUpdatedAt());
        assertTrue(savedOrder.getUpdatedAt().isAfter(testTime));
    }
}