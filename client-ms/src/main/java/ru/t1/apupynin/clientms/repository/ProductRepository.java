package ru.t1.apupynin.clientms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.apupynin.clientms.entity.Product;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByProductId(String productId);
}


