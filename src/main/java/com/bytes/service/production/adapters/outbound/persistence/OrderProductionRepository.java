package com.bytes.service.production.adapters.outbound.persistence;

import com.bytes.service.production.domain.models.ProductionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderProductionRepository extends JpaRepository<OrderProductionEntity, Long> {
    Integer countByStatusIn(List<ProductionStatus> received);

    boolean existsByOrderId(Long orderId);
}
