package ru.t1.apupynin.creditms.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.apupynin.creditms.service.CreditService;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class CreditConsumer {

    private final CreditService creditService;
    private final long creditLimit;

    public CreditConsumer(CreditService creditService,
                          @Value("${credit.limit:1000000}") long creditLimit) {
        this.creditService = creditService;
        this.creditLimit = creditLimit;
    }

    @KafkaListener(id = "credit-ms-group",
            topics = {"client_credit_products"},
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<Object> messages,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        try {
            for (Object msg : messages) {
                if (msg instanceof Map) {
                    creditService.processCreditProduct((Map<String, Object>) msg, creditLimit);
                } else {
                    log.warn("Unexpected payload type: {}", msg.getClass());
                }
            }
        } finally {
            ack.acknowledge();
        }
    }
}


