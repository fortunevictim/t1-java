package ru.t1.apupynin.clientms.enums;

public enum ProductKey {
    DC("Депозитный счет"),
    CC("Кредитная карта"),
    AC("Аккредитив"),
    IPO("Ипотечный кредит"),
    PC("Потребительский кредит"),
    PENS("Пенсионный счет"),
    NS("Накопительный счет"),
    INS("Страховой продукт"),
    BS("Брокерский счет");

    private final String description;

    ProductKey(String description) {
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
