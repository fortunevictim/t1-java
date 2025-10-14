package ru.t1.apupynin.clientms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.t1.apupynin.common.aspects.annotation.HttpIncomeRequestLog;
import ru.t1.apupynin.common.aspects.annotation.HttpOutcomeRequestLog;
import ru.t1.apupynin.common.aspects.annotation.LogDatasourceError;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/cards")
public class CardRequestController {

    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public CardRequestController(KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/request")
    @PreAuthorize("hasAnyRole('MASTER','GRAND_EMPLOYEE','CURRENT_CLIENT')")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public ResponseEntity<Void> requestCard(@RequestParam("accountId") Long accountId,
                                            @RequestParam("paymentSystem") String paymentSystem) {
        log.info("Card request received for accountId: {}, paymentSystem: {}", accountId, paymentSystem);
        
        kafkaTemplate.send("client_cards", Map.of(
                "accountId", accountId,
                "paymentSystem", paymentSystem
        ));
        
        log.info("Card request sent to Kafka topic: client_cards");
        return ResponseEntity.accepted().build();
    }
}


