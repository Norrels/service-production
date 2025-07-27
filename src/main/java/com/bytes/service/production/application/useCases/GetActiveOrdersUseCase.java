package com.bytes.service.production.application.useCases;

import com.bytes.service.production.domain.models.ProductionQueue;
import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionStatus;
import com.bytes.service.production.domain.ports.outbound.OrderProductionRepositoryPort;
import com.bytes.service.production.mappers.ProductionOrderMapper;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetActiveOrdersUseCase {

    private final ProductionOrderMapper orderMappper;

    private final OrderProductionRepositoryPort repository;

    public GetActiveOrdersUseCase(ProductionOrderMapper orderMappper, OrderProductionRepositoryPort repository) {
        this.orderMappper = orderMappper;
        this.repository = repository;
    }

    public List<ProductionQueue> execute() {
        List<OrderProduction> activeOrders = repository.findByStatusIn(
                List.of(ProductionStatus.RECEIVED, ProductionStatus.IN_PREPARATION)
        );

        return activeOrders.stream()
                .map(orderMappper::toProductionQueueDTO)
                .sorted(Comparator.comparing(ProductionQueue::getQueuePosition))
                .collect(Collectors.toList());
    }
}
