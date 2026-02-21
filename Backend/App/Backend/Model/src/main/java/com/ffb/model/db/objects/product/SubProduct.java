package com.ffb.model.db.objects.product;

import java.util.UUID;

import com.ffb.model.db.objects.food_court.FoodCourt;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Immutable
@Table(name = "v_sub_product", schema = "ffb")
public class SubProduct extends ProductPrototype {

    @Id
    @Column(name = "v_id")
    private UUID viewId;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "main_product_id", referencedColumnName = "id")
	private MainProduct mainProduct;

    protected SubProduct() {}

    public SubProduct(UUID id, double price, String displayName, String symbolIdentifier, int minimalWarning, FoodCourt foodCourt,  UUID viewId, MainProduct mainProduct) {
        super(id, price, displayName, symbolIdentifier, minimalWarning, foodCourt);
        this.viewId = viewId;
        this.mainProduct = mainProduct;
    }
}
