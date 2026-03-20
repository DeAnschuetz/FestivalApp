
import { getCredit as getCreditRequest, putCreditAdd } from "../generated/ffbAPI";
import type { CreditAddRequest } from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";
import { normalizeCredit } from "./normalizers";
import type { Credit } from "./types";

function getStoredCredit(): Credit | null {
  return readJson<Credit | null>(STORAGE_KEYS.credit, null);
}

function setStoredCredit(value: Credit): Credit {
  return writeJson(STORAGE_KEYS.credit, value);
}

export async function getCredit(): Promise<Credit> {
  return readThroughCache<Credit>({
    apiCall: () => getCreditRequest(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeCredit(data as Parameters<typeof normalizeCredit>[0]),
    readCache: () => getStoredCredit(),
    writeCache: setStoredCredit,
    errorMessage: "Credit could not be loaded.",
  });
}

export async function addCredit(request: CreditAddRequest): Promise<Credit> {
  return mutateWithOfflineFallback<Credit>({
    apiCall: () => putCreditAdd(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeCredit(data as Parameters<typeof normalizeCredit>[0]),
    onApiSuccess: setStoredCredit,
    applyOffline: () => {
      const current = getStoredCredit() ?? { credit: 0 };

      return setStoredCredit({
        credit: current.credit + (request.amount ?? 0),
      });
    },
    errorMessage: "Credit could not be updated.",
  });
}
