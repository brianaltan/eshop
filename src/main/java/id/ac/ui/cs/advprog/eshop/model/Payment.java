package id.ac.ui.cs.advprog.eshop.model;

import lombok.Builder;
import lombok.Getter;
import enums.PaymentStatus;

import java.util.Map;

@Builder
@Getter
public class Payment {
    private String id;
    private String method;
    private String status;
    private Map<String, String> paymentData;
    private Order order;

    public Payment(String id, String method, String status, Map<String, String> paymentData, Order order) {
        this.id = id;
        this.paymentData = paymentData;

        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.method = method;

        if (order == null) {
            throw new IllegalArgumentException();
        }
        this.order = order;

        setStatus(status);
    }

    public Payment(String id, String method, Map<String, String> paymentData, Order order) {
        this(id, method, PaymentStatus.PENDING.getValue(), paymentData, order);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMethod(String method) {
        if (method == null || method.isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.method = method;
    }

    public void setStatus(String status) {
        if (PaymentStatus.contains(status)) {
            this.status = status;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setPaymentData(Map<String, String> paymentData) {
        this.paymentData = paymentData;
    }

    public void setOrder(Order order) {
        if (order == null) {
            throw new IllegalArgumentException();
        }
        this.order = order;
    }
}