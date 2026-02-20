package com.ffb.app.service.impl.cart;

import com.ffb.app.dao.api.cart.CartDao;
import com.ffb.app.service.api.cart.CartService;
import com.ffb.model.api.request.cart.CartItemRequest;
import com.ffb.model.api.response.cart.CartItemSimple;
import com.ffb.model.api.response.cart.CartSimple;
import com.ffb.model.db.objects.cart.Cart;
import com.ffb.model.db.objects.cart.CartItem;
import com.ffb.model.db.objects.product.MainProduct;
import io.quarkus.hibernate.orm.panache.Panache;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class CartServiceImpl implements CartService {

    private final CartDao cartDao;

    @Inject
    public CartServiceImpl(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    @Override
    public CartSimple getCartByLoginNr(String loginNr) {
        Cart cart = cartDao.findByLoginNrWithItems(loginNr)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for loginNr=" + loginNr));
        return toSimple(cart);
    }

    @Override
    @Transactional
    public CartSimple changePrio(String loginNr, boolean newPrio) {
        Cart cart = cartDao.findByLoginNrWithItems(loginNr)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for loginNr=" + loginNr));

        cart.setHasPrio(newPrio);
        recalcTotal(cart);

        return toSimple(cart);
    }

    @Override
    @Transactional
    public CartSimple addItemToCart(CartItemRequest request) {
        // adapt these getters to your CartItemRequest fields
        String loginNr = request.loginNr();
        UUID productId = request.productId();
        int count = request.itemCount();
        String extra = request.extra();

        if (count <= 0) throw new IllegalArgumentException("count must be > 0");

        Cart cart = cartDao.findByLoginNrWithItems(loginNr)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for loginNr=" + loginNr));

        MainProduct product = Panache.getEntityManager().find(MainProduct.class, productId);
        if (product == null) {
            throw new IllegalArgumentException("Product not found: " + productId);
        }

        // Strategy: merge by same product + same extra
        CartItem existing = cart.getCartItems().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .filter(i -> (i.getExtra() == null ? extra == null : i.getExtra().equals(extra)))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            existing.setItemCount(existing.getItemCount() + count);
            existing.setPrice(calcItemPrice(product, existing.getItemCount(), extra));
        } else {
            CartItem item = new CartItem(UUID.randomUUID(), 0.0, count, extra);
            item.setCart(cart);
            item.setProduct(product);
            item.setPrice(calcItemPrice(product, count, extra));

            if (cart.getCartItems() == null) cart.setCartItems(new ArrayList<>());
            cart.getCartItems().add(item);
        }

        recalcTotal(cart);
        return toSimple(cart);
    }

    @Override
    @Transactional
    public CartSimple removeItemFromCart(String loginNr, UUID cartItemId) {
        Cart cart = cartDao.findByLoginNrWithItems(loginNr)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for loginNr=" + loginNr));

        boolean removed = cart.getCartItems().removeIf(i -> i.getId().equals(cartItemId));
        if (!removed) {
            throw new IllegalArgumentException("CartItem not found: " + cartItemId);
        }

        // because of orphan removal behavior:
        // If you want removed items deleted automatically, add orphanRemoval=true on @OneToMany.
        // Otherwise, JPA may keep them unless you explicitly delete.
        recalcTotal(cart);

        return toSimple(cart);
    }

    // ----------------- helpers -----------------

    private void recalcTotal(Cart cart) {
        double total = 0.0;
        if (cart.getCartItems() != null) {
            for (CartItem i : cart.getCartItems()) {
                total += i.getPrice();
            }
        }
        cart.setTotal(total);
    }

    private double calcItemPrice(MainProduct product, int count, String extra) {
        // adapt to your pricing logic
        double base = product.getPrice(); // assumes MainProduct has getPrice()
        double extraCost = 0.0;

        // example: add fixed extra cost if extra is present
        if (extra != null && !extra.isBlank()) {
            extraCost = 0.0; // put your own calculation here
        }

        return (base + extraCost) * count;
    }

    private CartSimple toSimple(Cart cart) {
        List<CartItemSimple> items = new ArrayList<>();

        if (cart.getCartItems() != null) {
            for (CartItem i : cart.getCartItems()) {
                // If you have bundles/menus with subItems, you’ll need a model relation for that.
                // Right now CartItem has no subItems field, so we map subItems = null.
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
