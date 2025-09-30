package ru.t1.apupynin.accountms.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.apupynin.accountms.service.AccountService;

import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class KafkaClientConsumer {

    private final AccountService accountService;

    public KafkaClientConsumer(AccountService accountService) {
        this.accountService = accountService;
    }

	@KafkaListener(id = "${t1.kafka.consumer.group-id:account-ms-group}",
            topics = {"client_products", "client_transactions", "client_cards", "client_credit_opened", "client_payments"},
			containerFactory = "kafkaListenerContainerFactory")
	public void listener(@Payload List<Object> messageList,
						 Acknowledgment ack,
						 @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
						 @Header(KafkaHeaders.RECEIVED_KEY) String key,
						 @Header(value = KafkaHeaders.NATIVE_HEADERS, required = false) Map<String, Object> headers) {
		log.debug("Account consumer: processing new messages");
		try {
			log.info("Topic: {}", topic);
			log.info("Key: {}", key);
			switch (topic) {
                case "client_products":
                    messageList.forEach(msg -> {
                        if (msg instanceof Map) {
                            accountService.handleClientProducts((Map<String, Object>) msg);
                        } else {
                            log.warn("Unexpected payload type for client_products: {}", msg.getClass());
                        }
                    });
                    break;
                case "client_transactions":
                    log.info("Processing {} client_transactions messages", messageList.size());
                    messageList.forEach(msg -> {
                        log.info("Processing client_transactions message: {}", msg);
                        if (msg instanceof Map) {
                            accountService.handleClientTransactions((Map<String, Object>) msg);
                        } else {
                            log.warn("Unexpected payload type for client_transactions: {}", msg.getClass());
                        }
                    });
                    break;
                case "client_credit_opened":
                    messageList.forEach(msg -> {
                        if (msg instanceof Map) {
                            accountService.handleClientProducts((Map<String, Object>) msg);
                        } else {
                            log.warn("Unexpected payload type for client_credit_opened: {}", msg.getClass());
                        }
                    });
                    break;
                case "client_cards":
                    log.info("Processing {} client_cards messages", messageList.size());
                    messageList.forEach(msg -> {
                        log.info("Processing client_cards message: {}", msg);
                        if (msg instanceof Map) {
                            accountService.handleClientCards((Map<String, Object>) msg);
                        } else {
                            log.warn("Unexpected payload type for client_cards: {}", msg.getClass());
                        }
                    });
                    break;
                case "client_payments":
                    log.info("Processing {} client_payments messages", messageList.size());
                    messageList.forEach(msg -> {
                        log.info("Processing client_payments message: {}", msg);
                        if (msg instanceof Map) {
                            accountService.handleClientPayments((Map<String, Object>) msg);
                        } else {
                            log.warn("Unexpected payload type for client_payments: {}", msg.getClass());
                        }
                    });
                    break;
				default:
					log.warn("Unknown topic: {}", topic);
			}
		} finally {
			ack.acknowledge();
		}
		log.debug("Account consumer: messages processed");
	}
}
