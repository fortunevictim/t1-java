package ru.t1.apupynin.clientms.mapper;

import org.springframework.stereotype.Component;
import ru.t1.apupynin.clientms.dto.ClientProductDto;
import ru.t1.apupynin.clientms.dto.ProductDto;
import ru.t1.apupynin.clientms.entity.ClientProduct;
import ru.t1.apupynin.clientms.entity.Product;

@Component
public class DtoMapper {

    public ProductDto toDto(Product entity) {
        ProductDto dto = new ProductDto();
        dto.id = entity.getId();
        dto.key = entity.getKey();
        dto.name = entity.getName();
        dto.createDate = entity.getCreateDate();
        dto.productId = entity.getProductId();
        return dto;
    }

    public ClientProductDto toDto(ClientProduct entity) {
        ClientProductDto dto = new ClientProductDto();
        dto.id = entity.getId();
        dto.clientId = entity.getClientId();
        dto.productId = entity.getProductId();
        dto.openDate = entity.getOpenDate();
        dto.closeDate = entity.getCloseDate();
        dto.status = entity.getStatus();
        return dto;
    }
}


