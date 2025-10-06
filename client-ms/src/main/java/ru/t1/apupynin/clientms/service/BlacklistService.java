package ru.t1.apupynin.clientms.service;

import org.springframework.stereotype.Service;
import ru.t1.apupynin.clientms.enums.DocumentType;
import ru.t1.apupynin.clientms.repository.BlacklistRegistryRepository;
import ru.t1.apupynin.common.aspects.annotation.Cached;

import java.time.LocalDateTime;

@Service
public class BlacklistService {

    private final BlacklistRegistryRepository blacklistRepository;

    public BlacklistService(BlacklistRegistryRepository blacklistRepository) {
        this.blacklistRepository = blacklistRepository;
    }

    @Cached(cacheName = "blacklist")
    public boolean isInBlacklist(DocumentType documentType, String documentId) {
        LocalDateTime now = LocalDateTime.now();
        return blacklistRepository.findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateAfter(documentType, documentId, now).isPresent()
                || blacklistRepository.findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateIsNull(documentType, documentId).isPresent();
    }
}


