package com.ffb.model.objects.foodorder;

import java.util.UUID;

public class FoodOrderItem {

	private UUID id;
	private UUID orderID;
	private UUID productID;
	private double price;
	private int itemCount;
	private String extra;

	public FoodOrderItem(UUID id, UUID orderID, UUID productID, double price, int itemCount, String extra) {
		super();
		this.id = id;
		this.orderID = orderID;
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

	public UUID getOrderID() {
		return orderID;
	}

	public void setOrderID(UUID orderID) {
		this.orderID = orderID;
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
		return "FoodOrderItems [id=" + id + ", orderID=" + orderID + ", productID=" + productID + ", price=" + price
				+ ", itemCount=" + itemCount + ", extra=" + extra + "]";
	}

}
