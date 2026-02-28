package com.ffb.model.api.response.account;

import com.ffb.model.api.response.cart.CartResponseSimple;
import com.ffb.model.api.response.credit.CreditResponseFull;
import com.ffb.model.api.response.food.court.FoodCourtResponseFull;
import com.ffb.model.api.response.food.order.FoodOrderResponse;
import com.ffb.model.api.response.food.order.FoodOrderResponseFull;

import java.util.List;

public record AccountResponseFull(AccountResponse account, CreditResponseFull credit, CartResponseSimple cart, List<FoodOrderResponseFull> orders, FoodCourtResponseFull foodCourt) {
}
