import type { GetCart200, Uuid } from "../generated/ffbAPI.schemas";
import { saveStoredSession } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson, deleteJson } from "./core/storage";
import type { ImportedAccountData, InitialImportData } from "./import.types";

function getEmptyCart(): GetCart200 {
  return {
    hasPrio: false,
    total: 0,
    cartItems: [],
  };
}

function normalizeImportData(raw: InitialImportData | string): InitialImportData {
  if (typeof raw === "string") {
    return JSON.parse(raw) as InitialImportData;
  }

  return raw;
}

function writeGlobalImportData(data: InitialImportData): void {
  writeJson(STORAGE_KEYS.foodCourts, data.global.foodCourts);
  writeJson(STORAGE_KEYS.products, data.global.products);
  writeJson(STORAGE_KEYS.productAssignments, data.global.productAssignments);
  writeJson(STORAGE_KEYS.productFoodCourtMap, data.global.productFoodCourtMap);
  writeJson(STORAGE_KEYS.foodCourtImages, data.global.foodCourtImages);
}

export function importInitialData(raw: InitialImportData | string): InitialImportData {
  const data = normalizeImportData(raw);

  writeJson(STORAGE_KEYS.initialImport, data);
  writeGlobalImportData(data);

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
  writeJson(STORAGE_KEYS.orders, importedAccount.visibleOrders ?? []);
  writeJson(STORAGE_KEYS.orderHistory, importedAccount.visibleOrderHistory ?? []);
  writeJson(STORAGE_KEYS.notifications, importedAccount.notifications ?? []);
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
}

export function getImportedOwnFoodCourtId(loginNr: string): Uuid | undefined {
  return findImportedAccount(loginNr)?.ownFoodCourtId;
}
