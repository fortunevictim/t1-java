package ru.t1.apupynin.accountms.entity;

import jakarta.persistence.*;
import ru.t1.apupynin.accountms.enums.CardStatus;
import ru.t1.apupynin.accountms.enums.PaymentSystem;
import java.util.Objects;

@Entity
@Table(name = "cards")
public class Card {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "account_id", nullable = false)
    private Long accountId;
    
    @Column(name = "card_id", nullable = false, unique = true)
    private String cardId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_system", nullable = false)
    private PaymentSystem paymentSystem;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CardStatus status;



    public Card() {}
    
    public Card(Long accountId, String cardId, PaymentSystem paymentSystem, CardStatus status) {
        this.accountId = accountId;
        this.cardId = cardId;
        this.paymentSystem = paymentSystem;
        this.status = status;
    }



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getAccountId() { return accountId; }
    public void setAccountId(Long accountId) { this.accountId = accountId; }
    public String getCardId() { return cardId; }
    public void setCardId(String cardId) { this.cardId = cardId; }
    public PaymentSystem getPaymentSystem() { return paymentSystem; }
    public void setPaymentSystem(PaymentSystem paymentSystem) { this.paymentSystem = paymentSystem; }
    public CardStatus getStatus() { return status; }
    public void setStatus(CardStatus status) { this.status = status; }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id, card.id);
    }
    
    @Override
    public int hashCode() { return Objects.hash(id); }
    
    @Override
    public String toString() {
        return "Card{id=" + id + ", cardId='" + cardId + "', status=" + status + "}";
    }
}
