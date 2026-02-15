package com.ffb.model.objects.cart;

import java.util.List;
import java.util.UUID;

public class Cart {

	private UUID id;
	private UUID accountID;
	private boolean hasPrio;
	private double total;
	private List<CartItem> cartItems;

	public Cart(UUID id, UUID accountID, boolean hasPrio, double total, List<CartItem> cartItems) {
		super();
		this.id = id;
		this.accountID = accountID;
		this.hasPrio = hasPrio;
		this.total = total;
		this.cartItems = cartItems;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getAccountID() {
		return accountID;
	}

	public void setAccountID(UUID accountID) {
		this.accountID = accountID;
	}

	public boolean isHasPrio() {
		return hasPrio;
	}

	public void setHasPrio(boolean hasPrio) {
		this.hasPrio = hasPrio;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public List<CartItem> getCartItems() {
		return cartItems;
	}

	public void setCartItems(List<CartItem> cartItems) {
		this.cartItems = cartItems;
	}

	@Override
	public String toString() {
		return "Cart [id=" + id + ", accountID=" + accountID + ", hasPrio=" + hasPrio + ", total=" + total
				+ ", cartItems=" + cartItems + "]";
	}

}
