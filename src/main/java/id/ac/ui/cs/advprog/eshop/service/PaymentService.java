package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import java.util.List;

public interface PaymentService {
    Payment createPayment(Payment payment);
    List<Payment> findAllPayments();
    Payment findPaymentById(String paymentId);
    List<Payment> findPaymentsByOrderId(String orderId);
    Payment updatePayment(Payment payment);
    void deletePayment(String paymentId);
}