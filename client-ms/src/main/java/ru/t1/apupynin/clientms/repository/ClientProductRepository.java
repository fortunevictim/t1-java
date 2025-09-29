package ru.t1.apupynin.clientms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.apupynin.clientms.entity.ClientProduct;

import java.util.List;

public interface ClientProductRepository extends JpaRepository<ClientProduct, Long> {
    List<ClientProduct> findAllByClientId(Long clientId);
}


