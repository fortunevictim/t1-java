package ru.t1.apupynin.accountms.entity;

import jakarta.persistence.*;
import ru.t1.apupynin.accountms.enums.AccountStatus;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "client_id", nullable = false)
    private Long clientId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "balance", nullable = false, precision = 19, scale = 2)
    private BigDecimal balance;
    
    @Column(name = "interest_rate", precision = 5, scale = 4)
    private BigDecimal interestRate;
    
    @Column(name = "is_recalc", nullable = false)
    private Boolean isRecalc;
    
    @Column(name = "card_exist", nullable = false)
    private Boolean cardExist;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;



    public Account() {}
    
    public Account(Long clientId, Long productId, BigDecimal balance, 
                  BigDecimal interestRate, Boolean isRecalc, 
                  Boolean cardExist, AccountStatus status) {
        this.clientId = clientId;
        this.productId = productId;
        this.balance = balance;
        this.interestRate = interestRate;
        this.isRecalc = isRecalc;
        this.cardExist = cardExist;
        this.status = status;
    }



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getClientId() {
        return clientId;
    }
    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
    public Long getProductId() {
        return productId;
    }
    public void setProductId(Long productId) {
        this.productId = productId;
    }
    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    public void setInterestRate(BigDecimal interestRate) {
        this.interestRate = interestRate;
    }
    public Boolean getIsRecalc() {
        return isRecalc;
    }
    public void setIsRecalc(Boolean isRecalc) {
        this.isRecalc = isRecalc;
    }
    public Boolean getCardExist() {
        return cardExist;
    }
    public void setCardExist(Boolean cardExist) {
        this.cardExist = cardExist;
    }
    public AccountStatus getStatus() {
        return status;
    }
    public void setStatus(AccountStatus status) {
        this.status = status;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(id, account.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", productId=" + productId +
                ", balance=" + balance +
                ", status=" + status +
                '}';
    }
}
