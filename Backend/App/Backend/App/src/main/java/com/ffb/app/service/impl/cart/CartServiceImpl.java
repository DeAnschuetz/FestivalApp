package com.ffb.app.service.impl.cart;

import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.dao.api.product.ProductDao;
import com.ffb.app.mapper.api.ResponseMapper;
import com.ffb.app.service.api.cart.CartService;
import com.ffb.model.api.request.cart.CartItemCreationRequest;
import com.ffb.model.api.request.cart.CartItemUpdateRequest;
import com.ffb.model.api.response.cart.CartResponseFull;
import com.ffb.model.api.response.cart.CartResponseSimple;
import com.ffb.model.db.object.cart.Cart;
import com.ffb.model.db.object.cart.CartItem;
import com.ffb.model.db.object.product.Product;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CartServiceImpl implements CartService {

    // TODO Logging
    private final Logger LOG = Logger.getLogger(CartServiceImpl.class);

    @ConfigProperty(name = "order.extra.price")
    double EXTRA_PRICE;
    private final CartDao cartDao;
    private final ProductDao productDao;
    private final ResponseMapper mapper;

    @Inject
    public CartServiceImpl(CartDao cartDao, ProductDao productDao, ResponseMapper mapper) {
        this.cartDao = cartDao;
        this.productDao = productDao;
        this.mapper = mapper;
    }

    @Override
    public CartResponseSimple getCartByLoginNr(String loginNr) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            LOG.error("LoginNr is null or Empty");
            throw new ServiceException("LoginNr is null or Empty", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        return mapper.getCartResponseSimple(cart);
    }

    @Transactional
    @Override
    public CartResponseSimple changePrio(String loginNr, boolean newPrio) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            LOG.error("LoginNr is null or Empty");
            throw new ServiceException("LoginNr is null or Empty", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        cart.setHasPrio(newPrio);
        calculateCartTotal(cart);

        return mapper.getCartResponseSimple(cart);
    }

    @Transactional
    @Override
    public CartResponseSimple addItemToCart(String loginNr, CartItemCreationRequest request) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            LOG.error("LoginNr is null or Empty");
            throw new ServiceException("LoginNr is null or Empty", Response.Status.BAD_REQUEST);
        }
        UUID productId = request.productId();
        if (productId==null){
            LOG.error("id is null");
            throw new ServiceException("id is null", Response.Status.BAD_REQUEST);
        }
        int itemCount = request.itemCount();
        if (itemCount<=0){
            LOG.error("itemCount is <= 0");
            throw new ServiceException("itemCount is <= 0", Response.Status.BAD_REQUEST);
        }
        String extra = request.extra();
        if (extra != null && extra.length() > 255) {
            throw new ServiceException("Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        // get the product, if it does not exist, throw an exception
        Product product = productDao.getById(productId);
        if (product == null) {
            throw new ServiceException("Product not found: " + productId, Response.Status.BAD_REQUEST);
        }

        // check if the cart already contains an item with the same product and extra
        CartItem existing = cart.getCartItems().stream()//
                .filter(item -> item.getProduct().getId().equals(productId))//
                .filter(item -> (item.getExtra() == null ? extra == null : item.getExtra().equalsIgnoreCase(extra)))//
                .findFirst()//
                .orElse(null)//
        ;

        if (existing != null) {
            // if the item already exists, increase the item count and update the price if the extra has changed
            existing.setItemCount(existing.getItemCount() + itemCount);
        } else {
            // create a new cart item with the given product, item count, and extra
            CartItem item = new CartItem(0, itemCount, extra, cart, product);
            item.setPrice(calculateCartItemPrice(product, extra));

            // add the new cart item to the cart, if the cart does not have any items yet, initialize the list first
            if (cart.getCartItems() == null) {
                cart.setCartItems(new ArrayList<>());
            }

            // persist the new cart item and add it to the cart
            cart.getCartItems().add(item);
        }

        // recalculate the cart total and persist the cart
        calculateCartTotal(cart);
        return mapper.getCartResponseSimple(cart);
    }

    @Transactional
    @Override
    public CartResponseSimple removeItemFromCart(String loginNr, UUID cartItemId) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            LOG.error("LoginNr is null or Empty");
            throw new ServiceException("LoginNr is null or Empty", Response.Status.BAD_REQUEST);
        }
        if (cartItemId == null) {
            LOG.error("cartItemId is null");
            throw new ServiceException("cartItemId is null", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        boolean removed = cart.getCartItems()//
                .removeIf(item -> item.getId().equals(cartItemId))//
        ;
        if (!removed) {
            throw new ServiceException("CartItem not found: " + cartItemId, Response.Status.NOT_FOUND);
        }

        calculateCartTotal(cart);
        return mapper.getCartResponseSimple(cart);
    }

    @Transactional
    @Override
    public CartResponseSimple updateCartItemById(String loginNr, CartItemUpdateRequest request) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            LOG.error("LoginNr is null or Empty");
            throw new ServiceException("LoginNr is null or Empty", Response.Status.BAD_REQUEST);
        }
        UUID cartItemId = request.cartItemId();
        if(cartItemId == null) {
            throw new ServiceException("Cart item id must be provided.", Response.Status.BAD_REQUEST);
        }
        int newItemCount = request.itemCount();
        if (newItemCount <= 0) {
            throw  new ServiceException("Item count must be greater than 0.", Response.Status.BAD_REQUEST);
        }
        String newExtra = request.extra();
        if (newExtra != null && newExtra.length() > 255) {
            throw new ServiceException("Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        List<CartItem> cartItems = cart.getCartItems();
        CartItem itemToUpdate = cartItems.stream()//
                .filter(item -> item.getId().equals(cartItemId))//
                .findFirst()//
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found: " + cartItemId))//
        ;

        if (itemToUpdate.getItemCount() != newItemCount) {
            itemToUpdate.setItemCount(newItemCount);
        }

        if (!itemToUpdate.getExtra().equalsIgnoreCase(newExtra)) {
            itemToUpdate.setExtra(newExtra);
            itemToUpdate.setPrice(calculateCartItemPrice(itemToUpdate.getProduct(), newExtra));
        }

        calculateCartTotal(cart);
        return mapper.getCartResponseSimple(cart);
    }

    @Override
    public List<CartResponseFull> listAll() {
        return cartDao.listAll().stream()//
                .map(mapper::getCartResponseFull)//
                .toList()//
        ;
    }

    /*
        Private Helper Functions
    */

    private void calculateCartTotal(Cart cart) {
        double cartTotal =  0;
        if (cart.getCartItems() != null) {
            for (CartItem i : cart.getCartItems()) {
                int itemCount = i.getItemCount();
                cartTotal = cartTotal + i.getPrice() * itemCount;
            }
        }
        cart.setTotal(cartTotal);
    }

    private double calculateCartItemPrice(Product product, String extra) {
        double basePrice = product.getPrice();
        double extraCost = (extra != null && !extra.isEmpty()) ?  EXTRA_PRICE :  0;
        return basePrice + extraCost ;
    }
}
