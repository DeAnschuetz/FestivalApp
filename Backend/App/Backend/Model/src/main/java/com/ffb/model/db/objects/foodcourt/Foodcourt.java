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

}
