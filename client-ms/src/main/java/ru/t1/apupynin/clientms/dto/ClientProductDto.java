package ru.t1.apupynin.clientms.dto;

import ru.t1.apupynin.clientms.enums.ClientProductStatus;

import java.time.LocalDateTime;

public class ClientProductDto {
    public Long id;
    public Long clientId;
    public Long productId;
    public LocalDateTime openDate;
    public LocalDateTime closeDate;
    public ClientProductStatus status;
}


