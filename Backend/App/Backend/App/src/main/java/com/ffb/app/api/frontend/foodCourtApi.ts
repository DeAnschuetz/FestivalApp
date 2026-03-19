import {
  getFoodCourt as getOwnFoodCourtRequest,
  getFoodCourtListAll,
  postFoodCourt,
  putFoodCourt,
} from "../generated/ffbAPI";
import type {
  FoodCourtRequestSimple,
  FoodCourtResponse,
  GetFoodCourt200,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache, saveStoredSession } from "./core/api";
import { fileToDataUrl, getStoredFoodCourtImage, responseToDataUrl, setStoredFoodCourtImage } from "./core/imageStorage";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";

type FoodCourtList = FoodCourtResponse[];

function getStoredFoodCourts(): FoodCourtList {
  return readJson<FoodCourtList>(STORAGE_KEYS.foodCourts, []);
}

function setStoredFoodCourts(foodCourts: FoodCourtList): FoodCourtList {
  return writeJson(STORAGE_KEYS.foodCourts, foodCourts);
}

function upsertFoodCourt(foodCourt: FoodCourtResponse): FoodCourtList {
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

export async function getAllFoodCourts(): Promise<FoodCourtResponse[]> {
  return readThroughCache<FoodCourtResponse[]>({
    apiCall: () => getFoodCourtListAll(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as FoodCourtResponse[],
    readCache: () => {
      const foodCourts = getStoredFoodCourts();
      return foodCourts.length > 0 ? foodCourts : null;
    },
    writeCache: setStoredFoodCourts,
    errorMessage: "Food courts could not be loaded.",
  });
}

export async function getOwnFoodCourt(): Promise<GetFoodCourt200> {
  return readThroughCache<GetFoodCourt200>({
    apiCall: () => getOwnFoodCourtRequest(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as GetFoodCourt200,
    readCache: () => {
      const ownId = getOwnFoodCourtId();
      const own = getStoredFoodCourts().find((item) => item.id === ownId);
      return own ?? null;
    },
    writeCache: (value) => {
      if (value.id) {
        setOwnFoodCourtId(value.id);
      }
      upsertFoodCourt(value);
    },
    errorMessage: "Own food court could not be loaded.",
  });
}

export async function createOwnFoodCourt(
  request: FoodCourtRequestSimple,
): Promise<FoodCourtResponse> {
  return mutateWithOfflineFallback<FoodCourtResponse>({
    apiCall: () => postFoodCourt(request, createRequestOptions()),
    expectedStatuses: [201],
    mapApiData: (data) => data as FoodCourtResponse,
    onApiSuccess: (value) => {
      if (value.id) {
        setOwnFoodCourtId(value.id);
      }
      upsertFoodCourt(value);
    },
    applyOffline: () => {
      const created: FoodCourtResponse = {
        id: crypto.randomUUID(),
        name: request.displayName ?? "Offline Food Court",
        waitingTime: 0,
      };

      if (created.id) {
        setOwnFoodCourtId(created.id);
      }

      upsertFoodCourt(created);
      return created;
    },
    errorMessage: "Food court could not be created.",
  });
}

export async function updateOwnFoodCourt(
  request: FoodCourtRequestSimple,
): Promise<FoodCourtResponse> {
  return mutateWithOfflineFallback<FoodCourtResponse>({
    apiCall: () => putFoodCourt(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as FoodCourtResponse,
    onApiSuccess: (value) => {
      if (value.id) {
        setOwnFoodCourtId(value.id);
      }
      upsertFoodCourt(value);
    },
    applyOffline: () => {
      const ownId = getOwnFoodCourtId() ?? crypto.randomUUID();

      const updated: FoodCourtResponse = {
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

export async function uploadOwnFoodCourtImage(file: File | Blob): Promise<string> {
  const ownFoodCourtId = getOwnFoodCourtId();

  if (!ownFoodCourtId) {
    throw new Error("No own food court is known. Create or load it first.");
  }

  const formData = new FormData();
  formData.append("file", file);

  try {
    const response = await fetch("/food_court/image", {
      ...createRequestOptions(),
      method: "POST",
      body: formData,
    });

    if (response.ok || response.status >= 500) {
      const dataUrl = await fileToDataUrl(file);
      setStoredFoodCourtImage(ownFoodCourtId, dataUrl);
      return dataUrl;
    }
  } catch {
    // fall back to local cache update below
  }

  const dataUrl = await fileToDataUrl(file);
  setStoredFoodCourtImage(ownFoodCourtId, dataUrl);
  return dataUrl;
}

export async function getFoodCourtImageUrl(foodCourtId: Uuid): Promise<string | null> {
  try {
    const response = await fetch(`/food_court/image/${foodCourtId}`, {
      ...createRequestOptions(),
      method: "GET",
    });

    if (response.ok) {
      const dataUrl = await responseToDataUrl(response);
      setStoredFoodCourtImage(foodCourtId, dataUrl);
      return dataUrl;
    }

    if (response.status >= 500) {
      return getStoredFoodCourtImage(foodCourtId);
    }

    return getStoredFoodCourtImage(foodCourtId);
  } catch {
    return getStoredFoodCourtImage(foodCourtId);
  }
}
