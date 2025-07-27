package com.bytes.service.production.domain.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductionStatusTest {

    @Test
    void shouldReturnCorrectDescriptionForAllStatuses() {
        assertEquals("Aguardando Pagamento", ProductionStatus.WAITING_PAYMENT.getDescription());
        assertEquals("Recebido", ProductionStatus.RECEIVED.getDescription());
        assertEquals("Em Preparação", ProductionStatus.IN_PREPARATION.getDescription());
        assertEquals("Pronto", ProductionStatus.READY.getDescription());
        assertEquals("Finalizado", ProductionStatus.FINISHED.getDescription());
        assertEquals("Cancelado", ProductionStatus.CANCELLED.getDescription());
    }

    @Test
    void shouldCreateStatusFromValidString() {
        assertEquals(ProductionStatus.WAITING_PAYMENT, ProductionStatus.fromString("WAITING_PAYMENT"));
        assertEquals(ProductionStatus.RECEIVED, ProductionStatus.fromString("RECEIVED"));
        assertEquals(ProductionStatus.IN_PREPARATION, ProductionStatus.fromString("IN_PREPARATION"));
        assertEquals(ProductionStatus.READY, ProductionStatus.fromString("READY"));
        assertEquals(ProductionStatus.FINISHED, ProductionStatus.fromString("FINISHED"));
        assertEquals(ProductionStatus.CANCELLED, ProductionStatus.fromString("CANCELLED"));
    }

    @Test
    void shouldCreateStatusFromLowercaseString() {
        assertEquals(ProductionStatus.WAITING_PAYMENT, ProductionStatus.fromString("waiting_payment"));
        assertEquals(ProductionStatus.RECEIVED, ProductionStatus.fromString("received"));
        assertEquals(ProductionStatus.IN_PREPARATION, ProductionStatus.fromString("in_preparation"));
        assertEquals(ProductionStatus.READY, ProductionStatus.fromString("ready"));
        assertEquals(ProductionStatus.FINISHED, ProductionStatus.fromString("finished"));
        assertEquals(ProductionStatus.CANCELLED, ProductionStatus.fromString("cancelled"));
    }

    @Test
    void shouldCreateStatusFromMixedCaseString() {
        assertEquals(ProductionStatus.WAITING_PAYMENT, ProductionStatus.fromString("Waiting_Payment"));
        assertEquals(ProductionStatus.RECEIVED, ProductionStatus.fromString("Received"));
        assertEquals(ProductionStatus.IN_PREPARATION, ProductionStatus.fromString("In_Preparation"));
    }

    @Test
    void shouldThrowExceptionForInvalidString() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ProductionStatus.fromString("INVALID_STATUS")
        );
        assertEquals("Categoria inválida: INVALID_STATUS", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionForNullString() {
        assertThrows(
            NullPointerException.class,
            () -> ProductionStatus.fromString(null)
        );
    }

    @Test
    void shouldThrowExceptionForEmptyString() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> ProductionStatus.fromString("")
        );
        assertEquals("Categoria inválida: ", exception.getMessage());
    }

    @Test
    void shouldHaveCorrectEnumValues() {
        ProductionStatus[] values = ProductionStatus.values();
        assertEquals(6, values.length);
        
        assertTrue(containsStatus(values, ProductionStatus.WAITING_PAYMENT));
        assertTrue(containsStatus(values, ProductionStatus.RECEIVED));
        assertTrue(containsStatus(values, ProductionStatus.IN_PREPARATION));
        assertTrue(containsStatus(values, ProductionStatus.READY));
        assertTrue(containsStatus(values, ProductionStatus.FINISHED));
        assertTrue(containsStatus(values, ProductionStatus.CANCELLED));
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        assertEquals("WAITING_PAYMENT", ProductionStatus.WAITING_PAYMENT.toString());
        assertEquals("RECEIVED", ProductionStatus.RECEIVED.toString());
        assertEquals("IN_PREPARATION", ProductionStatus.IN_PREPARATION.toString());
        assertEquals("READY", ProductionStatus.READY.toString());
        assertEquals("FINISHED", ProductionStatus.FINISHED.toString());
        assertEquals("CANCELLED", ProductionStatus.CANCELLED.toString());
    }

    private boolean containsStatus(ProductionStatus[] values, ProductionStatus status) {
        for (ProductionStatus value : values) {
            if (value == status) {
                return true;
            }
        }
        return false;
    }
}