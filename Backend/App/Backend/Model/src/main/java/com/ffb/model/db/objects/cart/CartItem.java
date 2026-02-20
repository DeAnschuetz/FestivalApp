package com.ffb.model.db.objects.cart;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.product.MainProduct;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "cart_item", schema = "ffb")
public class CartItem extends PanacheEntityBase {


    @Id
    @Column(name = "id")
	private UUID id;
	
    @Column(name = "price")
	private double price;
	
    @Column(name = "item_count")
	private int itemCount;
	
    @Column(name = "extra")
	private String extra;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private MainProduct product;
	
	protected CartItem() {}

	public CartItem(UUID id,double price, int itemCount, String extra) {
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

    public MainProduct getProduct() {
        return product;
    }

    public void setProduct(MainProduct product) {
        this.product = product;
    }
}
