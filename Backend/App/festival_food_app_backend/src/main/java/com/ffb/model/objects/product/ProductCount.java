package com.ffb.model.objects.product;

import java.util.UUID;

public class ProductCount {

	private UUID productID;
	private int productCount;

	public ProductCount(UUID productID, int productCount) {
		super();
		this.productID = productID;
		this.productCount = productCount;
	}

	public UUID getProductID() {
		return productID;
	}

	public void setProductID(UUID productID) {
		this.productID = productID;
	}

	public int getProductCount() {
		return productCount;
	}

	public void setProductCount(int productCount) {
		this.productCount = productCount;
	}

	@Override
	public String toString() {
		return "ProductCount [productID=" + productID + ", productCount=" + productCount + "]";
	}

}
