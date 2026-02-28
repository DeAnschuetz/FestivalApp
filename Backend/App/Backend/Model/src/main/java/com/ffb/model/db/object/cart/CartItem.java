package com.ffb.model.db.object.cart;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.ffb.model.db.object.product.Product;
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
@Table(name = "cart_item", schema = "ffb")
public class CartItem extends PanacheEntityBase {

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
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;
    
    @JsonIgnore
    @JdbcTypeCode(SqlTypes.UUID)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;
	
	protected CartItem() {}

    public CartItem(UUID id, double price, int itemCount, String extra, Cart cart, Product product) {
        this.id = id;
        this.price = price;
        this.itemCount = itemCount;
        this.extra = extra;
        this.cart = cart;
        this.product = product;
    }

    public CartItem(UUID id, double price, int itemCount, String extra) {
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

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
