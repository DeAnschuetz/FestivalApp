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

import org.jspecify.annotations.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CartServiceImpl implements CartService {

    // TODO Logging fertig
    private final Logger LOG = LoggerFactory.getLogger(CartServiceImpl.class);

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
    public CartResponseSimple getCartByLoginNr(@NonNull String loginNr) throws ServiceException {
        LOG.trace("ENTER: getCartByLoginNr; loginNr={{}}", loginNr);
        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("could not find cart for loginNr={{}}; Exception:", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }
        LOG.trace("EXIT: getCartByLoginNr; loginNr={{}}", loginNr);
        return mapper.getCartResponseSimple(cart);
    }

    @Transactional
    @Override
    public CartResponseSimple changePrio(@NonNull String loginNr, boolean newPrio) throws ServiceException {
        LOG.trace("ENTER: changePrio; loginNr={{}}, newPrio={}", loginNr, newPrio);
        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("Could not find cart for loginNr={{}}", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        cart.setHasPrio(newPrio);
        calculateCartTotal(cart);

        LOG.trace("EXIT: changePrio; loginNr={{}}, total={}", loginNr, cart.getTotal());
        return mapper.getCartResponseSimple(cart);
    }

    @Transactional
    @Override
    public CartResponseSimple addItemToCart(@NonNull String loginNr,@NonNull CartItemCreationRequest request) throws ServiceException {
        LOG.trace("ENTER: addItemToCart; loginNr={{}}, request=[{}]", loginNr, request);
        UUID productId = request.productId();
        int itemCount = request.itemCount();
        if (itemCount<=0){
            LOG.error("itemCount is <= 0");
            throw new ServiceException("Item Count is <= 0", Response.Status.BAD_REQUEST);
        }
        String extra = request.extra();
        if (extra != null && extra.length() > 255) {
            LOG.error("extra too long; length={}", extra.length());
            throw new ServiceException("Extra must be less than 255 characters.", Response.Status.BAD_REQUEST);
        }

        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("Could not find cart for loginNr={{}}", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        Product product = productDao.getById(productId);
        if (product == null) {
            LOG.error("Product not found; productId={{}}", productId);
            throw new ServiceException("Product not found: " + productId, Response.Status.BAD_REQUEST);
        }

        CartItem existing = cart.getCartItems().stream()//
                .filter(item -> item.getProduct().getId().equals(productId))//
                .filter(item -> (item.getExtra() == null ? extra == null : item.getExtra().equalsIgnoreCase(extra)))//
                .findFirst()//
                .orElse(null)//
        ;

        if (existing != null) {
            LOG.debug("Cart item exists; increasing count. loginNr={{}}, productId={{}}, oldCount={}, add={}", loginNr, productId, existing.getItemCount(), itemCount);
            existing.setItemCount(existing.getItemCount() + itemCount);
        } else {
            LOG.debug("Adding new cart item; loginNr={{}}, productId={{}}, itemCount={}, extra={}", loginNr, productId, itemCount, extra);
            CartItem item = new CartItem(0, itemCount, extra, cart, product);
            item.setPrice(calculateCartItemPrice(product, extra));

            if (cart.getCartItems() == null) {
                cart.setCartItems(new ArrayList<>());
            }
            cart.getCartItems().add(item);
        }
        calculateCartTotal(cart);
        LOG.trace("EXIT: addItemToCart; loginNr={{}}, total={}, items={}", loginNr, cart.getTotal(), cart.getCartItems() == null ? 0 : cart.getCartItems().size());
        return mapper.getCartResponseSimple(cart);
    }

    @Transactional
    @Override
    public CartResponseSimple removeItemFromCart(@NonNull String loginNr, @NonNull UUID cartItemId) throws ServiceException {
        LOG.trace("ENTER: removeItemFromCart; loginNr={}, cartItemId={}", loginNr, cartItemId);
        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("Could not find cart for loginNr={{}}", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        boolean removed = cart.getCartItems()//
                .removeIf(item -> item.getId().equals(cartItemId))//
        ;
        if (!removed) {
            LOG.warn("CartItem not found; loginNr={{}}, cartItemId={{}}", loginNr, cartItemId);
            throw new ServiceException("CartItem not found: " + cartItemId, Response.Status.NOT_FOUND);
        }

        calculateCartTotal(cart);
        LOG.trace("EXIT: removeItemFromCart; loginNr={{}}, total={}", loginNr, cart.getTotal());
        return mapper.getCartResponseSimple(cart);
    }

    @Transactional
    @Override
    public CartResponseSimple updateCartItemById(@NonNull String loginNr, @NonNull CartItemUpdateRequest request) throws ServiceException {
        LOG.trace("ENTER: updateCartItemById; loginNr={}, request={}", loginNr, request);
        UUID cartItemId = request.cartItemId();
        int newItemCount = request.itemCount();
        String newExtra = request.extra();

        Cart cart;
        try {
            cart = cartDao.findByLoginNr(loginNr);
        } catch (DaoException e) {
            LOG.error("Could not find cart for loginNr={{}}", loginNr, e);
            throw new ServiceException(e, Response.Status.NOT_FOUND);
        }

        List<CartItem> cartItems = cart.getCartItems();
        CartItem itemToUpdate = cartItems.stream()//
                .filter(item -> item.getId().equals(cartItemId))//
                .findFirst()//
                .orElseThrow(() -> {
                    LOG.error("could not find cart item cartitemId={{}}", cartItemId);
                    return new SecurityException("CartItem not found: " + cartItemId);
                })//
        ;

        if (itemToUpdate.getItemCount() != newItemCount) {
            LOG.debug("updating count");
            itemToUpdate.setItemCount(newItemCount);
        }

        if (!itemToUpdate.getExtra().equalsIgnoreCase(newExtra)) {
            LOG.debug("updating extra");
            itemToUpdate.setExtra(newExtra);
            itemToUpdate.setPrice(calculateCartItemPrice(itemToUpdate.getProduct(), newExtra));
        }

        calculateCartTotal(cart);
        LOG.trace("EXIT: updateCartItemById; loginNr={}, cartItemId={}, total={}", loginNr, cartItemId, cart.getTotal());

        return mapper.getCartResponseSimple(cart);
    }

    @Override
    public List<CartResponseFull> listAll() {
        LOG.trace("ENTER: listAll");
        List<CartResponseFull> carts =  cartDao.listAll().stream()//
                .map(mapper::getCartResponseFull)//
                .toList()//
        ;
        LOG.trace("EXIT: listAll; count={}", carts.size());
        return carts;
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
