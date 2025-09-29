package ru.t1.apupynin.clientms.dto;

import ru.t1.apupynin.clientms.enums.ProductKey;

import java.time.LocalDateTime;

public class ProductDto {
    public Long id;
    public ProductKey key;
    public String name;
    public LocalDateTime createDate;
    public String productId;
}


