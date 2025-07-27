package com.bytes.service.production.domain.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class ProductionItemTest {

    private ProductionItem productionItem;

    @BeforeEach
    void setUp() {
        productionItem = new ProductionItem("Pizza Margherita", 2, "Pizza", "Extra cheese");
    }

    @Test
    void shouldCreateProductionItemWithValidData() {
        assertNotNull(productionItem);
        assertEquals("Pizza Margherita", productionItem.getProductName());
        assertEquals(2, productionItem.getQuantity());
        assertEquals("Pizza", productionItem.getCategory());
        assertEquals("Extra cheese", productionItem.getObservations());
    }

    @Test
    void shouldSetAndGetProductName() {
        productionItem.setProductName("Hamburger");
        assertEquals("Hamburger", productionItem.getProductName());
    }

    @Test
    void shouldSetAndGetQuantity() {
        productionItem.setQuantity(5);
        assertEquals(5, productionItem.getQuantity());
    }

    @Test
    void shouldSetAndGetCategory() {
        productionItem.setCategory("Fast Food");
        assertEquals("Fast Food", productionItem.getCategory());
    }

    @Test
    void shouldSetAndGetObservations() {
        productionItem.setObservations("No onions");
        assertEquals("No onions", productionItem.getObservations());
    }

    @Test
    void shouldHandleNullValues() {
        ProductionItem nullItem = new ProductionItem(null, null, null, null);
        assertNull(nullItem.getProductName());
        assertNull(nullItem.getQuantity());
        assertNull(nullItem.getCategory());
        assertNull(nullItem.getObservations());
    }

    @Test
    void shouldHandleZeroQuantity() {
        productionItem.setQuantity(0);
        assertEquals(0, productionItem.getQuantity());
    }

    @Test
    void shouldHandleNegativeQuantity() {
        productionItem.setQuantity(-1);
        assertEquals(-1, productionItem.getQuantity());
    }

    @Test
    void shouldHandleEmptyStrings() {
        ProductionItem emptyItem = new ProductionItem("", 1, "", "");
        assertEquals("", emptyItem.getProductName());
        assertEquals("", emptyItem.getCategory());
        assertEquals("", emptyItem.getObservations());
    }
}