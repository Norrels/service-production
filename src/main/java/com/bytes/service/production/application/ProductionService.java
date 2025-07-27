package com.bytes.service.production.application;

import com.bytes.service.production.adapters.inbound.dtos.OrderStatusDTO;
import com.bytes.service.production.domain.models.ProductionQueue;
import com.bytes.service.production.application.useCases.GetActiveOrdersUseCase;
import com.bytes.service.production.application.useCases.GetOrderByIdUseCase;
import com.bytes.service.production.application.useCases.StartOrderUseCase;
import com.bytes.service.production.application.useCases.UpdateOrderStatusUseCase;
import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionStatus;
import com.bytes.service.production.domain.ports.inbound.ProductionServicePort;

import java.util.List;

public class ProductionService implements ProductionServicePort {

    private final StartOrderUseCase startOrderUseCase;
    private final GetOrderByIdUseCase getOrderByIdUseCase;
    private final GetActiveOrdersUseCase getActiveOrdersUseCase;

    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    public ProductionService(StartOrderUseCase startOrderUseCase, GetOrderByIdUseCase getOrderByIdUseCase, GetActiveOrdersUseCase getActiveOrdersUseCase, UpdateOrderStatusUseCase updateOrderStatusUseCase) {
        this.startOrderUseCase = startOrderUseCase;
        this.getOrderByIdUseCase = getOrderByIdUseCase;
        this.getActiveOrdersUseCase = getActiveOrdersUseCase;
        this.updateOrderStatusUseCase = updateOrderStatusUseCase;
    }

    @Override
    public List<ProductionQueue> getActiveOrders() {
      return getActiveOrdersUseCase.execute();
    }

    @Override
    public OrderStatusDTO getOrderStatus(Long orderId) {
        OrderProduction orderProduction = getOrderByIdUseCase.execute(orderId);

        return OrderStatusDTO.builder()
                .orderId(orderProduction.getOrderId())
                .currentStatus(orderProduction.getStatus())
                .statusDescription(getStatusDescription(orderProduction.getStatus()))
                .queuePosition(orderProduction.getPositionInQueue())
                .lastUpdate(orderProduction.getUpdatedAt() != null ? orderProduction.getUpdatedAt() : orderProduction.getStartedAt())
                .build();
    }

    @Override
    public void startProduction(Long orderId, String customerName) {
        startOrderUseCase.execute(orderId, customerName);
    }

    @Override
    public void updateStatus(Long orderId, ProductionStatus newStatus) {

        updateOrderStatusUseCase.execute(orderId, newStatus);
    }

    private String getStatusDescription(ProductionStatus status) {
        return status.getDescription();
    }
}
