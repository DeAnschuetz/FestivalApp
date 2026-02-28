package com.ffb.app.repository.api.product;

import com.ffb.model.db.view.MainProduct;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;

import java.util.Optional;
import java.util.UUID;

public interface MainProductRepository extends PanacheRepositoryBase<MainProduct, UUID> {

    void flush(MainProduct entity);

    void update(MainProduct entity);

    void persist(MainProduct entity);

    Optional<MainProduct> getMainProductByProductId(UUID id);
}
