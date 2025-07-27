package com.bytes.service.production.application.useCases;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionStatus;
import com.bytes.service.production.domain.ports.outbound.OrderProductionRepositoryPort;

import java.time.LocalDateTime;
import java.util.List;

public class StartOrderUseCase {

    private final OrderProductionRepositoryPort repository;

    public StartOrderUseCase(OrderProductionRepositoryPort repository) {
        this.repository = repository;
    }

    public void execute(Long orderId, String customerName) {
        if (repository.existsByOrderId(orderId)) {
            return;
        }

        Integer position = repository.countByStatusIn(
                List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION)
        ) + 1;

        OrderProduction production = new OrderProduction(
                orderId,
                ProductionStatus.RECEIVED,
                LocalDateTime.now(),
                position,
                customerName
        );

        repository.save(production);
    }
}
