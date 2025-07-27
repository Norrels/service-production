package com.bytes.service.production.domain.ports.inbound;

import com.bytes.service.production.adapters.inbound.dtos.OrderStatusDTO;
import com.bytes.service.production.domain.models.ProductionQueue;
import com.bytes.service.production.domain.models.ProductionStatus;

import java.util.List;

public interface ProductionServicePort {
    void startProduction(Long orderId, String customerName);
    void updateStatus(Long orderId, ProductionStatus newStatus);

    OrderStatusDTO getOrderStatus(Long orderId);

    List<ProductionQueue> getActiveOrders();
}