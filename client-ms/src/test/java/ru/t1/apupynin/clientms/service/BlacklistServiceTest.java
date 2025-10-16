package ru.t1.apupynin.clientms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.t1.apupynin.clientms.entity.BlacklistRegistry;
import ru.t1.apupynin.clientms.enums.DocumentType;
import ru.t1.apupynin.clientms.repository.BlacklistRegistryRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlacklistServiceTest {

    @Mock
    BlacklistRegistryRepository blacklistRegistryRepository;

    @Test
    void isInBlacklist_true_ifTemporaryNotExpired() {
        when(blacklistRegistryRepository.findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateAfter(
                any(), anyString(), any(LocalDateTime.class)
        )).thenReturn(Optional.of(mock(BlacklistRegistry.class)));

        BlacklistService service = new BlacklistService(blacklistRegistryRepository);
        assertTrue(service.isInBlacklist(DocumentType.PASSPORT, "123"));
    }

    @Test
    void isInBlacklist_true_ifPermanent() {
        when(blacklistRegistryRepository.findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateIsNull(any(), anyString()))
                .thenReturn(Optional.of(mock(BlacklistRegistry.class)));

        BlacklistService service = new BlacklistService(blacklistRegistryRepository);
        assertTrue(service.isInBlacklist(DocumentType.PASSPORT, "123"));
    }

    @Test
    void isInBlacklist_false_ifAbsent() {
        when(blacklistRegistryRepository.findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateAfter(
                any(), anyString(), any(LocalDateTime.class)
        )).thenReturn(Optional.empty());
        when(blacklistRegistryRepository.findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateIsNull(any(), anyString()))
                .thenReturn(Optional.empty());

        BlacklistService service = new BlacklistService(blacklistRegistryRepository);
        assertFalse(service.isInBlacklist(DocumentType.PASSPORT, "123"));
    }
}
