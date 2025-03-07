package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Order;
import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import enums.PaymentMethod;
import enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private PaymentService paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentService(paymentRepository);
    }

    @Test
    void testCreatePayment() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Test Bank");
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Execute
        Payment result = paymentService.createPayment(payment);

        // Verify
        assertNotNull(result);
        assertEquals("payment-123", result.getId());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testFindPaymentById() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        Payment expectedPayment = new Payment("payment-123", "BANK_TRANSFER", paymentData, mockOrder);

        when(paymentRepository.findById("payment-123")).thenReturn(expectedPayment);

        // Execute
        Payment result = paymentService.findPaymentById("payment-123");

        // Verify
        assertNotNull(result);
        assertEquals(expectedPayment, result);
    }

    @Test
    void testFindAllPayments() {
        // Setup
        List<Payment> expectedPayments = new ArrayList<>();
        expectedPayments.add(new Payment("payment-1", "BANK_TRANSFER", new HashMap<>(), mockOrder));
        expectedPayments.add(new Payment("payment-2", "VOUCHER", new HashMap<>(), mockOrder));

        when(paymentRepository.findAll()).thenReturn(expectedPayments);

        // Execute
        List<Payment> results = paymentService.findAllPayments();

        // Verify
        assertEquals(2, results.size());
        assertEquals(expectedPayments, results);
    }

    @Test
    void testFindPaymentsByOrderId() {
        // Setup
        List<Payment> expectedPayments = new ArrayList<>();
        expectedPayments.add(new Payment("payment-1", "BANK_TRANSFER", new HashMap<>(), mockOrder));

        when(paymentRepository.findByOrderId("order-123")).thenReturn(expectedPayments);
        when(mockOrder.getId()).thenReturn("order-123");

        // Execute
        List<Payment> results = paymentService.findPaymentsByOrderId("order-123");

        // Verify
        assertEquals(1, results.size());
        assertEquals(expectedPayments, results);
    }

    @Test
    void testUpdatePayment() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        // Execute
        Payment result = paymentService.updatePayment(payment);

        // Verify
        assertEquals(payment, result);
        verify(paymentRepository).save(payment);
    }

    @Test
    void testDeletePayment() {
        // Execute
        paymentService.deletePayment("payment-123");

        // Verify
        verify(paymentRepository).delete("payment-123");
    }

    @Test
    void testVoucherPayment_ValidVoucher() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execute
        Payment result = paymentService.createPayment(payment);

        // Verify
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_InvalidLength() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345"); // Too short

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execute
        Payment result = paymentService.createPayment(payment);

        // Verify
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_InvalidPrefix() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "STORE1234ABC5678"); // Wrong prefix

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execute
        Payment result = paymentService.createPayment(payment);

        // Verify
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_NotEnoughNumbers() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOPABCDEFGHIJK"); // No numerical characters

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execute
        Payment result = paymentService.createPayment(payment);

        // Verify
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_ExactlyEightNumbers() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678ABC"); // Exactly 8 numbers

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execute
        Payment result = paymentService.createPayment(payment);

        // Verify
        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_TooManyNumbers() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234567890AB"); // 10 numbers, should have 8

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execute
        Payment result = paymentService.createPayment(payment);

        // Verify
        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testNonVoucherPayment() {
        // Setup
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Test Bank");

        Payment payment = new Payment("payment-123", PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        // Execute
        Payment result = paymentService.createPayment(payment);

        // Verify - status should remain PENDING for non-voucher payments
        assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
    }
}