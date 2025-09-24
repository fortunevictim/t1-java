package ru.t1.apupynin.clientms.enums;

public enum DocumentType {
    PASSPORT("Паспорт"),
    INT_PASSPORT("Заграничный паспорт"),
    BIRTH_CERT("Свидетельство о рождении");

    private final String description;

    DocumentType(String description) {
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
