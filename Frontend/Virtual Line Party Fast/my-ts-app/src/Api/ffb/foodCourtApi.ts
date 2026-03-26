
import {
  getFoodCourt as getOwnFoodCourtRequest,
  getFoodCourtListAll,
  postFoodCourt,
  putFoodCourt,
  postFoodCourtImage,
  getFoodCourtImageFoodCourtId,
} from "../generated/ffbAPI";

import type {
  FoodCourtRequestSimple,
  Uuid,
} from "../generated/ffbAPI.schemas";

import {
  createRequestOptions,
  mutateWithOfflineFallback,
  readThroughCache,
  saveStoredSession,
} from "./core/api";

import {
  fileToDataUrl,
  getStoredFoodCourtImage,
  responseToDataUrl,
  setStoredFoodCourtImage,
} from "./core/imageStorage";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";
import { normalizeFoodCourt, normalizeFoodCourts } from "./normalizers";
import type { FoodCourt } from "./types";

type FoodCourtList = FoodCourt[];

function getStoredFoodCourts(): FoodCourtList {
  return readJson<FoodCourtList>(STORAGE_KEYS.foodCourts, []);
}

function setStoredFoodCourts(foodCourts: FoodCourtList): FoodCourtList {
  return writeJson(STORAGE_KEYS.foodCourts, foodCourts);
}

function upsertFoodCourt(foodCourt: FoodCourt): FoodCourtList {
  const current = getStoredFoodCourts().filter((item) => item.id !== foodCourt.id);
  current.push(foodCourt);
  return setStoredFoodCourts(current);
}

export function getOwnFoodCourtId(): Uuid | undefined {
  return readJson<Uuid | undefined>(STORAGE_KEYS.ownFoodCourtId, undefined);
}

function setOwnFoodCourtId(id: Uuid | undefined): void {
  writeJson(STORAGE_KEYS.ownFoodCourtId, id);
  saveStoredSession({ ownFoodCourtId: id });
}

export async function getAllFoodCourts(): Promise<FoodCourt[]> {
  return readThroughCache<FoodCourt[]>({
    apiCall: () => getFoodCourtListAll(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeFoodCourts(data as Parameters<typeof normalizeFoodCourts>[0]),
    readCache: () => {
      const foodCourts = getStoredFoodCourts();
      return foodCourts.length > 0 ? foodCourts : null;
    },
    writeCache: setStoredFoodCourts,
    errorMessage: "Food courts could not be loaded.",
  });
}

export async function getOwnFoodCourt(): Promise<FoodCourt> {
  return readThroughCache<FoodCourt>({
    apiCall: () => getOwnFoodCourtRequest(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeFoodCourt(data as Parameters<typeof normalizeFoodCourt>[0]),
    readCache: () => {
      const ownId = getOwnFoodCourtId();
      const own = getStoredFoodCourts().find((item) => item.id === ownId);
      return own ?? null;
    },
    writeCache: (value) => {
      setOwnFoodCourtId(value.id);
      upsertFoodCourt(value);
    },
    errorMessage: "Own food court could not be loaded.",
  });
}

export async function createOwnFoodCourt(
  request: FoodCourtRequestSimple,
): Promise<FoodCourt> {
  return mutateWithOfflineFallback<FoodCourt>({
    apiCall: () => postFoodCourt(request, createRequestOptions()),
    expectedStatuses: [201],
    mapApiData: (data) => normalizeFoodCourt(data as Parameters<typeof normalizeFoodCourt>[0]),
    onApiSuccess: (value) => {
      setOwnFoodCourtId(value.id);
      upsertFoodCourt(value);
    },
    applyOffline: () => {
      const created: FoodCourt = {
        id: crypto.randomUUID(),
        name: request.displayName ?? "Offline Food Court",
        waitingTime: 0,
      };

      setOwnFoodCourtId(created.id);
      upsertFoodCourt(created);
      return created;
    },
    errorMessage: "Food court could not be created.",
  });
}

export async function updateOwnFoodCourt(
  request: FoodCourtRequestSimple,
): Promise<FoodCourt> {
  return mutateWithOfflineFallback<FoodCourt>({
    apiCall: () => putFoodCourt(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeFoodCourt(data as Parameters<typeof normalizeFoodCourt>[0]),
    onApiSuccess: (value) => {
      setOwnFoodCourtId(value.id);
      upsertFoodCourt(value);
    },
    applyOffline: () => {
      const ownId = getOwnFoodCourtId() ?? crypto.randomUUID();

      const updated: FoodCourt = {
        id: ownId,
        name: request.displayName ?? "Offline Food Court",
        waitingTime: 0,
      };

      setOwnFoodCourtId(ownId);
      upsertFoodCourt(updated);
      return updated;
    },
    errorMessage: "Food court could not be updated.",
  });
}

export async function uploadOwnFoodCourtImage(file: File): Promise<string> {
  const ownFoodCourtId = getOwnFoodCourtId();

  if (!ownFoodCourtId) {
    throw new Error("No own food court is known. Create or load it first.");
  }

  const dataUrl = await fileToDataUrl(file);

  return mutateWithOfflineFallback<string>({
    apiCall: () => postFoodCourtImage({ file }, createRequestOptions()),
    expectedStatuses: [200, 201, 202, 204],
    mapApiData: () => dataUrl,
    onApiSuccess: (value) => {
      setStoredFoodCourtImage(ownFoodCourtId, value);
    },
    applyOffline: () => {
      setStoredFoodCourtImage(ownFoodCourtId, dataUrl);
      return dataUrl;
    },
    errorMessage: "Food court image could not be uploaded.",
  });
}

export async function getFoodCourtImageUrl(foodCourtId: Uuid): Promise<string | null> {
  return readThroughCache<string | null>({
    apiCall: () => getFoodCourtImageFoodCourtId(foodCourtId, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: async (data) => {
      const blob = data as Blob;

      if (!blob || blob.size === 0) {
        return null;
      }

      return responseToDataUrl(new Response(blob));
    },
    readCache: () => getStoredFoodCourtImage(foodCourtId),
    writeCache: (value) => {
      if (value) {
        setStoredFoodCourtImage(foodCourtId, value);
      }
    },
    errorMessage: "Food court image could not be loaded.",
  });
}