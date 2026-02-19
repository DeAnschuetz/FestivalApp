package com.ffb.model.objects.product;

import java.util.UUID;

public class Product {

	private UUID id;
	private UUID foodcourtID;
	private double price;
	private String displayName;
	private byte symbol;
	private int minimalWarning;

	public Product(UUID id, UUID foodcourtID, double price, String displayName, byte symbol, int minimalWarning) {
		super();
		this.id = id;
		this.foodcourtID = foodcourtID;
		this.price = price;
		this.displayName = displayName;
		this.symbol = symbol;
		this.minimalWarning = minimalWarning;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getFoodcourtID() {
		return foodcourtID;
	}

	public void setFoodcourtID(UUID foodcourtID) {
		this.foodcourtID = foodcourtID;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public byte getSymbol() {
		return symbol;
	}

	public void setSymbol(byte symbol) {
		this.symbol = symbol;
	}

	public int getMinimalWarning() {
		return minimalWarning;
	}

	public void setMinimalWarning(int minimalWarning) {
		this.minimalWarning = minimalWarning;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", foodcourtID=" + foodcourtID + ", price=" + price + ", displayName="
				+ displayName + ", symbol=" + symbol + ", minimalWarning=" + minimalWarning + "]";
	}

}
