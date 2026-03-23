
import {
  getFoodOrderListAll,
  getFoodOrderListAllByStatusStatus,
  getFoodOrderListAllByStatusStatusHistory,
  getFoodOrderListAllHistory,
  postFoodOrderOrder,
  putFoodOrderShare,
  putFoodOrderUpdateOrderIdStatus,
} from "../generated/ffbAPI";
import type {
  FoodOrderStatus,
  ShareOrderRequest,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { isOfflineLikeError, toApiError } from "./core/errors";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";
import { clearStoredCart, apiGetCart } from "./cartApi";
import {
  normalizeOrder,
  normalizeOrderHistory,
  normalizeOrderHistories,
  normalizeOrders,
} from "./normalizers";
import { upsertNotificationsFromOrderUpdate } from "./notificationApi";
import type { Order, OrderHistory, OrderHistoryEntry } from "./types";

function getStoredOrders(): Order[] {
  return readJson<Order[]>(STORAGE_KEYS.orders, []);
}

function setStoredOrders(orders: Order[]): Order[] {
  return writeJson(STORAGE_KEYS.orders, orders);
}

function getStoredOrderHistory(): OrderHistory[] {
  return readJson<OrderHistory[]>(STORAGE_KEYS.orderHistory, []);
}

function setStoredOrderHistory(orders: OrderHistory[]): OrderHistory[] {
  return writeJson(STORAGE_KEYS.orderHistory, orders);
}

function toHistory(order: Order, oldStatus?: FoodOrderStatus): OrderHistory {
  const history: OrderHistoryEntry[] = [
    {
      oldStatus,
      newStatus: order.status,
      statusChangeTime: new Date().toISOString(),
    },
  ];

  return {
    ...order,
    history,
  };
}

function upsertOrder(order: OrderHistory): void {
  setStoredOrders([
    ...getStoredOrders().filter((item) => item.id !== order.id),
    {
      id: order.id,
      status: order.status,
      foodCourtName: order.foodCourtName,
      waitingTime: order.waitingTime,
      orderItems: order.orderItems,
    },
  ]);

  const currentHistory = getStoredOrderHistory().filter((item) => item.id !== order.id);
  currentHistory.push(order);
  setStoredOrderHistory(currentHistory);
}

async function createFoodOrderOffline(): Promise<Order[]> {
  const currentCart = await apiGetCart();

  const order: Order = {
    id: crypto.randomUUID(),
    status: "ORDERED",
    foodCourtName: currentCart.cartItems[0]?.displayName ?? "Offline Food Court",
    waitingTime: 0,
    orderItems: currentCart.cartItems.map((item) => ({
      productId: item.id,
      displayName: item.displayName,
      iconIdentifier: item.symbolIdentifier,
      count: item.count,
      extra: item.extra,
      subItems: item.subItems.map((subItem) => ({
        productId: subItem.id,
        displayName: subItem.displayName,
        iconIdentifier: subItem.symbolIdentifier,
        count: subItem.count,
        extra: subItem.extra,
        subItems: [],
      })),
    })),
  };

  const orders = [...getStoredOrders(), order];
  setStoredOrders(orders);
  setStoredOrderHistory([...getStoredOrderHistory(), toHistory(order)]);
  clearStoredCart();
  return orders;
}

export async function getVisibleOrders(): Promise<Order[]> {
  return readThroughCache<Order[]>({
    apiCall: () => getFoodOrderListAll(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeOrders(data as Parameters<typeof normalizeOrders>[0]),
    readCache: () => {
      const orders = getStoredOrders();
      return orders.length > 0 ? orders : null;
    },
    writeCache: setStoredOrders,
    errorMessage: "Orders could not be loaded.",
  });
}

export async function getVisibleOrdersByStatus(
  status: FoodOrderStatus,
): Promise<Order[]> {
  return readThroughCache<Order[]>({
    apiCall: () => getFoodOrderListAllByStatusStatus(status, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeOrders(data as Parameters<typeof normalizeOrders>[0]),
    readCache: () => {
      const filtered = getStoredOrders().filter((order) => order.status === status);
      return filtered.length > 0 ? filtered : null;
    },
    writeCache: setStoredOrders,
    errorMessage: "Orders could not be loaded.",
  });
}

export async function getVisibleOrdersWithHistory(): Promise<OrderHistory[]> {
  return readThroughCache<OrderHistory[]>({
    apiCall: () => getFoodOrderListAllHistory(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) =>
      normalizeOrderHistories(data as Parameters<typeof normalizeOrderHistories>[0]),
    readCache: () => {
      const orders = getStoredOrderHistory();
      return orders.length > 0 ? orders : null;
    },
    writeCache: setStoredOrderHistory,
    errorMessage: "Order history could not be loaded.",
  });
}

export async function getVisibleOrdersByStatusWithHistory(
  status: FoodOrderStatus,
): Promise<OrderHistory[]> {
  return readThroughCache<OrderHistory[]>({
    apiCall: () => getFoodOrderListAllByStatusStatusHistory(status, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) =>
      normalizeOrderHistories(data as Parameters<typeof normalizeOrderHistories>[0]),
    readCache: () => {
      const filtered = getStoredOrderHistory().filter((order) => order.status === status);
      return filtered.length > 0 ? filtered : null;
    },
    writeCache: setStoredOrderHistory,
    errorMessage: "Order history could not be loaded.",
  });
}

export async function createFoodOrder(): Promise<Order[]> {
  try {
    const response = await postFoodOrderOrder(createRequestOptions());

    if (response.status === 200) {
      const orders = normalizeOrders(response.data as Parameters<typeof normalizeOrders>[0]);
      setStoredOrders(orders);
      setStoredOrderHistory(orders.map((order) => toHistory(order)));
      clearStoredCart();
      return orders;
    }

    if (response.status >= 500) {
      return createFoodOrderOffline();
    }

    throw toApiError(
      response as { status: number; data?: { code?: string; message?: string } | void },
      "Order could not be created.",
    );
  } catch (error) {
    if (isOfflineLikeError(error)) {
      return createFoodOrderOffline();
    }

    throw error;
  }
}

export async function shareFoodOrder(request: ShareOrderRequest): Promise<boolean> {
  return mutateWithOfflineFallback<boolean>({
    apiCall: () => putFoodOrderShare(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: () => true,
    applyOffline: () => true,
    errorMessage: "Order could not be shared.",
  });
}

export async function updateFoodOrderStatus(
  orderId: Uuid,
  status: FoodOrderStatus,
): Promise<OrderHistory> {
  return mutateWithOfflineFallback<OrderHistory>({
    apiCall: () => putFoodOrderUpdateOrderIdStatus(orderId, status, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) =>
      normalizeOrderHistory(data as Parameters<typeof normalizeOrderHistory>[0]),
    onApiSuccess: (value) => {
      upsertOrder(value);
      upsertNotificationsFromOrderUpdate(value);
    },
    applyOffline: () => {
      const oldOrder = getStoredOrders().find((item) => item.id === orderId);

      if (!oldOrder) {
        throw new Error("Offline update failed because the order is not cached.");
      }

      const currentHistoryEntries =
        getStoredOrderHistory().find((item) => item.id === orderId)?.history ?? [];

      const updated: OrderHistory = {
        ...oldOrder,
        status,
        history: [
          ...currentHistoryEntries,
          {
            oldStatus: oldOrder.status,
            newStatus: status,
            statusChangeTime: new Date().toISOString(),
          },
        ],
      };

      upsertOrder(updated);
      upsertNotificationsFromOrderUpdate(updated);
      return updated;
    },
    errorMessage: "Order status could not be updated.",
  });
}
