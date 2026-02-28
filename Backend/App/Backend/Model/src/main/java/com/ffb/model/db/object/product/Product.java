package com.ffb.model.db.object.product;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ffb.model.db.object.food_court.FoodCourt;

import com.ffb.model.db.object.foodorder.FoodOrderItem;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "product", schema = "ffb")
public class Product extends PanacheEntityBase {

	@Id
	@JdbcTypeCode(SqlTypes.UUID)
	@Column(name = "id")
	private UUID id;

	@JdbcTypeCode(SqlTypes.DECIMAL)
	@Column(name = "price", precision = 10, scale = 2)
	private double price;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "display_name", length = 100, nullable = false)
	private String displayName;

	@JdbcTypeCode(SqlTypes.VARCHAR)
	@Column(name = "symbol_identifier", length =  100, nullable = false)
	private String symbolIdentifier;

	@JdbcTypeCode(SqlTypes.INTEGER)
	@Column(name = "minimal_warning")
	private int minimalWarning;

	@JsonIgnore
	@JdbcTypeCode(SqlTypes.UUID)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "food_court_id", referencedColumnName = "id")
	private FoodCourt foodCourt;

	@JsonIgnore
	@OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
	private ProductCount productCount;

	@JsonIgnore
	@OneToMany(mappedBy = "mainProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MainSubProductLink> subLinks;

	@JsonIgnore
	@OneToMany(mappedBy = "subProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<MainSubProductLink> mainLinks;

	protected Product() {}

	public Product(double price, String displayName, String symbolIdentifier, int minimalWarning, FoodCourt foodCourt) {
		this.id = UUID.randomUUID();
		this.price = price;
		this.displayName = displayName;
		this.symbolIdentifier = symbolIdentifier;
		this.minimalWarning = minimalWarning;
		this.foodCourt = foodCourt;
		this.productCount = new ProductCount(
				this
		);
	}

	public Product(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning, FoodCourt foodCourt) {
		this.id = id;
		this.price = price;
		this.displayName = displayName;
		this.symbolIdentifier = symbolIdentifier;
		this.minimalWarning = minimalWarning;
		this.foodCourt = foodCourt;
		this.productCount = new ProductCount(
				this
		);
	}

	@Transient
	public List<Product> getSubProducts() {
		if (subLinks == null) {
            return Collections.emptyList();
		}
		return subLinks.stream()//
				.map(MainSubProductLink::getSubProduct)//
				.collect(Collectors.toList())//
		;
	}

	@Transient
	public int getCount() {
		if(this.productCount == null) {
			this.productCount = new ProductCount(
					this
			);
		}
		return this.productCount.getProductCount();
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

	public ProductCount getProductCount() {
		return productCount;
	}

	public void setProductCount(ProductCount productCount) {
		this.productCount = productCount;
	}

	public List<MainSubProductLink> getSubLinks() {
		return subLinks;
	}

	public void setSubLinks(List<MainSubProductLink> subLinks) {
		this.subLinks = subLinks;
	}

	public List<MainSubProductLink> getMainLinks() {
		return mainLinks;
	}

	public void setMainLinks(List<MainSubProductLink> mainLinks) {
		this.mainLinks = mainLinks;
	}
}
