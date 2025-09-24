package ru.t1.apupynin.accountms.enums;

public enum TransactionType {
    DEBIT("Дебет"),
    CREDIT("Кредит"),
    TRANSFER_IN("Входящий перевод"),
    TRANSFER_OUT("Исходящий перевод"),
    PAYMENT("Платеж"),
    REFUND("Возврат");

    private final String description;

    TransactionType(String description) {
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
