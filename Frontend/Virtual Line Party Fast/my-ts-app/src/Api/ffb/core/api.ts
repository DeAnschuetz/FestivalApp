import { postAccountLogout } from "../../generated/ffbAPI";
import type { AccountType, Uuid } from "../../generated/ffbAPI.schemas";
import { STORAGE_KEYS } from "./keys";
import { deleteJson, readJson, writeJson } from "./storage";
import { isOfflineLikeError, toApiError } from "./errors";

export type StoredSession = {
  loginNr?: string;
  token?: string;
  accountType?: AccountType;
  ownFoodCourtId?: Uuid;
};

type ApiResponse = {
  status: number;
  data?: unknown;
};

function buildHeaders(extraHeaders?: HeadersInit): HeadersInit {
  const session = getStoredSession();

  const headers = new Headers(extraHeaders);

  if (session.token) {
    headers.set("Authorization", `Bearer ${session.token}`);
    console.log(session.token);
  }

  return headers;
}

export function createRequestOptions(options?: RequestInit): RequestInit {
  return {
    ...options,
    headers: buildHeaders(options?.headers),
  };
}

export function getStoredSession(): StoredSession {
  return readJson<StoredSession>(STORAGE_KEYS.session, {});
}

export function saveStoredSession(partial: Partial<StoredSession>): StoredSession {
  const current = getStoredSession();
  return writeJson(STORAGE_KEYS.session, {
    ...current,
    ...partial,
  });
}

export function clearStoredSession(): void {
  deleteJson(STORAGE_KEYS.session);
}

export async function logoutLocallyAndTryApi(): Promise<void> {
  try {
    const response = await postAccountLogout(createRequestOptions());

    if (response.status >= 400 && response.status < 500) {
      throw toApiError(
        response as { status: number; data?: { code?: string; message?: string } | void },
        "Logout failed.",
      );
    }
  } catch (error) {
    if (!isOfflineLikeError(error)) {
      throw error;
    }
  } finally {
    clearStoredSession();
  }
}

export function requireValue<T>(value: T | null | undefined, message: string): T {
  if (value === undefined || value === null) {
    throw new Error(message);
  }

  return value;
}

export function isSuccessfulStatus(status: number, expected: number[]): boolean {
  return expected.includes(status);
}

export async function readThroughCache<TCached>(config: {
  apiCall: () => Promise<ApiResponse>;
  expectedStatuses: number[];
  mapApiData: (data: unknown) => TCached | Promise<TCached>;
  readCache: () => TCached | null;
  writeCache?: (value: TCached) => void;
  errorMessage: string;
}): Promise<TCached> {
  try {
    const response = await config.apiCall();

    if (isSuccessfulStatus(response.status, config.expectedStatuses)) {
      const mapped = await config.mapApiData(response.data);
      config.writeCache?.(mapped);
      return mapped;
    }

    if (response.status >= 500) {
      const cached = config.readCache();
      if (cached !== null) {
        return cached;
      }
    }

    throw toApiError(
      response as { status: number; data?: { code?: string; message?: string } | void },
      config.errorMessage,
    );
  } catch (error) {
    if (isOfflineLikeError(error)) {
      const cached = config.readCache();

      if (cached !== null) {
        return cached;
      }
    }

    throw error;
  }
}

export async function mutateWithOfflineFallback<TResult>(config: {
  apiCall: () => Promise<ApiResponse>;
  expectedStatuses: number[];
  mapApiData: (data: unknown) => TResult;
  applyOffline: () => TResult;
  onApiSuccess?: (value: TResult) => void;
  errorMessage: string;
}): Promise<TResult> {
  try {
    const response = await config.apiCall();

    if (isSuccessfulStatus(response.status, config.expectedStatuses)) {
      const mapped = config.mapApiData(response.data);
      config.onApiSuccess?.(mapped);
      return mapped;
    }

    if (response.status >= 500) {
      return config.applyOffline();
    }

    throw toApiError(
      response as { status: number; data?: { code?: string; message?: string } | void },
      config.errorMessage,
    );
  } catch (error) {
    if (isOfflineLikeError(error)) {
      return config.applyOffline();
    }

    throw error;
  }
}
