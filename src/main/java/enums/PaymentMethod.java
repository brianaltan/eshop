package enums;

import java.util.Arrays;

public enum PaymentMethod {
    BANK_TRANSFER("BANK_TRANSFER"),
    VOUCHER("VOUCHER");

    private final String value;

    PaymentMethod(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean contains(String method) {
        return Arrays.stream(values())
                .anyMatch(paymentMethod -> paymentMethod.getValue().equals(method));
    }

    public static PaymentMethod fromString(String method) {
        return Arrays.stream(values())
                .filter(paymentMethod -> paymentMethod.getValue().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown payment method: " + method));
    }
}