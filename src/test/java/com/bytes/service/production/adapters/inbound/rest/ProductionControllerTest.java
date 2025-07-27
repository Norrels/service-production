package com.bytes.service.production.adapters.inbound.rest;

import com.bytes.service.production.adapters.inbound.dtos.OrderStatusDTO;
import com.bytes.service.production.application.ProductionService;
import com.bytes.service.production.domain.models.ProductionQueue;
import com.bytes.service.production.domain.models.ProductionStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductionControllerTest {

    @Mock
    private ProductionService productionService;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private ProductionController productionController;

    @BeforeEach
    void setUp() {
        productionController = new ProductionController(productionService);
        mockMvc = MockMvcBuilders.standaloneSetup(productionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void shouldGetProductionQueue() throws Exception {
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        List<ProductionQueue> mockQueue = Arrays.asList(
            new ProductionQueue(1L, "John Doe", ProductionStatus.RECEIVED, 1, testTime),
            new ProductionQueue(2L, "Jane Smith", ProductionStatus.IN_PREPARATION, 2, testTime)
        );

        when(productionService.getActiveOrders()).thenReturn(mockQueue);

        mockMvc.perform(get("/api/v1/production/queue"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].orderId").value(1))
                .andExpect(jsonPath("$[0].customerName").value("John Doe"))
                .andExpect(jsonPath("$[0].status").value("RECEIVED"))
                .andExpect(jsonPath("$[1].orderId").value(2))
                .andExpect(jsonPath("$[1].customerName").value("Jane Smith"))
                .andExpect(jsonPath("$[1].status").value("IN_PREPARATION"));

        verify(productionService).getActiveOrders();
    }

    @Test
    void shouldGetProductionQueueWhenEmpty() throws Exception {
        when(productionService.getActiveOrders()).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/v1/production/queue"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));

        verify(productionService).getActiveOrders();
    }

    @Test
    void shouldUpdateOrderStatus() throws Exception {
        Long orderId = 123L;
        ProductionStatus newStatus = ProductionStatus.IN_PREPARATION;

        doNothing().when(productionService).updateStatus(eq(orderId), eq(newStatus));

        mockMvc.perform(put("/api/v1/production/{orderId}/status", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"IN_PREPARATION\""))
                .andExpect(status().isOk());

        verify(productionService).updateStatus(eq(orderId), eq(newStatus));
    }

    @Test
    void shouldGetOrderStatus() throws Exception {
        Long orderId = 123L;
        LocalDateTime testTime = LocalDateTime.of(2024, 1, 1, 10, 0);
        
        OrderStatusDTO mockStatus = OrderStatusDTO.builder()
                .orderId(orderId)
                .currentStatus(ProductionStatus.IN_PREPARATION)
                .statusDescription("Em Preparação")
                .queuePosition(2)
                .lastUpdate(testTime)
                .build();

        when(productionService.getOrderStatus(eq(orderId))).thenReturn(mockStatus);

        mockMvc.perform(get("/api/v1/production/order/{orderId}/status", orderId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.orderId").value(orderId))
                .andExpect(jsonPath("$.currentStatus").value("IN_PREPARATION"))
                .andExpect(jsonPath("$.statusDescription").value("Em Preparação"))
                .andExpect(jsonPath("$.queuePosition").value(2));

        verify(productionService).getOrderStatus(eq(orderId));
    }

    @Test
    void shouldHandleInvalidOrderIdInPath() throws Exception {
        mockMvc.perform(get("/api/v1/production/order/invalid/status"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleInvalidStatusUpdate() throws Exception {
        Long orderId = 123L;

        mockMvc.perform(put("/api/v1/production/{orderId}/status", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"INVALID_STATUS\""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleEmptyRequestBody() throws Exception {
        Long orderId = 123L;

        mockMvc.perform(put("/api/v1/production/{orderId}/status", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldHandleNullRequestBody() throws Exception {
        Long orderId = 123L;

        mockMvc.perform(put("/api/v1/production/{orderId}/status", orderId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldConstructControllerWithService() {
        ProductionController controller = new ProductionController(productionService);
        assertNotNull(controller);
    }
}