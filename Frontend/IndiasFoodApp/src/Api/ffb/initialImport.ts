
import type {
  AccountType,
  CreditHistoryResponse,
  FoodOrderStatus,
  NotificationStatus,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { AccountType as AccountTypeValues } from "../generated/ffbAPI.schemas";
import { saveStoredSession } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { deleteJson, readJson, writeJson } from "./core/storage";
import {
  normalizeCart,
  normalizeCredit,
  normalizeFoodCourt,
  normalizeFoodOrderStatus,
  normalizeNotification,
  normalizeNotificationStatus,
  normalizeOrder,
  normalizeOrderHistory,
  normalizeProduct,
} from "./normalizers";
import type { ImportedAccountData, InitialImportData, Cart } from "./types";

type StoredOfflineAccount = {
  loginNr: string;
  devPassword: string;
  type: AccountType;
};

const OFFLINE_ACCOUNTS_KEY = "ffb.offlineAccounts.v1";

type RawImportData = {
  version?: unknown;
  generatedAt?: unknown;
  source?: unknown;
  global?: {
    foodCourts?: unknown;
    products?: unknown;
    productAssignments?: unknown;
    productFoodCourtMap?: unknown;
    foodCourtImages?: unknown;
  };
  accounts?: unknown;
};

type RawImportedAccountData = {
  accountId?: unknown;
  loginNr?: unknown;
  type?: unknown;
  devPassword?: unknown;
  ownFoodCourtId?: unknown;
  cart?: unknown;
  credit?: unknown;
  creditHistory?: unknown;
  visibleOrders?: unknown;
  visibleOrderHistory?: unknown;
  notifications?: unknown;
};

function getEmptyCart(): Cart {
  return {
    hasPrio: false,
    total: 0,
    cartItems: [],
  };
}

function isRecord(value: unknown): value is Record<string, unknown> {
  return typeof value === "object" && value !== null;
}

function asArray<T>(value: unknown, fallback: T[] = []): T[] {
  return Array.isArray(value) ? (value as T[]) : fallback;
}

function requireString(value: unknown, fieldName: string): string {
  if (typeof value !== "string" || value.trim() === "") {
    throw new Error(`Initial import field "${fieldName}" must be a non-empty string.`);
  }

  return value;
}

function parseAccountType(value: unknown): AccountType {
  switch (value) {
    case AccountTypeValues.ADMIN:
    case AccountTypeValues.FOOD_COURT_WORKER:
    case AccountTypeValues.GUEST:
      return value;
    default:
      throw new Error(`Unknown account type in initial import: ${String(value)}`);
  }
}

function normalizeImportedAccount(rawAccount: RawImportedAccountData): ImportedAccountData {
  return {
    accountId: requireString(rawAccount.accountId, "accounts[].accountId") as Uuid,
    loginNr: requireString(rawAccount.loginNr, "accounts[].loginNr"),
    devPassword:
      typeof rawAccount.devPassword === "string" && rawAccount.devPassword.trim() !== ""
        ? rawAccount.devPassword
        : undefined,
    type: parseAccountType(rawAccount.type),
    ownFoodCourtId:
      typeof rawAccount.ownFoodCourtId === "string"
        ? (rawAccount.ownFoodCourtId as Uuid)
        : undefined,
    cart: isRecord(rawAccount.cart) ? normalizeCart(rawAccount.cart) : null,
    credit: isRecord(rawAccount.credit) ? normalizeCredit(rawAccount.credit) : null,
    creditHistory: asArray<CreditHistoryResponse>(rawAccount.creditHistory),
    visibleOrders: asArray(rawAccount.visibleOrders).map((order) =>
      normalizeOrder(order as Parameters<typeof normalizeOrder>[0]),
    ),
    visibleOrderHistory: asArray(rawAccount.visibleOrderHistory).map((order) =>
      normalizeOrderHistory(order as Parameters<typeof normalizeOrderHistory>[0]),
    ),
    notifications: asArray(rawAccount.notifications).map((notification) =>
      normalizeNotification(notification as Parameters<typeof normalizeNotification>[0]),
    ),
  };
}

function normalizeImportData(raw: unknown): InitialImportData {
  const parsed: unknown = typeof raw === "string" ? JSON.parse(raw) : raw;

  if (!isRecord(parsed)) {
    throw new Error("Initial import must be an object or a JSON string.");
  }

  const importData = parsed as RawImportData;
  const global = isRecord(importData.global) ? importData.global : {};

  return {
    version: 1,
    generatedAt:
      typeof importData.generatedAt === "string"
        ? importData.generatedAt
        : new Date().toISOString(),
    source: typeof importData.source === "string" ? importData.source : "manual-import",
    global: {
      foodCourts: asArray(global.foodCourts).map((foodCourt) =>
        normalizeFoodCourt(foodCourt as Parameters<typeof normalizeFoodCourt>[0]),
      ),
      products: asArray(global.products).map((product) =>
        normalizeProduct(product as Parameters<typeof normalizeProduct>[0]),
      ),
      productAssignments: asArray(global.productAssignments),
      productFoodCourtMap: isRecord(global.productFoodCourtMap)
        ? (global.productFoodCourtMap as Record<string, Uuid>)
        : {},
      foodCourtImages: isRecord(global.foodCourtImages)
        ? (global.foodCourtImages as Record<string, string>)
        : {},
    },
    accounts: asArray<RawImportedAccountData>(importData.accounts).map(normalizeImportedAccount),
  };
}

function writeGlobalImportData(data: InitialImportData): void {
  writeJson(STORAGE_KEYS.foodCourts, data.global.foodCourts);
  writeJson(STORAGE_KEYS.products, data.global.products);
  writeJson(STORAGE_KEYS.productAssignments, data.global.productAssignments);
  writeJson(STORAGE_KEYS.productFoodCourtMap, data.global.productFoodCourtMap);
  writeJson(STORAGE_KEYS.foodCourtImages, data.global.foodCourtImages);
}

function writeOfflineAccounts(data: InitialImportData): void {
  const offlineAccounts: StoredOfflineAccount[] = data.accounts
    .filter((account) => typeof account.devPassword === "string" && account.devPassword.trim() !== "")
    .map((account) => ({
      loginNr: account.loginNr,
      devPassword: account.devPassword!,
      type: account.type,
    }));

  writeJson(OFFLINE_ACCOUNTS_KEY, offlineAccounts);
}

export function importInitialData(raw: unknown): InitialImportData {
  const data = normalizeImportData(raw);

  writeJson(STORAGE_KEYS.initialImport, data);
  writeGlobalImportData(data);
  writeOfflineAccounts(data);
  return data;
}

export function getImportedInitialData(): InitialImportData | null {
  return readJson<InitialImportData | null>(STORAGE_KEYS.initialImport, null);
}

export function listImportedAccounts(): ImportedAccountData[] {
  return getImportedInitialData()?.accounts ?? [];
}

export function findImportedAccount(loginNr: string): ImportedAccountData | null {
  return listImportedAccounts().find((account) => account.loginNr === loginNr) ?? null;
}

export function applyImportedAccount(loginNr: string): ImportedAccountData {
  const data = getImportedInitialData();

  if (!data) {
    throw new Error("No initial import data was found in local storage.");
  }

  const importedAccount = data.accounts.find((account) => account.loginNr === loginNr);

  if (!importedAccount) {
    throw new Error(`No imported account data was found for loginNr ${loginNr}.`);
  }

  writeGlobalImportData(data);
  writeJson(STORAGE_KEYS.cart, importedAccount.cart ?? getEmptyCart());
  writeJson(STORAGE_KEYS.credit, importedAccount.credit ?? null);
  writeJson(STORAGE_KEYS.orders, importedAccount.visibleOrders);
  writeJson(STORAGE_KEYS.orderHistory, importedAccount.visibleOrderHistory);
  writeJson(STORAGE_KEYS.notifications, importedAccount.notifications);
  writeJson(STORAGE_KEYS.ownFoodCourtId, importedAccount.ownFoodCourtId);

  saveStoredSession({
    loginNr: importedAccount.loginNr,
    accountType: importedAccount.type,
    ownFoodCourtId: importedAccount.ownFoodCourtId,
  });

  return importedAccount;
}

export function clearImportedInitialData(): void {
  deleteJson(STORAGE_KEYS.initialImport);
  deleteJson(OFFLINE_ACCOUNTS_KEY);
}

export function getImportedOwnFoodCourtId(loginNr: string): Uuid | undefined {
  return findImportedAccount(loginNr)?.ownFoodCourtId;
}
