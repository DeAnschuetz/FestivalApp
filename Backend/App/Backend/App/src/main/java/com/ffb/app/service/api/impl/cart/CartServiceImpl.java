package com.ffb.app.service.api.impl.cart;

import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.dao.api.product.ProductDao;
import com.ffb.app.service.api.api.cart.CartService;
import com.ffb.model.api.response.cart.CartItemSimple;
import com.ffb.model.api.response.cart.CartSimple;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.cart.CartItem;
import com.ffb.model.db.objects.product.Product;
import com.ffb.model.exception.DaoException;
import com.ffb.model.exception.ServiceException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CartServiceImpl implements CartService {

    private final double EXTRA_PRICE = 2;
    private final CartDao cartDao;
    private final ProductDao productDao;

    @Inject
    public CartServiceImpl(CartDao cartDao, ProductDao productDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
    }

    @Override
    public CartSimple getCartByLoginNr(String loginNr) throws ServiceException {
        Cart cart;
        try {
            cart = cartDao.findByLoginNrWithItems(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        return toSimple(cart);
    }

    @Override
    @Transactional
    public CartSimple changePrio(String loginNr, boolean newPrio) throws ServiceException {
        Cart cart = null;
        try {
            cart = cartDao.findByLoginNrWithItems(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        cart.setHasPrio(newPrio);
        calculateCartTotal(cart);

        return toSimple(cart);
    }

    @Override
    @Transactional
    public CartSimple addItemToCart(String loginNr, UUID productId, int itemCount, String extra) throws ServiceException {

        // get the cart for the user, if it does not exist, throw an exception
        Cart cart;
        try {
            cart = cartDao.findByLoginNrWithItems(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        // get the product, if it does not exist, throw an exception
        Product product = productDao.getById(productId);
        if (product == null) {
            throw new ServiceException("Product not found: " + productId);
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
            // if the item does not exist, create a new cart item for each sub product (or the main product if there are no sub products)
            List<Product> products = product.getSubProducts();

            // if there are no sub products, we still want to create a cart item for the main product
            if (products == null) {
                products = List.of(product);
            }

            // create a cart item for each product
            products.forEach(subProduct -> {
                    // create a new cart item with the given product, item count, and extra
                    CartItem item = new CartItem(UUID.randomUUID(), 0.0, itemCount, extra, cart, subProduct);
                    item.setPrice(calculateCartItemPrice(subProduct, extra));

                    // add the new cart item to the cart, if the cart does not have any items yet, initialize the list first
                    if (cart.getCartItems() == null) {
                        cart.setCartItems(new ArrayList<>());
                    }

                    // persist the new cart item and add it to the cart
                    cartDao.persistCartItem(item);
                    cart.getCartItems().add(item);
                }//
            );
        }

        // recalculate the cart total and persist the cart
        calculateCartTotal(cart);
        cartDao.persist(cart);
        return toSimple(cart);
    }

    @Override
    @Transactional
    public CartSimple removeItemFromCart(String loginNr, UUID cartItemId) throws ServiceException {

        // get the cart for the user, if it does not exist, throw an exception
        Cart cart = null;
        try {
            cart = cartDao.findByLoginNrWithItems(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        // remove the cart item with the given id from the cart, if it does not exist, throw an exception
        boolean removed = cart.getCartItems()//
                .removeIf(item -> item.getId().equals(cartItemId))//
        ;
        if (!removed) {
            throw new ServiceException("CartItem not found: " + cartItemId);
        }

        // recalculate the cart total and persist the cart
        calculateCartTotal(cart);
        cartDao.persist(cart);
        return toSimple(cart);
    }

    @Override
    public CartSimple updateCartItemById(String loginNr, UUID cartItemId, int newItemCount, String newExtra) throws ServiceException {

        // get the cart for the user, if it does not exist, throw an exception
        Cart cart = null;
        try {
            cart = cartDao.findByLoginNrWithItems(loginNr);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }

        // find the cart item with the given id, if it does not exist, throw an exception
        List<CartItem> cartItems = cart.getCartItems();
        CartItem itemToUpdate = cartItems.stream()//
                .filter(item -> item.getId().equals(cartItemId))//
                .findFirst()//
                .orElseThrow(() -> new IllegalArgumentException("CartItem not found: " + cartItemId))//
        ;

        // update the item count and extra if they have changed, also update the price if the extra has changed
        if (itemToUpdate.getItemCount() != newItemCount) {
            itemToUpdate.setItemCount(newItemCount);
        }
        if (!itemToUpdate.getExtra().equalsIgnoreCase(newExtra)) {
            itemToUpdate.setExtra(newExtra);
            itemToUpdate.setPrice(calculateCartItemPrice(itemToUpdate.getProduct(), newExtra));
        }

        // persist the updated cart item, recalculate the cart total, and persist the cart
        cartDao.persistCartItem(itemToUpdate);
        calculateCartTotal(cart);
        cartDao.persist(cart);
        return null;
    }

    private void calculateCartTotal(Cart cart) {
        double cartTotal = 0.0;
        if (cart.getCartItems() != null) {
            for (CartItem i : cart.getCartItems()) {
                cartTotal += i.getPrice() * i.getItemCount();
            }
        }
        cart.setTotal(cartTotal);
    }

    private double calculateCartItemPrice(Product product, String extra) {
        double basePrice = product.getPrice();
        double extraCost = (extra != null && !extra.isEmpty()) ? EXTRA_PRICE : 0.0;
        return basePrice + extraCost;
    }

    private CartSimple toSimple(Cart cart) {
        List<CartItemSimple> items = new ArrayList<>();

        if (cart.getCartItems() != null) {
            for (CartItem i : cart.getCartItems()) {
                items.add(new CartItemSimple(
                        i.getId(),
                        i.getProduct() != null ? i.getProduct().getDisplayName() : "Unknown",
                        i.getProduct() != null ? i.getProduct().getSymbolIdentifier() : null,
                        i.getPrice(),
                        i.getItemCount(),
                        i.getExtra(),
                        null
                ));
            }
        }

        return new CartSimple(cart.isHasPrio(), cart.getTotal(), items);
    }
}
