package ru.t1.apupynin.clientms.entity;

import jakarta.persistence.*;
import ru.t1.apupynin.clientms.enums.DocumentType;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;
    
    @Column(name = "first_name", nullable = false)
    private String firstName;
    
    @Column(name = "middle_name")
    private String middleName;
    
    @Column(name = "last_name", nullable = false)
    private String lastName;
    
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private DocumentType documentType;
    
    @Column(name = "document_id", nullable = false)
    private String documentId;
    
    @Column(name = "document_prefix")
    private String documentPrefix;
    
    @Column(name = "document_suffix")
    private String documentSuffix;
    


    public Client() {}
    
    public Client(String clientId, Long userId, String firstName, String middleName, 
                  String lastName, LocalDate dateOfBirth, DocumentType documentType, 
                  String documentId, String documentPrefix, String documentSuffix) {
        this.clientId = clientId;
        this.userId = userId;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.documentType = documentType;
        this.documentId = documentId;
        this.documentPrefix = documentPrefix;
        this.documentSuffix = documentSuffix;
    }
    


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getMiddleName() {
        return middleName;
    }
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    public DocumentType getDocumentType() {
        return documentType;
    }
    public void setDocumentType(DocumentType documentType) {
        this.documentType = documentType;
    }
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }
    public String getDocumentPrefix() {
        return documentPrefix;
    }
    public void setDocumentPrefix(String documentPrefix) {
        this.documentPrefix = documentPrefix;
    }
    public String getDocumentSuffix() {
        return documentSuffix;
    }
    public void setDocumentSuffix(String documentSuffix) {
        this.documentSuffix = documentSuffix;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", clientId='" + clientId + '\'' +
                ", userId=" + userId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
