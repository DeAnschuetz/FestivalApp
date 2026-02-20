package com.ffb.model.db.objects.product;

import java.util.UUID;

import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Immutable
@Table(name = "v_sub_product", schema = "ffb")
public class SubProduct extends ProductPrototype {
	
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_product_id", referencedColumnName = "id")
	private MainProduct mainProduct;
    
    protected SubProduct() {}

	public SubProduct(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning) {
		super(id, price, displayName, symbolIdentifier, minimalWarning);
	}
}
