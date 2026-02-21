package com.ffb.model.db.objects.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class MainSubProductId implements Serializable {

    @Column(name = "main_product_id")
    public UUID mainProductId;

    @Column(name = "sub_product_id")
    public UUID subProductId;

    protected MainSubProductId() {}

    public MainSubProductId(UUID mainId, UUID subId) {
        this.mainProductId = mainId;
        this.subProductId = subId;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    @Override public int hashCode() {
        return 0;
    }
}
