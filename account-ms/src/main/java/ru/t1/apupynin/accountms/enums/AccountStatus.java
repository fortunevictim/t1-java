package ru.t1.apupynin.accountms.enums;

public enum AccountStatus {
    ACTIVE("Активный"),
    BLOCKED("Заблокированный"),
    CLOSED("Закрытый"),
    SUSPENDED("Приостановленный");

    private final String description;

    AccountStatus(String description) {
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
