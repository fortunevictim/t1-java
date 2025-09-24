package ru.t1.apupynin.accountms.enums;

public enum PaymentType {
    TRANSFER("Перевод"),
    DEPOSIT("Пополнение"),
    WITHDRAWAL("Снятие"),
    PAYMENT("Платеж"),
    REFUND("Возврат");

    private final String description;

    PaymentType(String description) {
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
