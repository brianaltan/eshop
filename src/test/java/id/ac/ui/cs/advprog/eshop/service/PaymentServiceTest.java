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
        PaymentServiceImpl serviceImpl = new PaymentServiceImpl();
        setField(serviceImpl, "paymentRepository", paymentRepository);
        paymentService = serviceImpl;
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = PaymentServiceImpl.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testCreatePayment() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Test Bank");
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.createPayment(payment);

        assertNotNull(result);
        assertEquals("payment-123", result.getId());
        verify(paymentRepository).save(payment);
    }

    @Test
    void testFindPaymentById() {
        Map<String, String> paymentData = new HashMap<>();
        Payment expectedPayment = new Payment("payment-123", "BANK_TRANSFER", paymentData, mockOrder);

        when(paymentRepository.findById("payment-123")).thenReturn(expectedPayment);

        Payment result = paymentService.findPaymentById("payment-123");

        assertNotNull(result);
        assertEquals(expectedPayment, result);
    }

    @Test
    void testFindAllPayments() {
        List<Payment> expectedPayments = new ArrayList<>();
        expectedPayments.add(new Payment("payment-1", "BANK_TRANSFER", new HashMap<>(), mockOrder));
        expectedPayments.add(new Payment("payment-2", "VOUCHER", new HashMap<>(), mockOrder));

        when(paymentRepository.findAll()).thenReturn(expectedPayments);

        List<Payment> results = paymentService.findAllPayments();

        assertEquals(2, results.size());
        assertEquals(expectedPayments, results);
    }

    @Test
    void testFindPaymentsByOrderId() {
        List<Payment> expectedPayments = new ArrayList<>();
        expectedPayments.add(new Payment("payment-1", "BANK_TRANSFER", new HashMap<>(), mockOrder));

        when(paymentRepository.findByOrderId("order-123")).thenReturn(expectedPayments);

        List<Payment> results = paymentService.findPaymentsByOrderId("order-123");

        assertEquals(1, results.size());
        assertEquals(expectedPayments, results);
    }

    @Test
    void testUpdatePayment() {
        Map<String, String> paymentData = new HashMap<>();
        Payment payment = new Payment("payment-123", "BANK_TRANSFER", paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.updatePayment(payment);

        assertEquals(payment, result);
        verify(paymentRepository).save(payment);
    }

    @Test
    void testDeletePayment() {
        paymentService.deletePayment("payment-123");

        verify(paymentRepository).delete("payment-123");
    }

    @Test
    void testVoucherPayment_ValidVoucher() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234ABC5678");

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.createPayment(payment);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_InvalidLength() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345");

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.createPayment(payment);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_InvalidPrefix() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "STORE1234ABC5678");

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.createPayment(payment);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_NotEnoughNumbers() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOPABCDEFGHIJK");

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.createPayment(payment);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_ExactlyEightNumbers() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP12345678ABC");

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.createPayment(payment);

        assertEquals(PaymentStatus.SUCCESS.getValue(), result.getStatus());
    }

    @Test
    void testVoucherPayment_TooManyNumbers() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("voucherCode", "ESHOP1234567890AB");

        Payment payment = new Payment("payment-123", PaymentMethod.VOUCHER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.createPayment(payment);

        assertEquals(PaymentStatus.REJECTED.getValue(), result.getStatus());
    }

    @Test
    void testNonVoucherPayment() {
        Map<String, String> paymentData = new HashMap<>();
        paymentData.put("bankName", "Test Bank");

        Payment payment = new Payment("payment-123", PaymentMethod.BANK_TRANSFER.getValue(),
                PaymentStatus.PENDING.getValue(), paymentData, mockOrder);

        when(paymentRepository.save(any(Payment.class))).thenAnswer(i -> i.getArguments()[0]);

        Payment result = paymentService.createPayment(payment);

        assertEquals(PaymentStatus.PENDING.getValue(), result.getStatus());
    }
}