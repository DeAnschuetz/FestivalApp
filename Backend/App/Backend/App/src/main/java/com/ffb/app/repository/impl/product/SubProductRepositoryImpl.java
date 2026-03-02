package com.ffb.app.repository.impl.product;

import com.ffb.app.repository.api.product.SubProductRepository;
import com.ffb.model.db.view.SubProduct;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class SubProductRepositoryImpl implements SubProductRepository {

    @Override
    public void update(SubProduct entity) {
        throw new UnsupportedOperationException("SubProduct is a view and is read-only.");
    }

    @Override
    public void persist(SubProduct entity) {
        throw new UnsupportedOperationException("SubProduct is a view and is read-only.");
    }
}
