package com.ffb.model.db.objects.foodcourt;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.foodorder.FoodOrder;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "foodcourt", schema = "ffb")
public class Foodcourt extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;
    
    @Column(name = "account_id")
	private UUID accountID;
    
    @Column(name = "display_name")
	private String displayName;
    
    @Column(name = "image_uri")
	private URI imageURI;
    
	private File image;
    
    @OneToOne(mappedBy = "foodcourt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private FoodcourtWaitingTime waitingTime;
    
    @JsonIgnore
    @OneToMany(mappedBy = "foodcourt", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<FoodOrder> foodOrder;
    
    protected Foodcourt() {}
    
	public Foodcourt(UUID id, UUID accountID, String displayName, URI imageURI) {
		super();
		this.id = id;
		this.accountID = accountID;
		this.displayName = displayName;
		this.imageURI = imageURI;
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

	public URI getImageURI() {
		return imageURI;
	}

	public void setImageURI(URI imageURI) {
		this.imageURI = imageURI;
	}

	public File getImage() {
		return image;
	}

	public void setImage(File image) {
		this.image = image;
	}

	public FoodcourtWaitingTime getWaitingTime() {
		return waitingTime;
	}

	public void setWaitingTime(FoodcourtWaitingTime waitingTime) {
		this.waitingTime = waitingTime;
	}

	public List<FoodOrder> getFoodOrder() {
		return foodOrder;
	}

	public void setFoodOrder(List<FoodOrder> foodOrder) {
		this.foodOrder = foodOrder;
	}
}
