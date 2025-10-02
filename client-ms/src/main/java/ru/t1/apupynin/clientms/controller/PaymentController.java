package ru.t1.apupynin.clientms.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;
import ru.t1.apupynin.clientms.annotation.HttpIncomeRequestLog;
import ru.t1.apupynin.clientms.annotation.HttpOutcomeRequestLog;
import ru.t1.apupynin.clientms.annotation.LogDatasourceError;

import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    public PaymentController(KafkaTemplate<String, Map<String, Object>> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping
    @HttpIncomeRequestLog
    @HttpOutcomeRequestLog
    @LogDatasourceError
    public ResponseEntity<Void> createPayment(@RequestBody PaymentRequest request) {
        log.info("Payment request received: {}", request);
        
        String messageKey = UUID.randomUUID().toString();
        
        Map<String, Object> payload = Map.of(
                "accountId", request.getAccountId(),
                "amount", request.getAmount()
        );
        
        kafkaTemplate.send("client_payments", messageKey, payload);
        
        log.info("Payment sent to Kafka topic: client_payments with key: {}", messageKey);
        return ResponseEntity.accepted().build();
    }
    
    public static class PaymentRequest {
        private Long accountId;
        private Double amount;
        
        public Long getAccountId() { return accountId; }
        public void setAccountId(Long accountId) { this.accountId = accountId; }
        
        public Double getAmount() { return amount; }
        public void setAmount(Double amount) { this.amount = amount; }
        
        @Override
        public String toString() {
            return "PaymentRequest{" +
                    "accountId=" + accountId +
                    ", amount=" + amount +
                    '}';
        }
    }
}

