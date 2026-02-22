package com.ffb.app.repository.impl.product;

import com.ffb.app.repository.api.product.ProductRepository;
import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.db.objects.product.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public List<Product> listByLoginNr(String loginNr) {
        return list("foodCourt.account.ticket.login_nr", loginNr);
    }

    @Override
    public List<Product> listByFoodCourtId(UUID foodCourtId) {
        return list("foodCourt.id", foodCourtId);
    }

    @Override
    public void persistLink(MainSubProductLink link) throws EntityExistsException, IllegalArgumentException, TransactionRequiredException {
        getEntityManager().persist(link);
    }
}
