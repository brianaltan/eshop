package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import enums.PaymentStatus;
import enums.PaymentMethod;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class PaymentRepository {
    private final Map<String, Payment> paymentMap;

    public PaymentRepository() {
        this.paymentMap = new HashMap<>();
    }

    public Payment save(Payment payment) {
        paymentMap.put(payment.getId(), payment);
        return payment;
    }

    public Payment findById(String id) {
        if (!paymentMap.containsKey(id)) {
            throw new PaymentNotFoundException("Payment not found");
        }
        return paymentMap.get(id);
    }

    public List<Payment> findAll() {
        return new ArrayList<>(paymentMap.values());
    }

    public List<Payment> findByOrderId(String orderId) {
        return paymentMap.values().stream()
                .filter(payment -> payment.getOrder().getId().equals(orderId))
                .collect(Collectors.toList());
    }

    public List<Payment> findByStatus(PaymentStatus status) {
        return paymentMap.values().stream()
                .filter(payment -> payment.getStatus().equals(status.getValue()))
                .collect(Collectors.toList());
    }

    public List<Payment> findByMethod(PaymentMethod method) {
        return paymentMap.values().stream()
                .filter(payment -> payment.getMethod().equals(method.getValue()))
                .collect(Collectors.toList());
    }

    public void delete(String id) {
        paymentMap.remove(id);
    }

    public static class PaymentNotFoundException extends RuntimeException {
        public PaymentNotFoundException(String message) {
            super(message);
        }
    }
}