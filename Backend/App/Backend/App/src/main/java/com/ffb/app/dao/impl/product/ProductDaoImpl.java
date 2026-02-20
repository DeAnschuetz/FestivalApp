package com.ffb.app.dao.impl.product;

import com.ffb.app.dao.api.product.ProductDao;
import com.ffb.app.repository.api.product.MainSubProductLinkRepository;
import com.ffb.app.repository.api.product.ProductRepository;
import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.db.objects.product.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class ProductDaoImpl implements ProductDao {
    private final ProductRepository productRepo;

    private final MainSubProductLinkRepository mainSubProductLinkRepo;

    @Inject
    public ProductDaoImpl(ProductRepository productRepo, MainSubProductLinkRepository mainSubProductLinkRepo) {
        this.productRepo = productRepo;
        this.mainSubProductLinkRepo = mainSubProductLinkRepo;
    }

    @Override
    public List<Product> listAll() {
        return productRepo.listAll();
    }

    @Override
    public List<Product> listByLoginNr(String loginNr) {
        return productRepo.listByLoginNr(loginNr);
    }

    @Override
    public List<Product> listByFoodCourtId(UUID foodCourtId) {
        return productRepo.listByFoodCourtId(foodCourtId);
    }

    @Override
    public Product getById(UUID id) {
        return productRepo.findById(id);
    }

    @Override
    public void persist(Product product) {
        productRepo.persist(product);
    }

    @Override
    public void delete(Product product) {
        productRepo.delete(product);
    }

    @Override
    public void persistLink(MainSubProductLink link)  {
        productRepo.persistLink(link);
    }

    @Override
    public boolean linkExists(UUID mainId, UUID subId) {
        return mainSubProductLinkRepo.linkExists(mainId, subId);
    }

    @Override
    public List<MainSubProductLink> listLinks() {
        return mainSubProductLinkRepo.listLinks();
    }

    @Override
    public List<MainSubProductLink> listLinksByMain(UUID mainId) {
        return mainSubProductLinkRepo.listLinksByMain(mainId);
    }

    @Override
    public long deleteLinkByPair(UUID mainId, UUID subId) {
        return mainSubProductLinkRepo.deleteLinkByPair(mainId, subId);
    }

    @Override
    public boolean deleteLinkById(UUID linkId) {
        return mainSubProductLinkRepo.deleteLinkById(linkId);
    }
}
