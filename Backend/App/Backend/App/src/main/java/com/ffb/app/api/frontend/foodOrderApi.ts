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
  FoodOrderHistoryResponse,
  FoodOrderResponse,
  FoodOrderResponseHistory,
  FoodOrderStatus,
  ShareOrderRequest,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";
import { clearStoredCart, getCart } from "./cartApi";
import { upsertNotificationsFromOrderUpdate } from "./notificationApi";
import { isOfflineLikeError, toApiError } from "./core/errors";

function getStoredOrders(): FoodOrderResponse[] {
  return readJson<FoodOrderResponse[]>(STORAGE_KEYS.orders, []);
}

function setStoredOrders(orders: FoodOrderResponse[]): FoodOrderResponse[] {
  return writeJson(STORAGE_KEYS.orders, orders);
}

function getStoredOrderHistory(): FoodOrderResponseHistory[] {
  return readJson<FoodOrderResponseHistory[]>(STORAGE_KEYS.orderHistory, []);
}

function setStoredOrderHistory(orders: FoodOrderResponseHistory[]): FoodOrderResponseHistory[] {
  return writeJson(STORAGE_KEYS.orderHistory, orders);
}

function toHistory(order: FoodOrderResponse, oldStatus?: FoodOrderStatus): FoodOrderResponseHistory {
  const historyEntry: FoodOrderHistoryResponse | undefined = order.status
    ? {
        oldStatus,
        newStatus: order.status,
        statusChangeTime: new Date().toISOString(),
      }
    : undefined;

  return {
    ...order,
    history: historyEntry ? [historyEntry] : [],
  };
}

function upsertOrder(order: FoodOrderResponse): FoodOrderResponse[] {
  const current = getStoredOrders().filter((item) => item.id !== order.id);
  current.push(order);
  setStoredOrders(current);

  const existingHistory = getStoredOrderHistory().find((item) => item.id === order.id);
  const history = getStoredOrderHistory().filter((item) => item.id !== order.id);
  history.push({
    ...toHistory(order),
    history: [
      ...(existingHistory?.history ?? []),
      ...(order.status
        ? [
            {
              oldStatus: existingHistory?.status,
              newStatus: order.status,
              statusChangeTime: new Date().toISOString(),
            } satisfies FoodOrderHistoryResponse,
          ]
        : []),
    ],
  });
  setStoredOrderHistory(history);

  return current;
}

async function createFoodOrderOffline(): Promise<FoodOrderResponse[]> {
  const currentCart = await getCart();

  const order: FoodOrderResponse = {
    id: crypto.randomUUID(),
    status: "ORDERED",
    foodCourtName: currentCart.cartItems?.[0]?.displayName ?? "Offline Food Court",
    waitingTime: 0,
    orderItems: currentCart.cartItems?.map((item) => ({
      productID: item.id,
      displayName: item.displayName,
      iconIdentifier: item.symbolIdentifier,
      count: item.count,
      extra: item.extra,
      subItems: item.subItems?.map((subItem) => ({
        productID: subItem.id,
        displayName: subItem.displayName,
        iconIdentifier: subItem.symbolIdentifier,
        count: subItem.count,
        extra: subItem.extra,
        subItems: [],
      })),
    })) ?? [],
  };

  const orders = [...getStoredOrders(), order];
  setStoredOrders(orders);
  setStoredOrderHistory([...getStoredOrderHistory(), toHistory(order)]);
  clearStoredCart();
  return orders;
}

export async function getVisibleOrders(): Promise<FoodOrderResponse[]> {
  return readThroughCache<FoodOrderResponse[]>({
    apiCall: () => getFoodOrderListAll(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as FoodOrderResponse[],
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
): Promise<FoodOrderResponse[]> {
  return readThroughCache<FoodOrderResponse[]>({
    apiCall: () => getFoodOrderListAllByStatusStatus(status, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as FoodOrderResponse[],
    readCache: () => {
      const filtered = getStoredOrders().filter((order) => order.status === status);
      return filtered.length > 0 ? filtered : null;
    },
    writeCache: (orders) => {
      setStoredOrders(orders);
    },
    errorMessage: "Orders could not be loaded.",
  });
}

export async function getVisibleOrdersWithHistory(): Promise<FoodOrderResponseHistory[]> {
  return readThroughCache<FoodOrderResponseHistory[]>({
    apiCall: () => getFoodOrderListAllHistory(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as FoodOrderResponseHistory[],
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
): Promise<FoodOrderResponseHistory[]> {
  return readThroughCache<FoodOrderResponseHistory[]>({
    apiCall: () => getFoodOrderListAllByStatusStatusHistory(status, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as FoodOrderResponseHistory[],
    readCache: () => {
      const filtered = getStoredOrderHistory().filter((order) => order.status === status);
      return filtered.length > 0 ? filtered : null;
    },
    writeCache: setStoredOrderHistory,
    errorMessage: "Order history could not be loaded.",
  });
}

export async function createFoodOrder(): Promise<FoodOrderResponse[]> {
  try {
    const response = await postFoodOrderOrder(createRequestOptions());

    if (response.status === 200) {
      const orders = response.data as FoodOrderResponse[];
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
): Promise<FoodOrderResponseHistory> {
  return mutateWithOfflineFallback<FoodOrderResponseHistory>({
    apiCall: () => putFoodOrderUpdateOrderIdStatus(orderId, status, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as FoodOrderResponseHistory,
    onApiSuccess: (value) => {
      upsertOrder(value);
      upsertNotificationsFromOrderUpdate(value);
    },
    applyOffline: () => {
      const currentOrders = getStoredOrders();
      const oldOrder = currentOrders.find((item) => item.id === orderId);

      if (!oldOrder) {
        throw new Error("Offline update failed because the order is not cached.");
      }

      const updated: FoodOrderResponseHistory = {
        ...oldOrder,
        status,
        history: [
          ...(getStoredOrderHistory().find((item) => item.id === orderId)?.history ?? []),
          {
            oldStatus: oldOrder.status,
            newStatus: status,
            statusChangeTime: new Date().toISOString(),
          },
        ],
      };

      setStoredOrders(
        currentOrders.map((item) =>
          item.id === orderId ? { ...item, status } : item,
        ),
      );

      const currentHistory = getStoredOrderHistory();
      const alreadyExists = currentHistory.some((item) => item.id === orderId);

      setStoredOrderHistory(
        alreadyExists
          ? currentHistory.map((item) => (item.id === orderId ? updated : item))
          : [...currentHistory, updated],
      );

      upsertNotificationsFromOrderUpdate(updated);

      return updated;
    },
    errorMessage: "Order status could not be updated.",
  });
}
