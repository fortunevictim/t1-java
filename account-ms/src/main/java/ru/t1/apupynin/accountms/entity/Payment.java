package ru.t1.apupynin.accountms.entity;

import jakarta.persistence.*;
import ru.t1.apupynin.accountms.enums.PaymentType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "payments")
public class Payment {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    
    @Column(name = "payment_date", nullable = false)
    private LocalDateTime paymentDate;
    
    @Column(name = "amount", nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "is_credit", nullable = false)
    private Boolean isCredit;
    
    @Column(name = "payed_at")
    private LocalDateTime payedAt;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PaymentType type;
    


    public Payment() {}
    
    public Payment(Long accountId, LocalDateTime paymentDate, BigDecimal amount, 
                  Boolean isCredit, LocalDateTime payedAt, PaymentType type) {
        this.accountId = accountId;
        this.paymentDate = paymentDate;
        this.amount = amount;
        this.isCredit = isCredit;
        this.payedAt = payedAt;
        this.type = type;
    }
    


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public Boolean getIsCredit() { return isCredit; }
    public void setIsCredit(Boolean isCredit) { this.isCredit = isCredit; }
    public LocalDateTime getPayedAt() { return payedAt; }
    public void setPayedAt(LocalDateTime payedAt) { this.payedAt = payedAt; }
    public PaymentType getType() { return type; }
    public void setType(PaymentType type) { this.type = type; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Payment payment = (Payment) o;
        return Objects.equals(id, payment.id);
    }
    
    @Override
    public int hashCode() { return Objects.hash(id); }
    
    @Override
    public String toString() {
        return "Payment{id=" + id + ", accountId=" + accountId + ", amount=" + amount + ", type=" + type + "}";
    }
}
