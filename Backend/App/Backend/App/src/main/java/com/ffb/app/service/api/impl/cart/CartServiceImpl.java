package com.ffb.app.service.api.impl.cart;

import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.dao.api.product.ProductDao;
import com.ffb.app.service.api.api.cart.CartService;
import com.ffb.model.api.response.cart.CartItemResponse;
import com.ffb.model.api.response.cart.CartResponse;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.cart.CartItem;
import com.ffb.model.db.objects.product.Product;
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

    private final Logger LOG = Logger.getLogger(CartServiceImpl.class);


    @ConfigProperty(name = "order.extra.price")
    double EXTRA_PRICE;
    private final CartDao cartDao;
    private final ProductDao productDao;

    @Inject
    public CartServiceImpl(CartDao cartDao, ProductDao productDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
    }

    @Override
    public CartResponse getCartByLoginNr(String loginNr) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            LOG.error("LoginNr is null or Empty");
            throw new ServiceException("LoginNr is null or Empty", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNrWithItems(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        return getCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse changePrio(String loginNr, boolean newPrio) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            LOG.error("LoginNr is null or Empty");
            throw new ServiceException("LoginNr is null or Empty", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNrWithItems(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        cart.setHasPrio(newPrio);
        calculateCartTotal(cart);
        cartDao.persist(cart);

        return getCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse addItemToCart(String loginNr, UUID productId, int itemCount, String extra) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            LOG.error("LoginNr is null or Empty");
            throw new ServiceException("LoginNr is null or Empty", Response.Status.BAD_REQUEST);
        }
        if (productId==null){
            LOG.error("id is null");
            throw new ServiceException("id is null", Response.Status.BAD_REQUEST);
        }
        if (itemCount<=0){
            LOG.error("itemCount is <= 0");
            throw new ServiceException("itemCount is <= 0", Response.Status.BAD_REQUEST);
        }
        if (extra != null && extra.length() > 255) {
            throw new ServiceException("Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNrWithItems(loginNr);
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
            CartItem item = new CartItem(UUID.randomUUID(),  0, itemCount, extra, cart, product);
            item.setPrice(calculateCartItemPrice(product, extra));

            // add the new cart item to the cart, if the cart does not have any items yet, initialize the list first
            if (cart.getCartItems() == null) {
                cart.setCartItems(new ArrayList<>());
            }

            // persist the new cart item and add it to the cart
            cartDao.persistCartItem(item);
            cart.getCartItems().add(item);
        }

        // recalculate the cart total and persist the cart
        calculateCartTotal(cart);
        cartDao.persist(cart);
        return getCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse removeItemFromCart(String loginNr, UUID cartItemId) throws ServiceException {
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
            cart = cartDao.findByLoginNrWithItems(loginNr);
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
        cartDao.persist(cart);
        return getCartResponse(cart);
    }

    @Transactional
    @Override
    public CartResponse updateCartItemById(String loginNr, UUID cartItemId, int newItemCount, String newExtra) throws ServiceException {
        if (loginNr == null || loginNr.isEmpty()) {
            LOG.error("LoginNr is null or Empty");
            throw new ServiceException("LoginNr is null or Empty", Response.Status.BAD_REQUEST);
        }
        if (cartItemId == null) {
            LOG.error("cartItemId is null");
            throw new ServiceException("cartItemId is null", Response.Status.BAD_REQUEST);
        }
        if (newItemCount == 0) {
            LOG.error("newItemCount is 0");
            throw new ServiceException("newItemCount is 0", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNrWithItems(loginNr);
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

        cartDao.persistCartItem(itemToUpdate);
        calculateCartTotal(cart);
        cartDao.persist(cart);
        return getCartResponse(cart);
    }

    @Override
    public List<CartResponse> listAll() {
        return cartDao.listAll().stream()//
                .map(this::getCartResponse)//
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

    	/*
		Private Helper Functions
	*/


    private CartResponse getCartResponse(Cart cart) {
        List<CartItemResponse> items = new ArrayList<>();

        if (cart.getCartItems() != null) {
            for (CartItem i : cart.getCartItems()) {
                items.add(getCartItemResponse(i));
            }
        }

        return new CartResponse(cart.isHasPrio(), cart.getTotal(), items);
    }

    private CartItemResponse getCartItemResponse(CartItem cartItem) {
        Product product = cartItem.getProduct();
        List<CartItemResponse> subItems = product.getSubProducts().stream().map(subProduct -> getCartItemResponse(subProduct, cartItem.getItemCount())).toList();
        return new CartItemResponse(
                cartItem.getId(),
                product.getDisplayName(),
                product.getSymbolIdentifier(),
                cartItem.getPrice(),
                cartItem.getItemCount(),
                cartItem.getExtra(),
                subItems
        );
    }

    private CartItemResponse getCartItemResponse(Product product, int count) {
        return new CartItemResponse(
                product.getId(),
                product.getDisplayName(),
                product.getSymbolIdentifier(),
                0,
                count,
                null,
                null
        );
    }
}
