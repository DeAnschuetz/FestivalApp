package com.ffb.app.mapper.impl;

import com.ffb.app.mapper.api.ResponseMapper;
import com.ffb.model.api.response.account.AccountResponse;
import com.ffb.model.api.response.cart.CartItemResponse;
import com.ffb.model.api.response.cart.CartResponseFull;
import com.ffb.model.api.response.cart.CartResponseSimple;
import com.ffb.model.api.response.credit.CreditHistoryResponse;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.api.response.credit.CreditResponseFull;
import com.ffb.model.api.response.food.court.FoodCourtResponse;
import com.ffb.model.api.response.food.court.FoodCourtResponseFull;
import com.ffb.model.api.response.food.order.FoodOrderHistoryResponse;
import com.ffb.model.api.response.food.order.FoodOrderItemResponse;
import com.ffb.model.api.response.food.order.FoodOrderResponse;
import com.ffb.model.api.response.food.order.FoodOrderResponseFull;
import com.ffb.model.api.response.notification.FoodOrderNotificationResponse;
import com.ffb.model.api.response.product.ProductResponse;
import com.ffb.model.api.response.ticket.TicketResponse;
import com.ffb.model.db.object.account.Account;
import com.ffb.model.db.object.account.Ticket;
import com.ffb.model.db.object.cart.Cart;
import com.ffb.model.db.object.cart.CartItem;
import com.ffb.model.db.object.credit.Credit;
import com.ffb.model.db.object.credit.CreditHistory;
import com.ffb.model.db.object.food_court.FoodCourt;
import com.ffb.model.db.object.foodorder.FoodOrder;
import com.ffb.model.db.object.foodorder.FoodOrderHistory;
import com.ffb.model.db.object.foodorder.FoodOrderItem;
import com.ffb.model.db.object.notification.FoodOrderNotification;
import com.ffb.model.db.object.product.Product;
import jakarta.enterprise.context.ApplicationScoped;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class ResponseMapperImpl implements ResponseMapper {

    // TODO Logging

    private final Logger LOG = LoggerFactory.getLogger(ResponseMapperImpl.class);

    @Override
    public AccountResponse getAccountResponse(Account account) {
        LOG.trace("ENTER: getAccountResponse; account={{}}", account);
        if(account == null) {
            return null;
        }
        AccountResponse response = new AccountResponse(
                account.getId(),
                account.getLoginNr(),
                account.getType()
        );
        LOG.trace("EXIT: getTicketResponse; response={{}}", response);
        return response;
    }

    @Override
    public TicketResponse getTicketResponse(Ticket ticket) {
        LOG.trace("ENTER: getTicketResponse; ticket={{}}", ticket);
        if(ticket == null) {
            return null;
        }
        TicketResponse response =  new TicketResponse(
                ticket.getId(),
                ticket.getLoginNr()
        );
        LOG.trace("EXIT: getTicketResponse; response={{}}", response);
        return response;
    }

    @Override
    public CreditResponse getCreditResponse(Credit credit) {
        if(credit == null) {
            return null;
        }
        return new CreditResponse(
                credit.getAmount()
        );
    }

    @Override
    public CartResponseSimple getCartResponseSimple(Cart cart) {
        if(cart == null) {
            return null;
        }
        List<CartItemResponse> items = cart.getCartItems().stream()//
                .map(this::getCartItemResponse)//
                .toList()//
        ;
        return new CartResponseSimple(cart.isHasPrio(), cart.getTotal(), items);
    }

    @Override
    public CartResponseFull getCartResponseFull(Cart cart) {
        if(cart == null) {
            return null;
        }
        List<CartItemResponse> items = new ArrayList<>();
        if (cart.getCartItems() != null) {
            for (CartItem i : cart.getCartItems()) {
                items.add(getCartItemResponse(i));
            }
        }


        return new CartResponseFull(getAccountResponse(cart.getAccount()), cart.isHasPrio(), cart.getTotal(), items);
    }

    @Override
    public CartItemResponse getCartItemResponse(CartItem cartItem) {
        if(cartItem == null) {
            return null;
        }
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

    @Override
    public CartItemResponse getCartItemResponse(Product product, int count) {
        if(product == null) {
            return null;
        }
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

    @Override
    public FoodCourtResponse getFoodCourtResponse(FoodCourt foodCourt) {
        if(foodCourt == null) {
            return null;
        }
        return new FoodCourtResponse(foodCourt.getId(), foodCourt.getDisplayName(), foodCourt.getWaitingTime());
    }

    @Override
    public FoodOrderResponse getFoodOrderResponse(FoodOrder foodOrder) {
        if(foodOrder == null) {
            return null;
        }
        List<FoodOrderItemResponse> foodOrderItems = foodOrder.getItems().stream().map(this::getFoodOrderItemResponse).toList();
        return new FoodOrderResponse(
                foodOrder.getId(),
                foodOrder.getStatus(),
                foodOrder.getFoodCourt().getDisplayName(),
                foodOrder.getWaitingTime(),
                foodOrderItems
        );
    }

    @Override
    public FoodOrderItemResponse getFoodOrderItemResponse(FoodOrderItem foodOrderItem) {
        if(foodOrderItem == null) {
            return null;
        }
        Product product = foodOrderItem.getProduct();
        List<FoodOrderItemResponse> subItems = product.getSubProducts().stream().map(subProduct -> getFoodOrderItemResponse(subProduct, foodOrderItem.getItemCount())).toList();
        return new FoodOrderItemResponse(
                product.getId(),
                product.getDisplayName(),
                product.getSymbolIdentifier(),
                foodOrderItem.getItemCount(),
                foodOrderItem.getExtra(),
                subItems
        );
    }

    @Override
    public FoodOrderItemResponse getFoodOrderItemResponse(Product product, int count) {
        if(product == null) {
            return null;
        }
        return new FoodOrderItemResponse(
                product.getId(),
                product.getDisplayName(),
                product.getSymbolIdentifier(),
                count,
                null,
                null
        );
    }

    @Override
    public ProductResponse getProductResponse(Product product) {
        if(product == null) {
            return null;
        }
        List<ProductResponse> subProducts = product.getSubProducts().stream().map(this::getProductResponse).toList();
        return new ProductResponse(
                product.getId(),
                product.getPrice(),
                product.getDisplayName(),
                product.getSymbolIdentifier(),
                product.getMinimalWarning(),
                product.getCount(),
                subProducts
        );
    }

    @Override
    public CreditHistoryResponse getCreditHistoryResponse(CreditHistory creditHistory) {
        if(creditHistory == null) {
            return null;
        }
        return new CreditHistoryResponse(
                creditHistory.getOldAmount(),
                creditHistory.getNewAmount(),
                creditHistory.getChangeTime()
        );
    }

    @Override
    public ProductResponse getProductResponseFull(Product product) {
        if(product == null) {
            return null;
        }
        List<ProductResponse> subProducts = product.getSubProducts().stream().map(this::getProductResponse).toList();
        return new ProductResponse(
                product.getId(),
                product.getPrice(),
                product.getDisplayName(),
                product.getSymbolIdentifier(),
                product.getMinimalWarning(),
                product.getCount(),
                subProducts
        );
    }

    @Override
    public FoodCourtResponseFull getFoodCourtResponseFull(FoodCourt foodCourt) {
        if(foodCourt == null) {
            return null;
        }
        return new FoodCourtResponseFull(
                getFoodCourtResponse(foodCourt),
                foodCourt.getProducts().stream()//
                        .map(this::getProductResponseFull)//
                        .toList()//
                ,
                foodCourt.getFoodOrders().stream()//
                        .map(this::getFoodOrderResponse)
                        .toList()
        );
    }

    @Override
    public CreditResponseFull getCreditResponseFull(Credit credit) {
        if(credit == null) {
            return null;
        }
        return new CreditResponseFull(
                credit.getId(),
                credit.getAmount(),
                credit.getCreditHistory().stream()//
                        .map(this::getCreditHistoryResponse)//
                        .toList()//
        );
    }

    @Override
    public FoodOrderResponseFull getFoodOrderResponseFull(FoodOrder foodOrder) {
        if(foodOrder == null) {
            return null;
        }
        List<FoodOrderItemResponse> foodOrderItems = foodOrder.getItems().stream().map(this::getFoodOrderItemResponse).toList();
        return new FoodOrderResponseFull(
                foodOrder.getId(),
                foodOrder.getStatus(),
                foodOrder.getFoodCourt().getDisplayName(),
                foodOrder.getWaitingTime(),
                foodOrderItems,
                foodOrder.getHistory().stream()//
                        .map(this::getFoodOrderHistoryResponse)
                        .toList(),
                foodOrder.getNotifications().stream()//
                        .map(this::getFoodOrderNotificationResponse)
                        .toList()
        );
    }

    @Override
    public FoodOrderNotificationResponse getFoodOrderNotificationResponse(FoodOrderNotification notification) {
        return new FoodOrderNotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getStatus(),
                notification.getMessage(),
                notification.getCreationTime(),
                notification.getPickupTime()
        );
    }

    @Override
    public FoodOrderHistoryResponse getFoodOrderHistoryResponse(FoodOrderHistory foodOrderHistory) {
        return new FoodOrderHistoryResponse(
                foodOrderHistory.getOldStatus(),
                foodOrderHistory.getNewStatus(),
                foodOrderHistory.getStatusChangeTime()
        );
    }
}
