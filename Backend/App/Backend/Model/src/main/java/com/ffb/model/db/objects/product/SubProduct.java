package com.ffb.model.objects.product;

import java.util.UUID;

public class SubProduct extends Product {
	
	private UUID mainProductId;

	public SubProduct(UUID id, UUID foodcourtID, double price, String displayName, byte symbol, int minimalWarning, UUID mainProductId) {
		super(id, foodcourtID, price, displayName, symbol, minimalWarning);
		this.mainProductId = mainProductId;
	}

	public UUID getMainProductId() {
		return mainProductId;
	}

	public void setMainProductId(UUID mainProductId) {
		this.mainProductId = mainProductId;
	}

	@Override
	public String toString() {
		return "SubProduct [mainProductId=" + mainProductId + "]";
	}
	
}
