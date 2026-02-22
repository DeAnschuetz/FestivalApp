package com.ffb.app.repository.impl.product;

import com.ffb.app.repository.api.product.MainProductRepository;
import com.ffb.model.db.objects.product.MainProduct;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class MainProductRepositoryImpl implements MainProductRepository {

    public Optional<MainProduct> getMainProductByProductId(UUID id) {
        return find(
                    "id",
                    id
                )//
                .firstResultOptional()//
        ;
    }
}
