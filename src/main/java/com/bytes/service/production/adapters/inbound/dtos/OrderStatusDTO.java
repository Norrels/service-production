package com.bytes.service.production.adapters.inbound.dtos;

import com.bytes.service.production.domain.models.ProductionStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusDTO {
    private Long orderId;
    private ProductionStatus currentStatus;
    private String statusDescription;
    private Integer queuePosition;
    private LocalDateTime lastUpdate;
}