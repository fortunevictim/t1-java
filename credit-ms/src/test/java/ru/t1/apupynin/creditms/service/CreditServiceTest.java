package ru.t1.apupynin.creditms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import ru.t1.apupynin.creditms.entity.ProductRegistry;
import ru.t1.apupynin.creditms.repository.ProductRegistryRepository;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {

    @Mock ProductRegistryRepository productRegistryRepository;
    @Mock KafkaTemplate<String, Map<String, Object>> kafkaTemplate;

    @InjectMocks CreditService creditService;

    @Test
    void processCreditProduct_missingFields_returns() {
        creditService.processCreditProduct(Map.of("clientId", 1L), 1_000_000L);
        verifyNoInteractions(productRegistryRepository, kafkaTemplate);
    }

    @Test
    void processCreditProduct_savesRegistry_andSendsKafka() {
        when(productRegistryRepository.save(any(ProductRegistry.class))).thenAnswer(inv -> {
            ProductRegistry pr = inv.getArgument(0);
            pr.setId(42L);
            return pr;
        });

        creditService.processCreditProduct(Map.of("clientId", 1L, "productId", 2L), 1_000_000L);

        verify(productRegistryRepository).save(any(ProductRegistry.class));
        verify(kafkaTemplate).send(eq("client_credit_opened"), any(Map.class));
    }
}
