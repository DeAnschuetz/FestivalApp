package com.ffb.app.service.impl.product;

import com.ffb.app.repository.api.foodcourt.FoodcourtRepository;
import com.ffb.app.repository.api.product.ProductRepository;
import com.ffb.app.repository.impl.product.ProductRepositoryImpl;
import com.ffb.app.service.api.product.ProductService;
import com.ffb.model.api.request.product.ProductRequest;
import com.ffb.model.db.objects.foodcourt.Foodcourt;
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

    private final ProductRepository productRepo;
    private final FoodcourtRepository foodcourtRepo;

    @Inject
    public ProductServiceImpl(ProductRepositoryImpl productRepo, FoodcourtRepository foodcourtRepo) {
        this.productRepo = productRepo;
        this.foodcourtRepo = foodcourtRepo;
    }

    @Transactional
    public Product createProduct(ProductRequest req) throws NotFoundException {
        UUID id = UUID.randomUUID();
        Foodcourt foodcourt = foodcourtRepo.findByIdOptional(req.foodcourtId())//
                .orElseThrow(() -> new EntityNotFoundException("Foodcourt not found: " + id))//
        ;
        Product p = new Product(id, req.price(), req.displayName(), req.symbolIdentifier(), req.minimalWarning());
        p.setFoodcourt(foodcourt);
        productRepo.persist(p);
        return p;
    }

    @Transactional
    public Product createProductWithId(UUID id, ProductRequest req) throws KeyAlreadyExistsException, EntityNotFoundException {
        if (productRepo.findById(id) != null) {
            throw new KeyAlreadyExistsException("Product already exists: " + id);
        }

        Foodcourt foodcourt = foodcourtRepo.findByIdOptional(req.foodcourtId())//
                .orElseThrow(() -> new EntityNotFoundException("Foodcourt not found: " + id))//
        ;
        Product p = new Product(id, req.price(), req.displayName(), req.symbolIdentifier(), req.minimalWarning());
        p.setFoodcourt(foodcourt);
        productRepo.persist(p);
        return p;
    }

    public Product getProductById(UUID id) throws NotFoundException {
        Product p = productRepo.findById(id);
        if (p == null) throw new NotFoundException("Product not found: " + id);
        return p;
    }

    public List<Product> listProducts() {
        return productRepo.listAll();
    }

    public List<Product> listProductsByFoodcourtId(UUID foodcourtId) {
        return productRepo.listByFoodcourtId(foodcourtId);
    }

    @Transactional
    public void deleteProductById(UUID id) throws NotFoundException {
        Product p = productRepo.findById(id);
        if (p == null) throw new NotFoundException("Product not found: " + id);
        productRepo.delete(p);
    }

    // -------- Assignments (Links) --------

    @Transactional
    public UUID createAssignment(UUID mainProductId, UUID subProductId) throws IllegalArgumentException, NoSuchElementException, IllegalStateException, PersistenceException {

        Product main = productRepo.findById(mainProductId);
        if (main == null) throw new NotFoundException("Main product not found: " + mainProductId);

        Product sub = productRepo.findById(subProductId);
        if (sub == null) throw new NotFoundException("Sub product not found: " + subProductId);

        if (productRepo.linkExists(mainProductId, subProductId)) {
            throw new KeyAlreadyExistsException("Assignment already exists");
        }

        MainSubProductLink link = new MainSubProductLink(main, sub);

        productRepo.persistLink(link);
        return link.id;
    }

    public List<MainSubProductLink> listAssignmentsForMainProduct(UUID mainProductId) throws IllegalArgumentException, IllegalStateException, PersistenceException {
        return productRepo.listLinksByMain(mainProductId);
    }

    @Transactional
    public void deleteAssignmentById(UUID linkId) throws NotFoundException, IllegalArgumentException, TransactionRequiredException {
        boolean ok = productRepo.deleteLinkById(linkId);
        if (!ok) throw new NotFoundException("Assignment not found: " + linkId);
    }

    @Transactional
    public void deleteAssignmentByPair(UUID mainProductId, UUID subProductId) throws NotFoundException, IllegalArgumentException, IllegalStateException, PersistenceException {
        long deleted = productRepo.deleteLinkByPair(mainProductId, subProductId);
        if (deleted == 0) throw new NotFoundException("Assignment not found for given pair");
    }
}
