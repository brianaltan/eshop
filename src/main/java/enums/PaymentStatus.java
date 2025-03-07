package enums;

import java.util.Arrays;

public enum PaymentStatus {
    PENDING("PENDING"),
    SUCCESS("SUCCESS"),
    REJECTED("REJECTED");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String status) {
        return Arrays.stream(values())
                .anyMatch(paymentStatus -> paymentStatus.getValue().equals(status));
    }
}