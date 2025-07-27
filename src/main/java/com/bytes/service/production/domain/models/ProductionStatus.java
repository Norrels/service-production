package com.bytes.service.production.domain.models;

public enum ProductionStatus {
    WAITING_PAYMENT("Aguardando Pagamento"),
    RECEIVED("Recebido"),
    IN_PREPARATION("Em Preparação"),
    READY("Pronto"),
    FINISHED("Finalizado"),
    CANCELLED("Cancelado");
    
    private final String description;
    
    ProductionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static ProductionStatus fromString(String value) {
        try {
            return ProductionStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Categoria inválida: " + value);
        }
    }
}