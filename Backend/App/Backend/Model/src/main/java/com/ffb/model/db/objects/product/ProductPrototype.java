package com.ffb.model.db.objects.product;

import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.foodcourt.Foodcourt;
import com.ffb.model.db.objects.foodorder.FoodOrderItem;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.OneToMany;

@MappedSuperclass
public class ProductPrototype extends PanacheEntityBase {

    @Id
    @Column(name = "id")
	private UUID id;

    @Column(name = "price")
	private double price;

    @Column(name = "display_namme")
	private String displayName;

    @Column(name = "symbol_identifier")
	private String symbolIdentifier;

    @Column(name = "minimal_warning")
	private int minimalWarning;
    
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "foodcourt_id", referencedColumnName = "id")
    private Foodcourt foodcourt;

    protected ProductPrototype() {}
    
	public ProductPrototype(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning) {
		super();
		this.id = id;
		this.price = price;
		this.displayName = displayName;
		this.symbolIdentifier = symbolIdentifier;
		this.minimalWarning = minimalWarning;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public String getSymbolIdentifier() {
		return symbolIdentifier;
	}

	public void setSymbolIdentifier(String symbolIdentifier) {
		this.symbolIdentifier = symbolIdentifier;
	}

	public int getMinimalWarning() {
		return minimalWarning;
	}

	public void setMinimalWarning(int minimalWarning) {
		this.minimalWarning = minimalWarning;
	}

	public Foodcourt getFoodcourt() {
		return foodcourt;
	}

	public void setFoodcourt(Foodcourt foodcourt) {
		this.foodcourt = foodcourt;
	}
}
