import {
  getNotificationListAll,
  putNotificationUpdateNotificationIdNewStatus,
} from "../generated/ffbAPI";
import type {
  FoodOrderNotificationResponse,
  FoodOrderResponse,
  FoodOrderResponseHistory,
  NotificationStatus,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";

export type StoredNotification = FoodOrderNotificationResponse;

function getStoredNotifications(): StoredNotification[] {
  return readJson<StoredNotification[]>(STORAGE_KEYS.notifications, []);
}

function setStoredNotifications(value: StoredNotification[]): StoredNotification[] {
  return writeJson(STORAGE_KEYS.notifications, value);
}

function normalizeNotificationList(raw: unknown): StoredNotification[] {
  return (raw as Array<FoodOrderResponse | FoodOrderNotificationResponse>).map((item) => {
    const notification = item as FoodOrderNotificationResponse;

    return {
      id: notification.id ?? crypto.randomUUID(),
      type: notification.type,
      status: notification.status ?? "NEW",
      message: notification.message ?? "Notification",
      creationTime: notification.creationTime ?? new Date().toISOString(),
      pickupTime: notification.pickupTime,
    };
  });
}

export function upsertNotificationsFromOrderUpdate(
  order: Pick<FoodOrderResponseHistory, "id" | "status" | "foodCourtName">,
): void {
  const next: StoredNotification = {
    id: crypto.randomUUID(),
    type: order.status,
    status: "NEW",
    message: `${order.foodCourtName ?? "Order"} changed to ${order.status ?? "UNKNOWN"}.`,
    creationTime: new Date().toISOString(),
  };

  setStoredNotifications([next, ...getStoredNotifications()]);
}

export async function getNotifications(): Promise<StoredNotification[]> {
  return readThroughCache<StoredNotification[]>({
    apiCall: () => getNotificationListAll(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: normalizeNotificationList,
    readCache: () => {
      const notifications = getStoredNotifications();
      return notifications.length > 0 ? notifications : null;
    },
    writeCache: setStoredNotifications,
    errorMessage: "Notifications could not be loaded.",
  });
}

export async function updateNotificationStatus(
  notificationId: Uuid,
  newStatus: NotificationStatus,
): Promise<StoredNotification[]> {
  return mutateWithOfflineFallback<StoredNotification[]>({
    apiCall: () =>
      putNotificationUpdateNotificationIdNewStatus(
        notificationId,
        newStatus,
        createRequestOptions(),
      ),
    expectedStatuses: [200],
    mapApiData: normalizeNotificationList,
    onApiSuccess: setStoredNotifications,
    applyOffline: () => {
      const updated = getStoredNotifications().map((item) =>
        item.id === notificationId ? { ...item, status: newStatus } : item,
      );

      return setStoredNotifications(updated);
    },
    errorMessage: "Notification status could not be updated.",
  });
}
