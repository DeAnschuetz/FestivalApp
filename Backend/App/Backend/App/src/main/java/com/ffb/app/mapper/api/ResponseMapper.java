package com.ffb.app.mapper.api;

import com.ffb.model.api.response.account.AccountResponse;
import com.ffb.model.api.response.cart.CartItemResponse;
import com.ffb.model.api.response.cart.CartResponseFull;
import com.ffb.model.api.response.cart.CartResponseSimple;
import com.ffb.model.api.response.credit.CreditHistoryResponse;
import com.ffb.model.api.response.credit.CreditResponse;
import com.ffb.model.api.response.credit.CreditResponseFull;
import com.ffb.model.api.response.food.court.FoodCourtResponse;
import com.ffb.model.api.response.food.court.FoodCourtResponseFull;
import com.ffb.model.api.response.food.order.*;
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

public interface ResponseMapper {

    AccountResponse getAccountResponse(Account account);

    TicketResponse getTicketResponse(Ticket ticket);

    CreditResponse getCreditResponse(Credit credit);

    CartResponseSimple getCartResponseSimple(Cart cart);

    CartResponseFull getCartResponseFull(Cart cart);

    CartItemResponse getCartItemResponse(CartItem cartItem);

    CartItemResponse getCartItemResponse(Product product, int count);

    FoodCourtResponse getFoodCourtResponse(FoodCourt foodCourt);

    FoodOrderResponse getFoodOrderResponse(FoodOrder foodOrder);

    FoodOrderItemResponse getFoodOrderItemResponse(FoodOrderItem foodOrderItem);

    FoodOrderItemResponse getFoodOrderItemResponse(Product product, int count);

    ProductResponse getProductResponse(Product product);

    CreditHistoryResponse getCreditHistoryResponse(CreditHistory creditHistory);

    ProductResponse getProductResponseFull(Product product);

    FoodCourtResponseFull getFoodCourtResponseFull(FoodCourt foodCourt);

    CreditResponseFull getCreditResponseFull(Credit credit);

    FoodOrderResponseFull getFoodOrderResponseFullWithNotification(FoodOrder foodOrder);

    FoodOrderResponseHistory FoodOrderResponseHistory(FoodOrder foodOrder);

    FoodOrderNotificationResponse getFoodOrderNotificationResponse(FoodOrderNotification notification);

    FoodOrderHistoryResponse getFoodOrderHistoryResponse(FoodOrderHistory foodOrderHistory);
}
