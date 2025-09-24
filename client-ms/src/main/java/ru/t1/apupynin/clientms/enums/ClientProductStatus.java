package ru.t1.apupynin.clientms.enums;

public enum ClientProductStatus {
    ACTIVE("Активный"),
    CLOSED("Закрытый"),
    BLOCKED("Заблокированный"),
    ARRESTED("Арестованный");

    private final String description;

    ClientProductStatus(String description) {
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
