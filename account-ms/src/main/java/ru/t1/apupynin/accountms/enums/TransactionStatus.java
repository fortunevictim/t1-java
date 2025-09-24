package ru.t1.apupynin.accountms.enums;

public enum TransactionStatus {
    ALLOWED("Разрешена"),
    PROCESSING("Обрабатывается"),
    COMPLETE("Завершена"),
    BLOCKED("Заблокирована"),
    CANCELLED("Отменена");

    private final String description;

    TransactionStatus(String description) {
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
