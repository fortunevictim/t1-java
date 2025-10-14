package ru.t1.apupynin.clientms.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.t1.apupynin.clientms.dto.ClientProductDto;
import ru.t1.apupynin.clientms.entity.ClientProduct;
import ru.t1.apupynin.clientms.entity.Product;
import ru.t1.apupynin.clientms.enums.ProductKey;
import ru.t1.apupynin.clientms.mapper.DtoMapper;
import ru.t1.apupynin.clientms.repository.ClientProductRepository;
import ru.t1.apupynin.clientms.repository.ProductRepository;
import ru.t1.apupynin.common.aspects.annotation.HttpIncomeRequestLog;
import ru.t1.apupynin.common.aspects.annotation.HttpOutcomeRequestLog;
import ru.t1.apupynin.common.aspects.annotation.LogDatasourceError;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/client-products")
public class ClientProductController {

    private final ClientProductRepository clientProductRepository;
    private final ProductRepository productRepository;
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;
    private final DtoMapper mapper;

    public ClientProductController(ClientProductRepository clientProductRepository,
                                   ProductRepository productRepository,
                                   KafkaTemplate<String, Map<String, Object>> kafkaTemplate,
                                   DtoMapper mapper) {
        this.clientProductRepository = clientProductRepository;
        this.productRepository = productRepository;
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = mapper;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MASTER','GRAND_EMPLOYEE','CURRENT_CLIENT')")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public List<ClientProductDto> list() {
        return clientProductRepository.findAll().stream().map(mapper::toDto).toList();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER','GRAND_EMPLOYEE','CURRENT_CLIENT')")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public ResponseEntity<ClientProductDto> get(@PathVariable Long id) {
        return clientProductRepository.findById(id)
                .map(mapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','GRAND_EMPLOYEE','CURRENT_CLIENT')")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public ResponseEntity<ClientProductDto> create(@RequestBody ClientProduct cp) {
        ClientProduct saved = clientProductRepository.save(cp);

        // resolve product key
        Product product = productRepository.findById(saved.getProductId()).orElse(null);
        if (product != null) {
            ProductKey key = product.getKey();
            Map<String, Object> payload = Map.of(
                    "clientId", saved.getClientId(),
                    "productId", saved.getProductId(),
                    "clientProductId", saved.getId(),
                    "status", String.valueOf(saved.getStatus())
            );
            switch (key) {
                case DC, CC, NS, PENS -> kafkaTemplate.send("client_products", payload);
                case IPO, PC, AC -> kafkaTemplate.send("client_credit_products", payload);
                default -> {}
            }
        }

        return ResponseEntity.created(URI.create("/api/client-products/" + saved.getId())).body(mapper.toDto(saved));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER','GRAND_EMPLOYEE','CURRENT_CLIENT')")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public ResponseEntity<ClientProductDto> update(@PathVariable Long id, @RequestBody ClientProduct cp) {
        return clientProductRepository.findById(id)
                .map(existing -> {
                    cp.setId(existing.getId());
                    ClientProduct saved = clientProductRepository.save(cp);
                    return ResponseEntity.ok(mapper.toDto(saved));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER','GRAND_EMPLOYEE')")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!clientProductRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        clientProductRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


