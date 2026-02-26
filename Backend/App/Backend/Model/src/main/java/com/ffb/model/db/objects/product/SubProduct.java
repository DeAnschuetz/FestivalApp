package com.ffb.model.db.objects.product;

import java.util.UUID;

import com.ffb.model.db.objects.food_court.FoodCourt;
import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Immutable
@Table(name = "v_sub_product", schema = "ffb")
public class SubProduct extends ProductPrototype {

    @Id
    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "v_id")
    private UUID viewId;

    @JsonIgnore
    @JdbcTypeCode(SqlTypes.UUID)
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
