package ru.t1.apupynin.clientms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.t1.apupynin.clientms.entity.BlacklistRegistry;
import ru.t1.apupynin.clientms.enums.DocumentType;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BlacklistRegistryRepository extends JpaRepository<BlacklistRegistry, Long> {
    Optional<BlacklistRegistry> findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateAfter(
            DocumentType documentType,
            String documentId,
            LocalDateTime now
    );
    Optional<BlacklistRegistry> findFirstByDocumentTypeAndDocumentIdAndBlacklistExpirationDateIsNull(
            DocumentType documentType,
            String documentId
    );
}


