package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    Product product;

    @BeforeEach
    void setUp() {
        this.product = new Product();
        this.product.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        this.product.setProductName("Sampo Cap Bambang");
        this.product.setProductQuantity(100);
    }

    @Test
    void testGetProductId() {
        assertEquals("eb558e9f-1c39-460e-8860-71af6af63bd6", product.getProductId());
    }

    @Test
    void testGetProductName() {
        assertEquals("Sampo Cap Bambang", product.getProductName());
    }

    @Test
    void testGetProductQuantity() {
        assertEquals(100, product.getProductQuantity());
    }

    @Test
    void testGetProductIdNegative() {
        assertNotEquals("eb558e9f-1c39-460e-8860-71af6af63bd7", product.getProductId());
    }

    @Test
    void testGetProductNameNegative() {
        assertNotEquals("Sampo Cap Bang", product.getProductName());
    }

    @Test
    void testGetProductQuantityNegative() {
        assertNotEquals(101, product.getProductQuantity());
    }
}