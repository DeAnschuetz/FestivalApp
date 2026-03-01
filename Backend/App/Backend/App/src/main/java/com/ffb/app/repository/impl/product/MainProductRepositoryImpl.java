package com.ffb.app.repository.impl.product;

import com.ffb.app.repository.api.product.MainProductRepository;
import com.ffb.model.db.view.MainProduct;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MainProductRepositoryImpl implements MainProductRepository {

    // TODO Logging

    @Override
    public void update(MainProduct entity) {
        throw new UnsupportedOperationException("MainProduct is a view and is read-only.");
    }

    @Override
    public void persist(MainProduct entity) {
        throw new UnsupportedOperationException("MainProduct is a view and is read-only.");
    }

    public Optional<MainProduct> getMainProductByProductId(UUID id) {
        return find(
                    "id",
                    id
                )//
                .firstResultOptional()//
        ;
    }
}
