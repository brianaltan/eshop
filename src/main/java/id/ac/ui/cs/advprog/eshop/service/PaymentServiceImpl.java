package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Payment;
import id.ac.ui.cs.advprog.eshop.repository.PaymentRepository;
import enums.PaymentMethod;
import enums.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(Payment payment) {
        // If it's a voucher payment, validate the voucher code
        if (payment.getMethod().equals(PaymentMethod.VOUCHER.getValue())) {
            validateVoucherPayment(payment);
        }

        return paymentRepository.save(payment);
    }

    @Override
    public List<Payment> findAllPayments() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findPaymentById(String paymentId) {
        return paymentRepository.findById(paymentId);
    }

    @Override
    public List<Payment> findPaymentsByOrderId(String orderId) {
        return paymentRepository.findByOrderId(orderId);
    }

    @Override
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public void deletePayment(String paymentId) {
        paymentRepository.delete(paymentId);
    }

    /**
     * Validates a voucher payment and sets the appropriate status.
     *
     * @param payment the voucher payment to validate
     */
    private void validateVoucherPayment(Payment payment) {
        String voucherCode = payment.getPaymentData().get("voucherCode");

        if (isValidVoucherCode(voucherCode)) {
            payment.setStatus(PaymentStatus.SUCCESS.getValue());
        } else {
            payment.setStatus(PaymentStatus.REJECTED.getValue());
        }
    }

    /**
     * Validates if a voucher code meets the required criteria:
     * - 16 characters long
     * - Starts with "ESHOP"
     * - Contains 8 numerical characters
     *
     * @param voucherCode the voucher code to validate
     * @return true if the voucher code is valid, false otherwise
     */
    private boolean isValidVoucherCode(String voucherCode) {
        if (voucherCode == null) {
            return false;
        }

        // Check length is 16
        if (voucherCode.length() != 16) {
            return false;
        }

        // Check starts with "ESHOP"
        if (!voucherCode.startsWith("ESHOP")) {
            return false;
        }

        // Count numerical characters
        int numericCount = 0;
        for (char c : voucherCode.toCharArray()) {
            if (Character.isDigit(c)) {
                numericCount++;
            }
        }

        // Check has exactly 8 numerical characters
        return numericCount == 8;
    }
}