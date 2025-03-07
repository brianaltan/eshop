package id.ac.ui.cs.advprog.eshop.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {
    private Order order;
    private Map<String, String> paymentData;

    @BeforeEach
    void setUp() {
        List<Product> products = new ArrayList<>();
        Product product1 = new Product();
        product1.setProductId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        product1.setProductName("Sampo Cap Bambang");
        product1.setProductQuantity(2);
        products.add(product1);

        this.order = new Order("13652556-012a-4c07-b546-54eb1396d79b",
                products, 1708560000L, "Safira Sudrajat");

        this.paymentData = new HashMap<>();
        this.paymentData.put("bankName", "Example Bank");
        this.paymentData.put("referenceCode", "REF123456");
    }

    @Test
    void testCreatePayment() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, order);

        assertEquals("pay-123", payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals(order, payment.getOrder());
    }

    @Test
    void testCreatePaymentWithNullOrder() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, null);
        });
    }

    @Test
    void testCreatePaymentWithNullMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("pay-123", null, "PENDING", paymentData, order);
        });
    }

    @Test
    void testCreatePaymentWithEmptyMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            Payment payment = new Payment("pay-123", "", "PENDING", paymentData, order);
        });
    }

    @Test
    void testCreatePaymentWithDefaultStatus() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", paymentData, order);

        assertEquals("pay-123", payment.getId());
        assertEquals("BANK_TRANSFER", payment.getMethod());
        assertEquals("PENDING", payment.getStatus());
        assertEquals(paymentData, payment.getPaymentData());
        assertEquals(order, payment.getOrder());
    }

    @Test
    void testSetValidStatus() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, order);
        payment.setStatus("SUCCESS");
        assertEquals("SUCCESS", payment.getStatus());

        payment.setStatus("REJECTED");
        assertEquals("REJECTED", payment.getStatus());
    }

    @Test
    void testSetInvalidStatus() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, order);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setStatus("INVALID_STATUS");
        });
    }

    @Test
    void testUpdatePaymentData() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, order);

        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("bankName", "New Bank");
        updatedData.put("referenceCode", "NEW789012");

        payment.setPaymentData(updatedData);

        assertEquals("New Bank", payment.getPaymentData().get("bankName"));
        assertEquals("NEW789012", payment.getPaymentData().get("referenceCode"));
    }

    @Test
    void testGetAndSetId() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, order);
        payment.setId("pay-456");
        assertEquals("pay-456", payment.getId());
    }

    @Test
    void testGetAndSetMethod() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, order);
        payment.setMethod("VOUCHER");
        assertEquals("VOUCHER", payment.getMethod());
    }

    @Test
    void testSetInvalidMethod() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, order);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setMethod("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setMethod(null);
        });
    }

    @Test
    void testGetAndSetOrder() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, order);

        List<Product> newProducts = new ArrayList<>();
        Product newProduct = new Product();
        newProduct.setProductId("new-product-id");
        newProduct.setProductName("New Product");
        newProduct.setProductQuantity(1);
        newProducts.add(newProduct);

        Order newOrder = new Order("new-order-id", newProducts, 1708570000L, "New Author");
        payment.setOrder(newOrder);

        assertEquals(newOrder, payment.getOrder());
    }

    @Test
    void testSetNullOrder() {
        Payment payment = new Payment("pay-123", "BANK_TRANSFER", "PENDING", paymentData, order);

        assertThrows(IllegalArgumentException.class, () -> {
            payment.setOrder(null);
        });
    }
}