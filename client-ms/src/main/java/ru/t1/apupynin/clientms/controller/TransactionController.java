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
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public TransactionController(KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER','GRAND_EMPLOYEE','CURRENT_CLIENT')")
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public ResponseEntity<Void> createTransaction(@RequestBody TransactionRequest request) {
        log.info("Transaction request received: {}", request);
        
        String messageKey = UUID.randomUUID().toString();
        
        Map<String, Object> payload = Map.of(
                "accountId", request.getAccountId(),
                "cardId", request.getCardId(),
                "type", request.getType(),
                "amount", request.getAmount()
        );
        
        kafkaTemplate.send("client_transactions", messageKey, payload);
        
        log.info("Transaction sent to Kafka topic: client_transactions with key: {}", messageKey);
        return ResponseEntity.accepted().build();
    }
    
    public static class TransactionRequest {
        private Long accountId;
        private Long cardId;
        private String type;
        private Double amount;
        
        public Long getAccountId() { return accountId; }
        public void setAccountId(Long accountId) { this.accountId = accountId; }
        
        public Long getCardId() { return cardId; }
        public void setCardId(Long cardId) { this.cardId = cardId; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        
        @Override
        public String toString() {
            return "TransactionRequest{" +
                    "accountId=" + accountId +
                    ", cardId=" + cardId +
                    ", type='" + type + '\'' +
                    ", amount=" + amount +
                    '}';
        }
    }
}

