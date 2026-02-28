package com.ffb.app.repository.api.product;

import com.ffb.model.db.view.SubProduct;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import java.util.UUID;

public interface SubProductRepository extends  PanacheRepositoryBase<SubProduct, UUID> {

    void flush(SubProduct entity);

    void update(SubProduct entity);

    void persist(SubProduct entity);

}
