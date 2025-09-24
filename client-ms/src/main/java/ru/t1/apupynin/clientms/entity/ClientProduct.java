package ru.t1.apupynin.clientms.entity;

import jakarta.persistence.*;
import ru.t1.apupynin.clientms.enums.ClientProductStatus;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "client_products")
public class ClientProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "client_id", nullable = false)
    private Long clientId;
    
    @Column(name = "product_id", nullable = false)
    private Long productId;
    
    @Column(name = "open_date", nullable = false)
    private LocalDateTime openDate;
    
    @Column(name = "close_date")
    private LocalDateTime closeDate;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ClientProductStatus status;
    


    public ClientProduct() {}
    
    public ClientProduct(Long clientId, Long productId, LocalDateTime openDate, 
                        LocalDateTime closeDate, ClientProductStatus status) {
        this.clientId = clientId;
        this.productId = productId;
        this.openDate = openDate;
        this.closeDate = closeDate;
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
    public LocalDateTime getOpenDate() {
        return openDate;
    }
    public void setOpenDate(LocalDateTime openDate) {
        this.openDate = openDate;
    }
    public LocalDateTime getCloseDate() {
        return closeDate;
    }
    public void setCloseDate(LocalDateTime closeDate) {
        this.closeDate = closeDate;
    }
    public ClientProductStatus getStatus() {
        return status;
    }
    public void setStatus(ClientProductStatus status) {
        this.status = status;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientProduct that = (ClientProduct) o;
        return Objects.equals(id, that.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "ClientProduct{" +
                "id=" + id +
                ", clientId=" + clientId +
                ", productId=" + productId +
                ", status=" + status +
                '}';
    }
}
