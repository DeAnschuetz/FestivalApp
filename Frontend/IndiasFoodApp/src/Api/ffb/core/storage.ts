type JsonValue = unknown;

const memoryStorage = new Map<string, string>();

function hasBrowserStorage(): boolean {
  return typeof window !== "undefined" && typeof window.localStorage !== "undefined";
}

function getRaw(key: string): string | null {
  if (hasBrowserStorage()) {
    return window.localStorage.getItem(key);
  }

  return memoryStorage.get(key) ?? null;
}

function setRaw(key: string, value: string): void {
  if (hasBrowserStorage()) {
    window.localStorage.setItem(key, value);
    return;
  }

  memoryStorage.set(key, value);
}

function removeRaw(key: string): void {
  if (hasBrowserStorage()) {
    window.localStorage.removeItem(key);
    return;
  }

  memoryStorage.delete(key);
}

export function readJson<T>(key: string, fallback: T): T {
  const raw = getRaw(key);

  if (!raw) {
    return fallback;
  }

  try {
    return JSON.parse(raw) as T;
  } catch {
    return fallback;
  }
}

export function writeJson<T extends JsonValue>(key: string, value: T): T {
  setRaw(key, JSON.stringify(value));
  return value;
}

export function deleteJson(key: string): void {
  removeRaw(key);
}

export function updateJson<T>(key: string, fallback: T, updater: (current: T) => T): T {
  const current = readJson(key, fallback);
  const next = updater(current);
  writeJson(key, next);
  return next;
}
