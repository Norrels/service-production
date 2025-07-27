package com.bytes.service.production.application;

import com.bytes.service.production.adapters.inbound.dtos.OrderStatusDTO;
import com.bytes.service.production.application.useCases.GetActiveOrdersUseCase;
import com.bytes.service.production.application.useCases.GetOrderByIdUseCase;
import com.bytes.service.production.application.useCases.StartOrderUseCase;
import com.bytes.service.production.application.useCases.UpdateOrderStatusUseCase;
import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionQueue;
import com.bytes.service.production.domain.models.ProductionStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductionServiceTest {

    @Mock
    private StartOrderUseCase startOrderUseCase;

    @Mock
    private GetOrderByIdUseCase getOrderByIdUseCase;

    @Mock
    private GetActiveOrdersUseCase getActiveOrdersUseCase;

    @Mock
    private UpdateOrderStatusUseCase updateOrderStatusUseCase;

    private ProductionService productionService;

    @BeforeEach
    void setUp() {
        productionService = new ProductionService(
                startOrderUseCase, 
                getOrderByIdUseCase, 
                getActiveOrdersUseCase, 
                updateOrderStatusUseCase
        );
    }

    @Test
    void shouldGetActiveOrders() {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        List<ProductionQueue> expectedOrders = Arrays.asList(
            new ProductionQueue(1L, "John Doe", ProductionStatus.RECEIVED, 1, testTime),
            new ProductionQueue(2L, "Jane Smith", ProductionStatus.IN_PREPARATION, 2, testTime)
        );

        when(getActiveOrdersUseCase.execute()).thenReturn(expectedOrders);

        List<ProductionQueue> result = productionService.getActiveOrders();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedOrders, result);
        verify(getActiveOrdersUseCase).execute();
    }

    @Test
    void shouldGetActiveOrdersWhenEmpty() {
        when(getActiveOrdersUseCase.execute()).thenReturn(Arrays.asList());

        List<ProductionQueue> result = productionService.getActiveOrders();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(getActiveOrdersUseCase).execute();
    }

    @Test
    void shouldGetOrderStatusWithUpdatedAt() {
        Long orderId = 123L;
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 1, 11, 0);

        OrderProduction orderProduction = new OrderProduction(
                orderId, ProductionStatus.IN_PREPARATION, startedAt, 2, "John Doe"
        );
        orderProduction.setId(1L);
        orderProduction.setUpdatedAt(updatedAt);

        when(getOrderByIdUseCase.execute(eq(orderId))).thenReturn(orderProduction);

        OrderStatusDTO result = productionService.getOrderStatus(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals(ProductionStatus.IN_PREPARATION, result.getCurrentStatus());
        assertEquals("Em Preparação", result.getStatusDescription());
        assertEquals(2, result.getQueuePosition());
        assertEquals(updatedAt, result.getLastUpdate());

        verify(getOrderByIdUseCase).execute(eq(orderId));
    }

    @Test
    void shouldGetOrderStatusWithStartedAtWhenUpdatedAtIsNull() {
        Long orderId = 123L;
        LocalDateTime startedAt = LocalDateTime.of(2024, 1, 1, 10, 0);

        OrderProduction orderProduction = new OrderProduction(
                orderId, ProductionStatus.RECEIVED, startedAt, 1, "Jane Smith"
        );
        orderProduction.setId(1L);

        when(getOrderByIdUseCase.execute(eq(orderId))).thenReturn(orderProduction);

        OrderStatusDTO result = productionService.getOrderStatus(orderId);

        assertNotNull(result);
        assertEquals(orderId, result.getOrderId());
        assertEquals(ProductionStatus.RECEIVED, result.getCurrentStatus());
        assertEquals("Recebido", result.getStatusDescription());
        assertEquals(1, result.getQueuePosition());
        assertEquals(startedAt, result.getLastUpdate());

        verify(getOrderByIdUseCase).execute(eq(orderId));
    }

    @Test
    void shouldStartProduction() {
        Long orderId = 456L;
        String customerName = "Alice Johnson";

        doNothing().when(startOrderUseCase).execute(eq(orderId), eq(customerName));

        productionService.startProduction(orderId, customerName);

        verify(startOrderUseCase).execute(eq(orderId), eq(customerName));
    }

    @Test
    void shouldUpdateStatus() {
        Long orderId = 789L;
        ProductionStatus newStatus = ProductionStatus.READY;

        doNothing().when(updateOrderStatusUseCase).execute(eq(orderId), eq(newStatus));

        productionService.updateStatus(orderId, newStatus);

        verify(updateOrderStatusUseCase).execute(eq(orderId), eq(newStatus));
    }

    @Test
    void shouldReturnCorrectStatusDescriptions() {
        assertEquals("Aguardando Pagamento", ProductionStatus.WAITING_PAYMENT.getDescription());
        assertEquals("Recebido", ProductionStatus.RECEIVED.getDescription());
        assertEquals("Em Preparação", ProductionStatus.IN_PREPARATION.getDescription());
        assertEquals("Pronto", ProductionStatus.READY.getDescription());
        assertEquals("Finalizado", ProductionStatus.FINISHED.getDescription());
        assertEquals("Cancelado", ProductionStatus.CANCELLED.getDescription());
    }

    @Test
    void shouldHandleAllProductionStatuses() {
        Long orderId = 100L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);

        for (ProductionStatus status : ProductionStatus.values()) {
            OrderProduction orderProduction = new OrderProduction(
                    orderId, status, testTime, 1, "Test Customer"
            );
            orderProduction.setId(1L);

            when(getOrderByIdUseCase.execute(eq(orderId))).thenReturn(orderProduction);

            OrderStatusDTO result = productionService.getOrderStatus(orderId);

            assertNotNull(result);
            assertEquals(status, result.getCurrentStatus());
            assertEquals(status.getDescription(), result.getStatusDescription());
        }

        verify(getOrderByIdUseCase, times(ProductionStatus.values().length)).execute(eq(orderId));
    }

    @Test
    void shouldConstructServiceWithAllDependencies() {
        ProductionService service = new ProductionService(
                startOrderUseCase, 
                getOrderByIdUseCase, 
                getActiveOrdersUseCase, 
                updateOrderStatusUseCase
        );
        
        assertNotNull(service);
    }
}