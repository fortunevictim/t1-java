package ru.t1.apupynin.creditms.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.apupynin.creditms.entity.ProductRegistry;
import ru.t1.apupynin.creditms.repository.ProductRegistryRepository;
import ru.t1.apupynin.common.aspects.annotation.Metric;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class CreditService {

    private final ProductRegistryRepository productRegistryRepository;
    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public CreditService(ProductRegistryRepository productRegistryRepository,
                         KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.productRegistryRepository = productRegistryRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    @Metric
    public void processCreditProduct(Map<String, Object> payload, long creditLimit) {
        Long clientId = toLong(payload.get("clientId"));
        Long productId = toLong(payload.get("productId"));
        BigDecimal interestRate = new BigDecimal("0.22");
        Integer monthCount = 60;

        log.info("Processing credit product for clientId: {}, productId: {}", clientId, productId);

        if (clientId == null || productId == null) {
            log.warn("Missing required fields: clientId={}, productId={}", clientId, productId);
            return;
        }

        ProductRegistry pr = new ProductRegistry(clientId, null, productId, interestRate, LocalDateTime.now(), monthCount);
        pr = productRegistryRepository.save(pr);
        
        log.info("Created ProductRegistry with id: {}", pr.getId());

        kafkaTemplate.send("client_credit_opened", Map.of(
                "clientId", pr.getClientId(),
                "productId", pr.getProductId(),
                "productRegistryId", pr.getId()
        ));
        
        log.info("Sent message to client_credit_opened topic");
    }

    private static Long toLong(Object o) {
        if (o instanceof Number n) return n.longValue();
        if (o instanceof String s) try { return Long.parseLong(s); } catch (Exception ignored) {}
        return null;
    }
}


