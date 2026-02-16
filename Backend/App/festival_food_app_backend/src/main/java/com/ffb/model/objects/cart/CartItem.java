package com.ffb.model.objects.cart;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", referencedColumnName = "id")
    private Cart cart;
	
    @Column(name = "product_id")
	private UUID productID;
	
    @Column(name = "price")
	private double price;
	
    @Column(name = "item_count")
	private int itemCount;
	
    @Column(name = "extra")
	private String extra;
	
	protected CartItem() {}

	public CartItem(UUID id, Cart cart, UUID productID, double price, int itemCount, String extra) {
		super();
		this.id = id;
		this.cart = cart;
		this.productID = productID;
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
	
	@JsonIgnore
	public Cart getCart() {
		return cart;
	}

	@JsonIgnore
	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public UUID getProductID() {
		return productID;
	}

	public void setProductID(UUID productID) {
		this.productID = productID;
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

	@Override
	public String toString() {
		return "CartItem [id=" + id + ", productID=" + productID + ", price=" + price
				+ ", itemCount=" + itemCount + ", extra=" + extra + "]";
	}

}
