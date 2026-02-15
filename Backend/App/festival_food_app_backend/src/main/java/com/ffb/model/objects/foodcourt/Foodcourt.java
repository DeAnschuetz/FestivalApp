package com.ffb.model.objects.foodcourt;

import java.util.UUID;

public class Foodcourt {

	private UUID id;
	private UUID accountID;
	private String displayName;
	private byte image;

	public Foodcourt(UUID id, UUID accountID, String displayName, byte image) {
		super();
		this.id = id;
		this.accountID = accountID;
		this.displayName = displayName;
		this.image = image;
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

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public byte getImage() {
		return image;
	}

	public void setImage(byte image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "Foodcourt [id=" + id + ", accountID=" + accountID + ", displayName=" + displayName + ", image=" + image
				+ "]";
	}

}
