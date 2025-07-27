package com.bytes.service.production.application.useCases;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.ports.outbound.OrderProductionRepositoryPort;
import com.bytes.service.production.exceptions.ResourceNotFoundException;

public class GetOrderByIdUseCase {

    private final OrderProductionRepositoryPort repository;

    public GetOrderByIdUseCase(OrderProductionRepositoryPort repository) {
        this.repository = repository;
    }

    public OrderProduction execute(Long orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order with ID " + orderId + " not found"));
    }
}
