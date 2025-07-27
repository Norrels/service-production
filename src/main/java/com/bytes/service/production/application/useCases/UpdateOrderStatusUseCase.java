package com.bytes.service.production.application.useCases;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionStatus;
import com.bytes.service.production.domain.ports.outbound.OrderProductionRepositoryPort;
import com.bytes.service.production.exceptions.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class UpdateOrderStatusUseCase {

    private final OrderProductionRepositoryPort repository;


    public UpdateOrderStatusUseCase(OrderProductionRepositoryPort repository) {
        this.repository = repository;
    }


    public void execute(Long orderId, ProductionStatus newStatus){
        OrderProduction production = repository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        validateStatusTransition(production.getStatus(), newStatus);

        production.setStatus(newStatus);
        production.setUpdatedAt(LocalDateTime.now());

        switch (newStatus) {
            case IN_PREPARATION:
                production.setStartedAt(LocalDateTime.now());
                recalculateQueuePositions();
                break;
            case READY:
                production.setFinishedAt(LocalDateTime.now());
                production.setPositionInQueue(null);
                break;
            case FINISHED:
                production.setDeliveredAt(LocalDateTime.now());
                break;
        }

        repository.save(production);

        // TODO: Implementar lógica de notificação para o cliente
    }

    private void recalculateQueuePositions() {
        List<OrderProduction> queuedOrders = repository.findByStatusOrderByStartedAtAsc(
                ProductionStatus.RECEIVED
        );

        int position = 1;
        for (OrderProduction order : queuedOrders) {
            order.setPositionInQueue(position++);
            repository.save(order);
        }
    }

    private void validateStatusTransition(ProductionStatus current, ProductionStatus newStatus) {
        Map<ProductionStatus, List<ProductionStatus>> allowedTransitions = Map.of(
                ProductionStatus.RECEIVED, List.of(ProductionStatus.IN_PREPARATION, ProductionStatus.CANCELLED),
                ProductionStatus.IN_PREPARATION, List.of(ProductionStatus.READY, ProductionStatus.CANCELLED),
                ProductionStatus.READY, List.of(ProductionStatus.FINISHED),
                ProductionStatus.FINISHED, List.of(),
                ProductionStatus.CANCELLED, List.of()
        );

        if (!allowedTransitions.get(current).contains(newStatus)) {
            throw new RuntimeException(
                    String.format("Cannot transition from %s to %s", current, newStatus)
            );
        }
    }
}
