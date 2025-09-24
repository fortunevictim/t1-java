package ru.t1.apupynin.creditms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payment_registry")
public class PaymentRegistry {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "product_registry_id", nullable = false)
    private Long productRegistryId;
    
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;
    
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "interest_rate_amount", precision = 19, scale = 2)
    private BigDecimal interestRateAmount;
    
    @Column(name = "debt_amount", precision = 19, scale = 2)
    private BigDecimal debtAmount;
    
    @Column(name = "expired", nullable = false)
    private Boolean expired;
    
    @Column(name = "payment_expiration_date")
    private LocalDateTime paymentExpirationDate;
    


    public PaymentRegistry() {}
    
    public PaymentRegistry(Long productRegistryId, LocalDateTime paymentDate, 
                          BigDecimal amount, BigDecimal interestRateAmount, 
                          BigDecimal debtAmount, Boolean expired, 
                          LocalDateTime paymentExpirationDate) {
        this.productRegistryId = productRegistryId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.interestRateAmount = interestRateAmount;
        this.debtAmount = debtAmount;
        this.expired = expired;
        this.paymentExpirationDate = paymentExpirationDate;
    }
    


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductRegistryId() { return productRegistryId; }
    public void setProductRegistryId(Long productRegistryId) { this.productRegistryId = productRegistryId; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public BigDecimal getInterestRateAmount() { return interestRateAmount; }
    public void setInterestRateAmount(BigDecimal interestRateAmount) { this.interestRateAmount = interestRateAmount; }
    public BigDecimal getDebtAmount() { return debtAmount; }
    public void setDebtAmount(BigDecimal debtAmount) { this.debtAmount = debtAmount; }
    public Boolean getExpired() { return expired; }
    public void setExpired(Boolean expired) { this.expired = expired; }
    public LocalDateTime getPaymentExpirationDate() { return paymentExpirationDate; }
    public void setPaymentExpirationDate(LocalDateTime paymentExpirationDate) { this.paymentExpirationDate = paymentExpirationDate; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentRegistry that = (PaymentRegistry) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() { return Objects.hash(id); }
    
    @Override
    public String toString() {
        return "PaymentRegistry{id=" + id + ", productRegistryId=" + productRegistryId + ", amount=" + amount + "}";
    }
}
