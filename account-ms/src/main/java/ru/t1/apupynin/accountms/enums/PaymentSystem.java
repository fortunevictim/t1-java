package ru.t1.apupynin.accountms.enums;

public enum PaymentSystem {
    VISA("Visa"),
    MASTERCARD("Mastercard"),
    MIR("Мир");

    private final String description;

    PaymentSystem(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
}
