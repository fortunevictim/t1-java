package ru.t1.apupynin.clientms.entity;

import jakarta.persistence.*;
import ru.t1.apupynin.clientms.enums.ProductKey;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "key", nullable = false)
    private ProductKey key;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "create_date", nullable = false)
    private LocalDateTime createDate;
    
    @Column(name = "product_id", unique = true, nullable = false)
    private String productId;



    public Product() {}
    
    public Product(ProductKey key, String name, LocalDateTime createDate, String productId) {
        this.key = key;
        this.name = name;
        this.createDate = createDate;
        this.productId = productId;
    }



    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public ProductKey getKey() {
        return key;
    }
    public void setKey(ProductKey key) {
        this.key = key;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public LocalDateTime getCreateDate() {
        return createDate;
    }
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }
    public String getProductId() {
        return productId;
    }
    public void setProductId(String productId) {
        this.productId = productId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", productId='" + productId + '\'' +
                '}';
    }
}
