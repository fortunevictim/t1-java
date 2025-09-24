package ru.t1.apupynin.accountms.enums;

public enum CardStatus {
    ACTIVE("Активная"),
    BLOCKED("Заблокированная"),
    EXPIRED("Истекшая"),
    LOST("Утерянная"),
    STOLEN("Украденная");

    private final String description;

    CardStatus(String description) {
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
