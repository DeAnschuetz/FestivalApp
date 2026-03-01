package com.ffb.app.dao.api.product;

import com.ffb.model.db.view.MainProduct;
import com.ffb.model.db.object.product.MainSubProductLink;
import com.ffb.model.db.object.product.Product;
import com.ffb.model.exception.DaoException;

import java.util.List;
import java.util.UUID;

public interface ProductDao {

    List<Product> listAll();

    List<Product> listByLoginNr(String loginNr);

    List<Product> listByFoodCourtId(UUID foodCourtId);

    void persistLink(MainSubProductLink link);

    boolean linkExists(UUID mainId, UUID subId);

    long deleteLinkByPair(UUID mainId, UUID subId);

    boolean deleteLinkById(UUID linkId);

    void persist(Product product);

    Product getById(UUID id);

    void delete(Product product);
}
