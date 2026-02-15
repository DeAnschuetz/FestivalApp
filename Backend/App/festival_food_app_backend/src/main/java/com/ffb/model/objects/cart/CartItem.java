package com.ffb.model.objects.cart;

import java.util.UUID;

public class CartItem {

	private UUID id;
	private UUID cartID;
	private UUID productID;
	private double price;
	private int itemCount;
	private String extra;

	public CartItem(UUID id, UUID cartID, UUID productID, double price, int itemCount, String extra) {
		super();
		this.id = id;
		this.cartID = cartID;
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

	public UUID getCartID() {
		return cartID;
	}

	public void setCartID(UUID cartID) {
		this.cartID = cartID;
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
		return "CartItem [id=" + id + ", cartID=" + cartID + ", productID=" + productID + ", price=" + price
				+ ", itemCount=" + itemCount + ", extra=" + extra + "]";
	}

}
