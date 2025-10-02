package ru.t1.apupynin.clientms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.t1.apupynin.clientms.annotation.HttpIncomeRequestLog;
import ru.t1.apupynin.clientms.annotation.HttpOutcomeRequestLog;
import ru.t1.apupynin.clientms.annotation.LogDatasourceError;
import ru.t1.apupynin.clientms.dto.ProductDto;
import ru.t1.apupynin.clientms.entity.Product;
import ru.t1.apupynin.clientms.mapper.DtoMapper;
import ru.t1.apupynin.clientms.repository.ProductRepository;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final DtoMapper mapper;

    public ProductController(ProductRepository productRepository, DtoMapper mapper) {
        this.productRepository = productRepository;
        this.mapper = mapper;
    }

    @GetMapping
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public List<ProductDto> list() {
        return productRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public ResponseEntity<ProductDto> get(@PathVariable("id") Long id) {
        return productRepository.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public ResponseEntity<ProductDto> create(@RequestBody Product product) {
        Product saved = productRepository.save(product);
        return ResponseEntity.created(URI.create("/api/products/" + saved.getId())).body(mapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable("id") Long id, @RequestBody Product product) {
        return productRepository.findById(id)
                .map(existing -> {
                    product.setId(existing.getId());
                    Product saved = productRepository.save(product);
                    return ResponseEntity.ok(mapper.toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        if (!productRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


