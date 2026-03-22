
import {
  AccountType,
  FoodOrderStatus,
  NotificationStatus,
} from "../generated/ffbAPI.schemas";
import type {
  AccountType as AccountTypeType,
  FoodOrderStatus as FoodOrderStatusType,
  NotificationStatus as NotificationStatusType,
  Uuid,
  CartItemResponse,
  CartResponse,
  CreditResponse,
} from "../generated/ffbAPI.schemas";
import type {
  Cart,
  CartItem,
  Credit,
  FoodCourt,
  OrderHistoryEntry,
  OrderItem,
  LoginResult,
  Notification,
  Order,
  OrderHistory,
  Product,
  RegisterResult,
  RawCartLike,
  RawCreditLike,
  RawFoodCourtLike,
  RawHistoryEntryLike,
  RawLoginLike,
  RawNotificationLike,
  RawOrderItemLike,
  RawOrderLike,
  RawProductLike,
  RawRegisterLike,
} from "./types";

function requireString(value: unknown, fieldName: string): string {
  if (typeof value !== "string" || value.trim() === "") {
    throw new Error(`Missing required field: ${fieldName}`);
  }

  return value;
}

function requireNumber(value: unknown, fieldName: string): number {
  if (typeof value !== "number" || Number.isNaN(value)) {
    throw new Error(`Missing required field: ${fieldName}`);
  }

  return value;
}

function requireUuid(value: unknown, fieldName: string): Uuid {
  return requireString(value, fieldName) as Uuid;
}

function normalizeAccountType(value: unknown, fieldName: string): AccountTypeType {
  if (Object.values(AccountType).includes(value as AccountTypeType)) {
    return value as AccountTypeType;
  }

  throw new Error(`Missing or invalid account type: ${fieldName}`);
}

export function inferAccountTypeFromLoginNr(loginNr: string): AccountTypeType {
  if (loginNr.startsWith("A-")) {
    return AccountType.ADMIN;
  }

  if (loginNr.startsWith("F-")) {
    return AccountType.FOOD_COURT_WORKER;
  }

  return AccountType.GUEST;
}

export function normalizeFoodOrderStatus(
  value: unknown,
  fieldName: string,
): FoodOrderStatusType {
  if (Object.values(FoodOrderStatus).includes(value as FoodOrderStatusType)) {
    return value as FoodOrderStatusType;
  }

  throw new Error(`Missing or invalid food order status: ${fieldName}`);
}

export function normalizeNotificationStatus(
  value: unknown,
  fieldName: string,
): NotificationStatusType {
  if (Object.values(NotificationStatus).includes(value as NotificationStatusType)) {
    return value as NotificationStatusType;
  }

  throw new Error(`Missing or invalid notification status: ${fieldName}`);
}

export function normalizeFoodCourt(foodCourt: RawFoodCourtLike): FoodCourt {
  return {
    id: requireUuid(foodCourt.id, "FoodCourtResponse.id"),
    name: requireString(foodCourt.name, "FoodCourtResponse.name"),
    waitingTime: requireNumber(foodCourt.waitingTime, "FoodCourtResponse.waitingTime"),
  };
}

export function normalizeFoodCourts(foodCourts: RawFoodCourtLike[] | undefined | null): FoodCourt[] {
  return (foodCourts ?? []).map(normalizeFoodCourt);
}

export function normalizeOrderItem(item: RawOrderItemLike, path = "FoodOrderItemResponse"): OrderItem {
  return {
    productId: requireUuid(item.productId, `${path}.productId`),
    displayName: requireString(item.displayName, `${path}.displayName`),
    iconIdentifier: requireString(item.iconIdentifier, `${path}.iconIdentifier`),
    count: requireNumber(item.count, `${path}.count`),
    extra: item.extra ?? "",
    subItems: (item.subItems ?? []).map((subItem, index) =>
      normalizeOrderItem(subItem, `${path}.subItems[${index}]`),
    ),
  };
}

export function normalizeOrder(order: RawOrderLike): Order {
  return {
    id: requireUuid(order.id, "FoodOrderResponse.id"),
    status: normalizeFoodOrderStatus(order.status, "FoodOrderResponse.status"),
    foodCourtName: requireString(order.foodCourtName, "FoodOrderResponse.foodCourtName"),
    waitingTime: requireNumber(order.waitingTime, "FoodOrderResponse.waitingTime"),
    orderItems: (order.orderItems ?? []).map((item, index) =>
      normalizeOrderItem(item, `FoodOrderResponse.orderItems[${index}]`),
    ),
  };
}

export function normalizeOrders(orders: RawOrderLike[] | undefined | null): Order[] {
  return (orders ?? []).map(normalizeOrder);
}

export function normalizeHistoryEntry(
  entry: RawHistoryEntryLike,
  path = "FoodOrderHistoryResponse",
): OrderHistoryEntry {
  return {
    oldStatus: entry.oldStatus
      ? normalizeFoodOrderStatus(entry.oldStatus, `${path}.oldStatus`)
      : undefined,
    newStatus: normalizeFoodOrderStatus(entry.newStatus, `${path}.newStatus`),
    statusChangeTime: requireString(entry.statusChangeTime, `${path}.statusChangeTime`),
  };
}

export function normalizeOrderHistory(order: RawOrderLike): OrderHistory {
  const base = normalizeOrder(order);

  return {
    ...base,
    history: ((order as { history?: RawHistoryEntryLike[] }).history ?? []).map((entry, index) =>
      normalizeHistoryEntry(entry, `FoodOrderResponseHistory.history[${index}]`),
    ),
  };
}

export function normalizeOrderHistories(
  orders: RawOrderLike[] | undefined | null,
): OrderHistory[] {
  return (orders ?? []).map(normalizeOrderHistory);
}

export function normalizeNotification(notification: RawNotificationLike): Notification {
  return {
    id: requireUuid(notification.id, "FoodOrderNotificationResponse.id"),
    type: normalizeFoodOrderStatus(notification.type, "FoodOrderNotificationResponse.type"),
    status: normalizeNotificationStatus(
      notification.status,
      "FoodOrderNotificationResponse.status",
    ),
    message: requireString(notification.message, "FoodOrderNotificationResponse.message"),
    creationTime: requireString(
      notification.creationTime,
      "FoodOrderNotificationResponse.creationTime",
    ),
    pickupTime: notification.pickupTime ?? undefined,
  };
}

export function normalizeNotifications(
  notifications: RawNotificationLike[] | undefined | null,
): Notification[] {
  return (notifications ?? []).map(normalizeNotification);
}

export function normalizeProduct(product: RawProductLike, path = "ProductResponse"): Product {
  return {
    id: requireUuid(product.id, `${path}.id`),
    price: requireNumber(product.price, `${path}.price`),
    displayName: requireString(product.displayName, `${path}.displayName`),
    symbolIdentifier: requireString(product.symbolIdentifier, `${path}.symbolIdentifier`),
    minimalWarning: requireNumber(product.minimalWarning, `${path}.minimalWarning`),
    productCount: requireNumber(product.productCount, `${path}.productCount`),
    subProducts: (product.subProducts ?? []).map((subProduct, index) =>
      normalizeProduct(subProduct, `${path}.subProducts[${index}]`),
    ),
  };
}

export function normalizeProducts(products: RawProductLike[] | undefined | null): Product[] {
  return (products ?? []).map((product, index) =>
    normalizeProduct(product, `ProductResponse[${index}]`),
  );
}

export function normalizeCartItem(item: CartItemResponse, path = "CartItemResponse"): CartItem {
  return {
    id: requireUuid(item.id, `${path}.id`),
    displayName: requireString(item.displayName, `${path}.displayName`),
    symbolIdentifier: requireString(item.symbolIdentifier, `${path}.symbolIdentifier`),
    price: requireNumber(item.price, `${path}.price`),
    count: requireNumber(item.count, `${path}.count`),
    extra: item.extra ?? "",
    subItems: (item.subItems ?? []).map((subItem, index) =>
      normalizeCartItem(subItem, `${path}.subItems[${index}]`),
    ),
  };
}

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === "object" && value !== null;
}

function requireBoolean(value: unknown, fieldName: string): boolean {
  if (typeof value !== "boolean") {
    throw new Error(`Field "${fieldName}" must be a boolean.`);
  }
  return value;
}

function asArray<T>(value: unknown, fallback: T[] = []): T[] {
  return Array.isArray(value) ? (value as T[]) : fallback;
}
export function normalizeCart(raw: unknown): Cart {
  if (!isRecord(raw)) {
    throw new Error("Invalid cart.");
  }

  return {
    hasPrio: requireBoolean(raw.hasPrio, "cart.hasPrio"),
    total: requireNumber(raw.total, "cart.total"),
    cartItems: asArray(raw.cartItems),
  };
}

export function normalizeCredit(raw: unknown): Credit {
  if (!isRecord(raw)) {
    throw new Error("Invalid credit.");
  }

  return {
    credit: requireNumber(raw.credit, "credit.credit"),
  };
}

export function normalizeLoginResult(value: RawLoginLike): LoginResult {
  return {
    loginNr: requireString(value.loginNr, "PostAccountLogin200.loginNr"),
    token: requireString(value.token, "PostAccountLogin200.token"),
  };
}

export function normalizeRegisterResult(value: RawRegisterLike): RegisterResult {
  return {
    id: requireUuid(value.id, "PostAccountRegister201.id"),
    loginNr: requireString(value.loginNr, "PostAccountRegister201.loginNr"),
    type: value.type
      ? normalizeAccountType(value.type, "PostAccountRegister201.type")
      : AccountType.GUEST,
  };
}
