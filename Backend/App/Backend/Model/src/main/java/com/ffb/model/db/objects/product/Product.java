package com.ffb.model.db.objects.product;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.objects.food_court.FoodCourt;

import com.ffb.model.db.objects.foodorder.FoodOrderItem;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

@Entity
@Table(name = "product", schema = "ffb")
public class Product extends PanacheEntityBase {

	@Id
	@Column(name = "id")
	private UUID id;

	@Column(name = "price")
	private double price;

	@Column(name = "display_name")
	private String displayName;

	@Column(name = "symbol_identifier")
	private String symbolIdentifier;

	@Column(name = "minimal_warning")
	private int minimalWarning;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_court_id", referencedColumnName = "id")
	private FoodCourt foodCourt;

	@JsonIgnore
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<FoodOrderItem> foodOrderItems;

	@JsonIgnore
	@OneToMany(mappedBy = "mainProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MainSubProductLink> subLinks;

	@JsonIgnore
	@OneToMany(mappedBy = "subProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MainSubProductLink> mainLinks;

	protected Product() {}

	public Product(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning, FoodCourt foodCourt) {
		this.id = id;
		this.price = price;
		this.displayName = displayName;
		this.symbolIdentifier = symbolIdentifier;
		this.minimalWarning = minimalWarning;
		this.foodCourt = foodCourt;
	}

	@Transient
	public List<Product> getSubProducts() {
		if (subLinks == null) {
			List<Product> products = Collections.emptyList();
			return products;
		}
		return subLinks.stream()
				.map(MainSubProductLink::getSubProduct)
				.collect(Collectors.toList());
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

	public FoodCourt getFoodCourt() {
		return foodCourt;
	}

	public void setFoodCourt(FoodCourt foodCourt) {
		this.foodCourt = foodCourt;
	}

	public List<FoodOrderItem> getFoodOrderItems() {
		return foodOrderItems;
	}

	public void setFoodOrderItems(List<FoodOrderItem> foodOrderItems) {
		this.foodOrderItems = foodOrderItems;
	}
}
