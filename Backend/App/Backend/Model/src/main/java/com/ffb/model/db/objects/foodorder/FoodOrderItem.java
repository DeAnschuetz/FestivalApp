package com.ffb.model.db.objects.foodorder;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.product.MainProduct;

import com.ffb.model.db.objects.product.Product;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;


@Entity
@Table(name = "food_order_item", schema = "ffb")
public class FoodOrderItem extends PanacheEntityBase {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "id")
	private UUID id;

    @JdbcTypeCode(SqlTypes.DECIMAL)
    @Column(name = "price")
    private double price;

    @JdbcTypeCode(SqlTypes.INTEGER)
    @Column(name = "item_count")
    private int itemCount;

    @JdbcTypeCode(SqlTypes.VARCHAR)
    @Column(name = "extra", length = 255, nullable = true)
    private String extra;
	
    @JsonIgnore
    @JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_order_id", referencedColumnName = "id")
	private FoodOrder foodOrder;
    
    @JsonIgnore
    @JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
    
    protected FoodOrderItem() {}

    public FoodOrderItem(UUID id, double price, int itemCount, String extra, FoodOrder foodOrder, Product product) {
        this.id = id;
        this.price = price;
        this.itemCount = itemCount;
        this.extra = extra;
        this.foodOrder = foodOrder;
        this.product = product;
    }

    public FoodOrderItem( UUID id ,double price, int itemCount, String extra, Product product) {
        this.extra = extra;
        this.itemCount = itemCount;
        this.price = price;
        this.id = id;
        this.product = product;
    }

    public FoodOrderItem(UUID id, double price, int itemCount, String extra) {
		super();
		this.id = id;
		this.price = price;
		this.itemCount = itemCount;
		this.extra = extra;
	}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public FoodOrder getFoodOrder() {
        return foodOrder;
    }

    public void setFoodOrder(FoodOrder foodOrder) {
        this.foodOrder = foodOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setMProduct(Product product) {
        this.product = product;
    }
}
