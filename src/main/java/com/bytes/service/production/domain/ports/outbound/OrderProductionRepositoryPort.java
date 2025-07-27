package com.bytes.service.production.domain.ports.outbound;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionStatus;

import java.util.List;
import java.util.Optional;

public interface OrderProductionRepositoryPort {
    Integer countByStatusIn(List<ProductionStatus> statuses);

    boolean existsByOrderId(Long orderId);

    void save(OrderProduction production);

    List<OrderProduction> findByStatusIn(List<Object> objects);

    Optional<OrderProduction> findByOrderId(Long orderId);

    List<OrderProduction> findByStatusOrderByStartedAtAsc(ProductionStatus productionStatus);
}
