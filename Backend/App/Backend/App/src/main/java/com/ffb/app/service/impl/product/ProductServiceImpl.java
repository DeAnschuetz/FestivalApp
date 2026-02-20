package com.ffb.app.service.impl.product;

import com.ffb.app.dao.api.food.court.FoodCourtDao;
import com.ffb.app.dao.api.product.ProductDao;
import com.ffb.app.service.api.product.ProductService;
import com.ffb.model.db.objects.food_court.FoodCourt;
import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.db.objects.product.Product;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@ApplicationScoped
public class ProductServiceImpl implements ProductService {
    private final ProductDao productDao;

    private final FoodCourtDao foodCourtDao;

    @Inject
    public ProductServiceImpl(ProductDao productDao, FoodCourtDao foodCourtDao) {
        this.productDao = productDao;
        this.foodCourtDao = foodCourtDao;
    }

    @Override
    public List<Product> listProducts() {
        return productDao.listAll();
    }

    @Override
    public List<Product> listProductsByLoginNr(String loginNr) {
        return productDao.listByLoginNr(loginNr);
    }

    @Override
    @Transactional
    public Product createProductByLoginNr(String loginNr, double price, String displayName, String symbolIdentifier, int minimalWarning) throws NotFoundException {
        UUID id = UUID.randomUUID();
        FoodCourt foodcourt = foodCourtDao.getByLoginNr(loginNr)//
                .orElseThrow(() -> new EntityNotFoundException("Food Court not found for loginNr: " + loginNr))//
        ;
        Product p = new Product(id, price, displayName, symbolIdentifier, minimalWarning);
        p.setFoodCourt(foodcourt);
        productDao.persist(p);
        return p;
    }

    @Override
    public List<Product> listProductsByFoodCourtId(UUID foodCourtId) {
        return productDao.listByFoodCourtId(foodCourtId);
    }

    @Override
    public Product getProductById(UUID id) throws NotFoundException {
        return productDao.getById(id);
    }

    @Override
    @Transactional
    public Product createProductWithId(UUID id, UUID foodCourtId, double price, String displayName, String symbolIdentifier, int minimalWarning) throws KeyAlreadyExistsException, EntityNotFoundException {
        if (productDao.getById(id) != null) {
            throw new KeyAlreadyExistsException("Product already exists: " + id);
        }

        FoodCourt foodcourt = foodCourtDao.getById(foodCourtId)//
                .orElseThrow(() -> new EntityNotFoundException("Food Court not found: " + foodCourtId))//
        ;
        Product p = new Product(id, price, displayName, symbolIdentifier, minimalWarning);
        p.setFoodCourt(foodcourt);
        productDao.persist(p);
        return p;
    }

    @Override
    @Transactional
    public void deleteProductById(UUID id) throws NotFoundException {
        Product p = productDao.getById(id);
        if (p == null) throw new NotFoundException("Product not found: " + id);
        productDao.delete(p);
    }

    @Override
    @Transactional
    public UUID createAssignment(UUID mainProductId, UUID subProductId) throws IllegalArgumentException, NoSuchElementException, IllegalStateException, PersistenceException {

        Product main = productDao.getById(mainProductId);
        if (main == null) throw new NotFoundException("Main product not found: " + mainProductId);

        Product sub = productDao.getById(subProductId);
        if (sub == null) throw new NotFoundException("Sub product not found: " + subProductId);

        if (productDao.linkExists(mainProductId, subProductId)) {
            throw new KeyAlreadyExistsException("Assignment already exists");
        }

        MainSubProductLink link = new MainSubProductLink(main, sub);

        productDao.persistLink(link);
        return link.id;
    }

    @Override
    public List<MainSubProductLink> listAssignmentsForMainProduct(UUID mainProductId) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return productDao.listLinksByMain(mainProductId);
    }

    @Override
    @Transactional
    public void deleteAssignmentById(UUID linkId) throws NotFoundException, IllegalArgumentException, TransactionRequiredException {
        boolean ok = productDao.deleteLinkById(linkId);
        if (!ok) throw new NotFoundException("Assignment not found: " + linkId);
    }

    @Override
    @Transactional
    public void deleteAssignmentByPair(UUID mainProductId, UUID subProductId) throws NotFoundException, IllegalArgumentException, IllegalStateException, PersistenceException {
        long deleted = productDao.deleteLinkByPair(mainProductId, subProductId);
        if (deleted == 0) throw new NotFoundException("Assignment not found for given pair");
    }
}
