package com.ffb.model.db.objects.product;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private MainProduct product;
    
    protected ProductCount() {}

    public ProductCount(UUID id, int productCount, MainProduct product) {
        this.id = id;
        this.productCount = productCount;
        this.product = product;
    }

    public ProductCount(UUID id, int productCount) {
		super();
		this.id = id;
		this.productCount = productCount;
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

    public MainProduct getProduct() {
        return product;
    }

    public void setProduct(MainProduct product) {
        this.product = product;
    }
}
