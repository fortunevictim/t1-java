package ru.t1.apupynin.clientms.entity;

import jakarta.persistence.*;
import ru.t1.apupynin.clientms.enums.DocumentType;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "blacklist_registry")
public class BlacklistRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;

    @Column(name = "document_id", nullable = false)
    private String documentId;

    @Column(name = "blacklisted_at", nullable = false)
    private LocalDateTime blacklistedAt;

    @Column(name = "reason")
    private String reason;

    @Column(name = "blacklist_expiration_date")
    private LocalDateTime blacklistExpirationDate;

    public BlacklistRegistry() {}

    public BlacklistRegistry(DocumentType documentType,
                             String documentId,
                             LocalDateTime blacklistedAt,
                             String reason,
                             LocalDateTime blacklistExpirationDate) {
        this.documentType = documentType;
        this.documentId = documentId;
        this.blacklistedAt = blacklistedAt;
        this.reason = reason;
        this.blacklistExpirationDate = blacklistExpirationDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public DocumentType getDocumentType() { return documentType; }
    public void setDocumentType(DocumentType documentType) { this.documentType = documentType; }
    public String getDocumentId() { return documentId; }
    public void setDocumentId(String documentId) { this.documentId = documentId; }
    public LocalDateTime getBlacklistedAt() { return blacklistedAt; }
    public void setBlacklistedAt(LocalDateTime blacklistedAt) { this.blacklistedAt = blacklistedAt; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public LocalDateTime getBlacklistExpirationDate() { return blacklistExpirationDate; }
    public void setBlacklistExpirationDate(LocalDateTime blacklistExpirationDate) { this.blacklistExpirationDate = blacklistExpirationDate; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlacklistRegistry that = (BlacklistRegistry) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() { return Objects.hash(id); }
}


