import { getCredit as getCreditRequest, putCreditAdd } from "../generated/ffbAPI";
import type { CreditAddRequest, GetCredit200 } from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";

function getStoredCredit(): GetCredit200 | null {
  return readJson<GetCredit200 | null>(STORAGE_KEYS.credit, null);
}

function setStoredCredit(value: GetCredit200): GetCredit200 {
  return writeJson(STORAGE_KEYS.credit, value);
}

export async function getCredit(): Promise<GetCredit200> {
  return readThroughCache<GetCredit200>({
    apiCall: () => getCreditRequest(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as GetCredit200,
    readCache: () => getStoredCredit(),
    writeCache: setStoredCredit,
    errorMessage: "Credit could not be loaded.",
  });
}

export async function addCredit(request: CreditAddRequest): Promise<GetCredit200> {
  return mutateWithOfflineFallback<GetCredit200>({
    apiCall: () => putCreditAdd(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as GetCredit200,
    onApiSuccess: setStoredCredit,
    applyOffline: () => {
      const current = getStoredCredit() ?? { credit: 0 };

      return setStoredCredit({
        credit: (current.credit ?? 0) + (request.amount ?? 0),
      });
    },
    errorMessage: "Credit could not be updated.",
  });
}
