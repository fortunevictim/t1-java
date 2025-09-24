package ru.t1.apupynin.creditms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "product_registry")
public class ProductRegistry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "client_id", nullable = false)
    private Long clientId;
    
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "interest_rate", precision = 5, scale = 4)
    private BigDecimal interestRate;
    
    @Column(name = "open_date", nullable = false)
    private LocalDateTime openDate;
    


    public ProductRegistry() {}
    
    public ProductRegistry(Long clientId, Long accountId, Long productId, 
                          BigDecimal interestRate, LocalDateTime openDate) {
        this.clientId = clientId;
        this.accountId = accountId;
        this.productId = productId;
        this.interestRate = interestRate;
        this.openDate = openDate;
    }
    


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
    public LocalDateTime getOpenDate() { return openDate; }
    public void setOpenDate(LocalDateTime openDate) { this.openDate = openDate; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductRegistry that = (ProductRegistry) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() { return Objects.hash(id); }
    
    @Override
    public String toString() {
        return "ProductRegistry{id=" + id + ", clientId=" + clientId + ", productId=" + productId + "}";
    }
}
