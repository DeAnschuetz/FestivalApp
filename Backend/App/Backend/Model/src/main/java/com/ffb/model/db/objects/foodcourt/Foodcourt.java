package com.ffb.model.objects.foodcourt;

import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "foodcourt", schema = "ffb")
public class Foodcourt extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;
    
    @Column(name = "accountId")
	private UUID accountID;
    
    @Column(name = "display_name")
	private String displayName;
    
    @Column(name = "image")
	private byte image;
    
    protected Foodcourt() {}
    
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
