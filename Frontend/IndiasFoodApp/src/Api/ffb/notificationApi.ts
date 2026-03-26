
import {
  getNotificationListAll,
  putNotificationUpdateNotificationIdNewStatus,
} from "../generated/ffbAPI";
import type {
  FoodOrderStatus,
  NotificationStatus,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";
import {
  normalizeFoodOrderStatus,
  normalizeNotifications,
  normalizeNotificationStatus,
} from "./normalizers";
import type { Notification, OrderHistory } from "./types";

function getStoredNotifications(): Notification[] {
  return readJson<Notification[]>(STORAGE_KEYS.notifications, []);
}

function setStoredNotifications(value: Notification[]): Notification[] {
  return writeJson(STORAGE_KEYS.notifications, value);
}

function normalizeNotificationList(raw: unknown): Notification[] {
  return normalizeNotifications(raw as Parameters<typeof normalizeNotifications>[0]);
}

export function upsertNotificationsFromOrderUpdate(
  order: Pick<OrderHistory, "id" | "status" | "foodCourtName">,
): void {
  const next: Notification = {
    id: crypto.randomUUID(),
    type: normalizeFoodOrderStatus(order.status, "order.status"),
    status: normalizeNotificationStatus("NEW", "notification.status"),
    message: `${order.foodCourtName} changed to ${order.status}.`,
    creationTime: new Date().toISOString(),
  };

  setStoredNotifications([next, ...getStoredNotifications()]);
}

export async function getNotifications(): Promise<Notification[]> {
  return readThroughCache<Notification[]>({
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
): Promise<Notification[]> {
  return mutateWithOfflineFallback<Notification[]>({
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
