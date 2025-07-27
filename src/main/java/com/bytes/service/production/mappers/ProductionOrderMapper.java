package com.bytes.service.production.mappers;

import com.bytes.service.production.domain.models.OrderProduction;
import com.bytes.service.production.domain.models.ProductionQueue;

public class ProductionOrderMapper {

    public ProductionQueue toProductionQueueDTO(OrderProduction orderProduction) {
        return new ProductionQueue(orderProduction.getOrderId(), orderProduction.getCustomerName(), orderProduction.getStatus(), orderProduction.getPositionInQueue(), orderProduction.getStartedAt()
        );
    }
}
