package com.ffb.app.service.api.api.product;

import com.ffb.model.db.objects.product.MainSubProductLink;
import com.ffb.model.db.objects.product.Product;
import com.ffb.model.exception.ServiceException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TransactionRequiredException;
import jakarta.ws.rs.NotFoundException;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public interface ProductService {

    List<Product> listProducts();

    List<Product> listProductsByLoginNr(String loginNr);

    Product createProductByLoginNr(String loginNr, double price, String displayName, String symbolIdentifier, int minimalWarning) throws NotFoundException, ServiceException;

    List<Product> listProductsByFoodCourtId(UUID foodCourtId);

    Product getProductById(UUID id) throws NotFoundException;

    Product createProductWithId(UUID id, UUID foodCourtId, double price, String displayName, String symbolIdentifier, int minimalWarning) throws KeyAlreadyExistsException, EntityNotFoundException, ServiceException;

    void deleteProductById(UUID id) throws NotFoundException, ServiceException;

    boolean createAssignment(UUID mainProductId, UUID subProductId) throws IllegalArgumentException, NoSuchElementException, IllegalStateException, PersistenceException, ServiceException;

    List<MainSubProductLink> listAssignmentsForMainProduct(UUID mainProductId) throws IllegalArgumentException, IllegalStateException, PersistenceException;

    void deleteAssignmentById(UUID linkId) throws NotFoundException, IllegalArgumentException, TransactionRequiredException;

    void deleteAssignmentByPair(UUID mainProductId, UUID subProductId) throws NotFoundException, IllegalArgumentException, IllegalStateException, PersistenceException, ServiceException;
}
