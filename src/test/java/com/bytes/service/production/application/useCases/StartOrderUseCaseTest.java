package com.bytes.service.production.application.useCases;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionStatus;
import com.bytes.service.production.domain.ports.outbound.OrderProductionRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StartOrderUseCaseTest {

    @Mock
    private OrderProductionRepositoryPort repository;

    private StartOrderUseCase startOrderUseCase;

    @BeforeEach
    void setUp() {
        startOrderUseCase = new StartOrderUseCase(repository);
    }

    @Test
    void shouldCreateNewOrderWhenOrderDoesNotExist() {
        Long orderId = 123L;
        String customerName = "John Doe";
        
        when(repository.existsByOrderId(eq(orderId))).thenReturn(false);
        when(repository.countByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION))))
                .thenReturn(2);

        startOrderUseCase.execute(orderId, customerName);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertNotNull(savedOrder);
        assertEquals(orderId, savedOrder.getOrderId());
        assertEquals(customerName, savedOrder.getCustomerName());
        assertEquals(ProductionStatus.RECEIVED, savedOrder.getStatus());
        assertEquals(3, savedOrder.getPositionInQueue());
        assertNotNull(savedOrder.getStartedAt());
        assertTrue(savedOrder.getStartedAt().isBefore(LocalDateTime.now().plusSeconds(1)));

        verify(repository).existsByOrderId(eq(orderId));
        verify(repository).countByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION)));
    }

    @Test
    void shouldNotCreateOrderWhenOrderAlreadyExists() {
        Long orderId = 456L;
        String customerName = "Jane Smith";

        when(repository.existsByOrderId(eq(orderId))).thenReturn(true);

        startOrderUseCase.execute(orderId, customerName);

        verify(repository).existsByOrderId(eq(orderId));
        verify(repository, never()).countByStatusIn(any());
        verify(repository, never()).save(any());
    }

    @Test
    void shouldSetCorrectPositionWhenNoActiveOrders() {
        Long orderId = 789L;
        String customerName = "Alice Johnson";

        when(repository.existsByOrderId(eq(orderId))).thenReturn(false);
        when(repository.countByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION))))
                .thenReturn(0);

        startOrderUseCase.execute(orderId, customerName);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertEquals(1, savedOrder.getPositionInQueue());
    }

    @Test
    void shouldSetCorrectPositionWhenMultipleActiveOrders() {
        Long orderId = 999L;
        String customerName = "Bob Wilson";

        when(repository.existsByOrderId(eq(orderId))).thenReturn(false);
        when(repository.countByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION))))
                .thenReturn(5);

        startOrderUseCase.execute(orderId, customerName);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertEquals(6, savedOrder.getPositionInQueue());
    }

    @Test
    void shouldHandleNullCustomerName() {
        Long orderId = 111L;
        String customerName = null;

        when(repository.existsByOrderId(eq(orderId))).thenReturn(false);
        when(repository.countByStatusIn(any())).thenReturn(0);

        startOrderUseCase.execute(orderId, customerName);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertNull(savedOrder.getCustomerName());
    }

    @Test
    void shouldHandleNullOrderId() {
        Long orderId = null;
        String customerName = "Test Customer";

        when(repository.existsByOrderId(eq(orderId))).thenReturn(false);
        when(repository.countByStatusIn(any())).thenReturn(0);

        startOrderUseCase.execute(orderId, customerName);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertNull(savedOrder.getOrderId());
    }

    @Test
    void shouldSetReceivedStatusForNewOrder() {
        Long orderId = 222L;
        String customerName = "Charlie Brown";

        when(repository.existsByOrderId(eq(orderId))).thenReturn(false);
        when(repository.countByStatusIn(any())).thenReturn(0);

        startOrderUseCase.execute(orderId, customerName);

        ArgumentCaptor<OrderProduction> orderCaptor = ArgumentCaptor.forClass(OrderProduction.class);
        verify(repository).save(orderCaptor.capture());

        OrderProduction savedOrder = orderCaptor.getValue();
        assertEquals(ProductionStatus.RECEIVED, savedOrder.getStatus());
    }

    @Test
    void shouldOnlyCountReceivedAndInPreparationOrders() {
        Long orderId = 333L;
        String customerName = "Diana Prince";

        when(repository.existsByOrderId(eq(orderId))).thenReturn(false);

        startOrderUseCase.execute(orderId, customerName);

        verify(repository).countByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION)));
    }

    @Test
    void shouldConstructUseCaseWithRepository() {
        StartOrderUseCase useCase = new StartOrderUseCase(repository);
        assertNotNull(useCase);
    }
}