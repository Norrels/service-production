package com.bytes.service.production.adapters.outbound.persistence;

import com.bytes.service.production.domain.models.ProductionStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "order_production")
@Data
@Builder
public class OrderProductionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "order_id", unique = true, nullable = false)
    private Long orderId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductionStatus status;
    
    @Column(name = "started_at")
    private LocalDateTime startedAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "finished_at")
    private LocalDateTime finishedAt;
    
    @Column(name = "position_in_queue")
    private Integer positionInQueue;
    
    // Dados mínimos do pedido para exibição
    @Column(name = "order_display_number")
    private String orderDisplayNumber; // Ex: "001", "002"
    
    @Column(name = "customer_name")
    private String customerName; // Para chamar quando pronto
}