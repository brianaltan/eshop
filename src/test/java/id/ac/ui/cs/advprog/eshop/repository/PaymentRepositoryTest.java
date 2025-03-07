package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentRepositoryTest {

    @InjectMocks
    PaymentRepository paymentRepository;

    @Mock
    Order mockOrder;

    private Payment createSamplePayment(String id, String method) {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Test Bank");
        paymentData.put("referenceCode", "REF" + id);

        return new Payment(id, method, "PENDING", paymentData, mockOrder);
    }

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();
    }

    @Test
    void testSaveAndFindById() {
        Payment payment = createSamplePayment("payment-123", "BANK_TRANSFER");
        Payment savedPayment = paymentRepository.save(payment);

        Payment foundPayment = paymentRepository.findById("payment-123");
        assertNotNull(foundPayment);
        assertEquals(payment.getId(), foundPayment.getId());
        assertEquals(payment.getMethod(), foundPayment.getMethod());
        assertEquals(payment.getStatus(), foundPayment.getStatus());
    }

    @Test
    void testFindByIdWhenNotFound() {
        Exception exception = assertThrows(RuntimeException.class, () ->
                paymentRepository.findById("nonexistent-id")
        );
        assertEquals("Payment not found", exception.getMessage());
    }

    @Test
    void testFindAllWhenEmpty() {
        List<Payment> payments = paymentRepository.findAll();
        assertTrue(payments.isEmpty());
    }

    @Test
    void testFindAllWithMultiplePayments() {
        Payment payment1 = createSamplePayment("payment-123", "BANK_TRANSFER");
        Payment payment2 = createSamplePayment("payment-456", "VOUCHER");

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        List<Payment> payments = paymentRepository.findAll();
        assertEquals(2, payments.size());

        // Verify the payments are in the list
        boolean foundPayment1 = false;
        boolean foundPayment2 = false;

        for (Payment payment : payments) {
            if (payment.getId().equals("payment-123")) {
                foundPayment1 = true;
            } else if (payment.getId().equals("payment-456")) {
                foundPayment2 = true;
            }
        }

        assertTrue(foundPayment1);
        assertTrue(foundPayment2);
    }

    @Test
    void testUpdateExistingPayment() {
        Payment payment = createSamplePayment("payment-123", "BANK_TRANSFER");
        paymentRepository.save(payment);

        // Update the payment
        payment.setStatus("SUCCESS");
        Map<String, String> updatedData = new HashMap<>();
        updatedData.put("bankName", "Updated Bank");
        updatedData.put("referenceCode", "UPDATED123");
        payment.setPaymentData(updatedData);

        Payment updatedPayment = paymentRepository.save(payment);

        // Verify the update
        assertEquals("SUCCESS", updatedPayment.getStatus());
        assertEquals("Updated Bank", updatedPayment.getPaymentData().get("bankName"));
        assertEquals("UPDATED123", updatedPayment.getPaymentData().get("referenceCode"));

        // Verify the updated payment is in the repository
        Payment foundPayment = paymentRepository.findById("payment-123");
        assertEquals("SUCCESS", foundPayment.getStatus());
        assertEquals("Updated Bank", foundPayment.getPaymentData().get("bankName"));
    }

    @Test
    void testFindByOrderId() {
        // Setup
        Order order1 = new Order("order-123",
                Collections.singletonList(new Product()), 1708560000L, "Test User");
        Order order2 = new Order("order-456",
                Collections.singletonList(new Product()), 1708560000L, "Test User");

        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Test Bank");
        paymentData.put("referenceCode", "REF123");

        Payment payment1 = new Payment("payment-123", "BANK_TRANSFER", "PENDING", paymentData, order1);
        Payment payment2 = new Payment("payment-456", "BANK_TRANSFER", "SUCCESS", paymentData, order2);
        Payment payment3 = new Payment("payment-789", "VOUCHER", "REJECTED", paymentData, order1);

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
        paymentRepository.save(payment3);

        // Test
        List<Payment> paymentsForOrder1 = paymentRepository.findByOrderId("order-123");

        // Verify
        assertEquals(2, paymentsForOrder1.size());

        boolean foundPayment1 = false;
        boolean foundPayment3 = false;

        for (Payment payment : paymentsForOrder1) {
            if (payment.getId().equals("payment-123")) {
                foundPayment1 = true;
            } else if (payment.getId().equals("payment-789")) {
                foundPayment3 = true;
            }
        }

        assertTrue(foundPayment1);
        assertTrue(foundPayment3);
    }

    @Test
    void testFindByOrderIdWhenNotFound() {
        List<Payment> payments = paymentRepository.findByOrderId("nonexistent-order");
        assertTrue(payments.isEmpty());
    }

    @Test
    void testDeletePayment() {
        Payment payment = createSamplePayment("payment-to-delete", "BANK_TRANSFER");
        paymentRepository.save(payment);

        paymentRepository.delete("payment-to-delete");

        Exception exception = assertThrows(RuntimeException.class, () -> {
            paymentRepository.findById("payment-to-delete");
        });
        assertEquals("Payment not found", exception.getMessage());
    }

    @Test
    void testDeleteNonexistentPayment() {
        // Ensure that deleting a nonexistent payment doesn't throw an error
        assertDoesNotThrow(() -> {
            paymentRepository.delete("nonexistent-id");
        });
    }
}