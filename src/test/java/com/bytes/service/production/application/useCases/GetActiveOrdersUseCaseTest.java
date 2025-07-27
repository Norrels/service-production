package com.bytes.service.production.application.useCases;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionQueue;
import com.bytes.service.production.domain.models.ProductionStatus;
import com.bytes.service.production.domain.ports.outbound.OrderProductionRepositoryPort;
import com.bytes.service.production.mappers.ProductionOrderMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetActiveOrdersUseCaseTest {

    @Mock
    private ProductionOrderMapper orderMapper;

    @Mock
    private OrderProductionRepositoryPort repository;

    private GetActiveOrdersUseCase getActiveOrdersUseCase;

    @BeforeEach
    void setUp() {
        getActiveOrdersUseCase = new GetActiveOrdersUseCase(orderMapper, repository);
    }

    @Test
    void shouldGetActiveOrdersSortedByPosition() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order1 = new OrderProduction(1L, ProductionStatus.RECEIVED, testTime, 2, "John Doe");
        OrderProduction order2 = new OrderProduction(2L, ProductionStatus.IN_PREPARATION, testTime, 1, "Jane Smith");
        List<OrderProduction> orderProductions = Arrays.asList(order1, order2);

        ProductionQueue queue1 = new ProductionQueue(1L, "John Doe", ProductionStatus.RECEIVED, 2, testTime);
        ProductionQueue queue2 = new ProductionQueue(2L, "Jane Smith", ProductionStatus.IN_PREPARATION, 1, testTime);

        when(repository.findByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION))))
                .thenReturn(orderProductions);
        when(orderMapper.toProductionQueueDTO(eq(order1))).thenReturn(queue1);
        when(orderMapper.toProductionQueueDTO(eq(order2))).thenReturn(queue2);

        List<ProductionQueue> result = getActiveOrdersUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get(0).getOrderId());
        assertEquals(1L, result.get(1).getOrderId());

        verify(repository).findByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION)));
        verify(orderMapper).toProductionQueueDTO(eq(order1));
        verify(orderMapper).toProductionQueueDTO(eq(order2));
    }

    @Test
    void shouldReturnEmptyListWhenNoActiveOrders() {
        when(repository.findByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION))))
                .thenReturn(Arrays.asList());

        List<ProductionQueue> result = getActiveOrdersUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(repository).findByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION)));
        verify(orderMapper, never()).toProductionQueueDTO(any());
    }

    @Test
    void shouldHandleSingleActiveOrder() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order = new OrderProduction(1L, ProductionStatus.RECEIVED, testTime, 1, "John Doe");
        ProductionQueue queue = new ProductionQueue(1L, "John Doe", ProductionStatus.RECEIVED, 1, testTime);

        when(repository.findByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION))))
                .thenReturn(Arrays.asList(order));
        when(orderMapper.toProductionQueueDTO(eq(order))).thenReturn(queue);

        List<ProductionQueue> result = getActiveOrdersUseCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getOrderId());
        assertEquals("John Doe", result.get(0).getCustomerName());
        assertEquals(ProductionStatus.RECEIVED, result.get(0).getStatus());

        verify(repository).findByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION)));
        verify(orderMapper).toProductionQueueDTO(eq(order));
    }

    @Test
    void shouldSortOrdersByQueuePositionCorrectly() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderProduction order1 = new OrderProduction(1L, ProductionStatus.RECEIVED, testTime, 3, "Alice");
        OrderProduction order2 = new OrderProduction(2L, ProductionStatus.IN_PREPARATION, testTime, 1, "Bob");
        OrderProduction order3 = new OrderProduction(3L, ProductionStatus.RECEIVED, testTime, 2, "Charlie");
        List<OrderProduction> orderProductions = Arrays.asList(order1, order2, order3);

        ProductionQueue queue1 = new ProductionQueue(1L, "Alice", ProductionStatus.RECEIVED, 3, testTime);
        ProductionQueue queue2 = new ProductionQueue(2L, "Bob", ProductionStatus.IN_PREPARATION, 1, testTime);
        ProductionQueue queue3 = new ProductionQueue(3L, "Charlie", ProductionStatus.RECEIVED, 2, testTime);

        when(repository.findByStatusIn(any())).thenReturn(orderProductions);
        when(orderMapper.toProductionQueueDTO(eq(order1))).thenReturn(queue1);
        when(orderMapper.toProductionQueueDTO(eq(order2))).thenReturn(queue2);
        when(orderMapper.toProductionQueueDTO(eq(order3))).thenReturn(queue3);

        List<ProductionQueue> result = getActiveOrdersUseCase.execute();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(2L, result.get(0).getOrderId());
        assertEquals(3L, result.get(1).getOrderId());
        assertEquals(1L, result.get(2).getOrderId());
    }

    @Test
    void shouldConstructUseCaseWithDependencies() {
        GetActiveOrdersUseCase useCase = new GetActiveOrdersUseCase(orderMapper, repository);
        assertNotNull(useCase);
    }

    @Test
    void shouldFilterOnlyReceivedAndInPreparationStatuses() {
        getActiveOrdersUseCase.execute();

        verify(repository).findByStatusIn(eq(List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION)));
    }
}