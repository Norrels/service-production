package com.bytes.service.production.adapters.inbound.rest;

import com.bytes.service.production.adapters.inbound.dtos.OrderStatusDTO;
import com.bytes.service.production.domain.models.ProductionQueue;
import com.bytes.service.production.application.ProductionService;
import com.bytes.service.production.domain.models.ProductionStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/production")
public class ProductionController {
    
    private final ProductionService productionService;

    public ProductionController(ProductionService productionService) {
        this.productionService = productionService;
    }

    @GetMapping("/queue")
    public ResponseEntity<List<ProductionQueue>> getProductionQueue() {
        return ResponseEntity.ok(productionService.getActiveOrders());
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long orderId,
            @RequestBody ProductionStatus status) {
        
        productionService.updateStatus(orderId, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/order/{orderId}/status")
    public ResponseEntity<OrderStatusDTO> getOrderStatus(@PathVariable Long orderId) {
        return ResponseEntity.ok(productionService.getOrderStatus(orderId));
    }

}