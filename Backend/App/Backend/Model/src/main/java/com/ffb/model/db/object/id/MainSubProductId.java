package com.ffb.model.db.object.id;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class MainSubProductId implements Serializable {

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "main_product_id")
    public UUID mainProductId;

    @JdbcTypeCode(SqlTypes.UUID)
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
