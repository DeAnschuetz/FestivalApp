package com.ffb.model.db.object.product;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "product_count", schema = "ffb")
public class ProductCount extends PanacheEntityBase {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;

    @JdbcTypeCode(SqlTypes.INTEGER)
    @Column(name = "product_count")
	private int productCount;
    
    @JsonIgnore
    @JdbcTypeCode(SqlTypes.UUID)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "product_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_product"),
            unique = true,
            nullable = false
    )
    private Product product;
    
    protected ProductCount() {}

    public ProductCount(Product product) {
        this.id = UUID.randomUUID();
        this.productCount = 0;
        this.product = product;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getProductCount() {
        return productCount;
    }

    public void setProductCount(int productCount) {
        this.productCount = productCount;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
