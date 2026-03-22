
import { postAccountLogin, postAccountRegister } from "../generated/ffbAPI";
import type {
  AccountType,
  LoginRequest,
  RegisterRequest,
} from "../generated/ffbAPI.schemas";
import {
  clearStoredSession,
  createRequestOptions,
  getStoredSession,
  logoutLocallyAndTryApi,
  mutateWithOfflineFallback,
  saveStoredSession,
} from "./core/api";
import { applyImportedAccount, findImportedAccount } from "./initialImport";
import { inferAccountTypeFromLoginNr, normalizeLoginResult, normalizeRegisterResult } from "./normalizers";
import type { LoginResult, RegisterResult } from "./types";

type StoredOfflineAccount = {
  loginNr: string;
  devPassword: string;
  type: AccountType;
};

const OFFLINE_ACCOUNTS_KEY = "ffb.offlineAccounts.v1";

function getOfflineAccounts(): StoredOfflineAccount[] {
  try {
    const raw =
      typeof window !== "undefined" ? window.localStorage.getItem(OFFLINE_ACCOUNTS_KEY) : null;

    return raw ? (JSON.parse(raw) as StoredOfflineAccount[]) : [];
  } catch {
    return [];
  }
}

function setOfflineAccounts(accounts: StoredOfflineAccount[]): void {
  try {
    if (typeof window !== "undefined") {
      window.localStorage.setItem(OFFLINE_ACCOUNTS_KEY, JSON.stringify(accounts));
    }
  } catch {
    // ignore local storage write failures in offline demo mode
  }
}

function rememberOfflineAccount(account: StoredOfflineAccount): void {
  const nextAccounts = getOfflineAccounts().filter((item) => item.loginNr !== account.loginNr);
  nextAccounts.push(account);
  setOfflineAccounts(nextAccounts);
}

function resolveAccountType(loginNr: string, hintedType?: AccountType): AccountType {
  if (hintedType) {
    return hintedType;
  }

  const importedType = findImportedAccount(loginNr)?.type;
  if (importedType) {
    return importedType;
  }

  return inferAccountTypeFromLoginNr(loginNr);
}

export function getSession() {
  return getStoredSession();
}

export function getAuthToken(): string | undefined {
  return getStoredSession().token;
}

export function setStoredAccountType(accountType: AccountType): void {
  saveStoredSession({ accountType });
}

export function clearSession(): void {
  clearStoredSession();
}

export async function apiLogin(loginRequest: LoginRequest): Promise<LoginResult> {
  return mutateWithOfflineFallback<LoginResult>({
    apiCall: () => postAccountLogin(loginRequest, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeLoginResult(data as Parameters<typeof normalizeLoginResult>[0]),
    onApiSuccess: (data) => {
      const accountType = resolveAccountType(data.loginNr, getStoredSession().accountType);

      saveStoredSession({
        loginNr: data.loginNr,
        token: data.token,
        accountType,
      });

      if (loginRequest.loginNr && loginRequest.password) {
        rememberOfflineAccount({
          loginNr: loginRequest.loginNr,
          devPassword: loginRequest.password,
          type: accountType,
        });
      }
    },
    applyOffline: () => {
      const offlineAccount = getOfflineAccounts().find(
        (item) =>
          item.loginNr === loginRequest.loginNr && item.devPassword === loginRequest.password,
      );

      if (!offlineAccount) {
        throw new Error("Offline login failed. Invalid login number or password.");
      }

      applyImportedAccount(offlineAccount.loginNr);

      const offlineResult: LoginResult = {
        loginNr: offlineAccount.loginNr,
        token: `offline-token-${offlineAccount.loginNr}`,
      };

      saveStoredSession({
        loginNr: offlineResult.loginNr,
        token: offlineResult.token,
        accountType: offlineAccount.type,
      });

      return offlineResult;
    },
    errorMessage: "Login failed.",
  });
}

export async function register(registerRequest: RegisterRequest): Promise<RegisterResult> {
  return mutateWithOfflineFallback<RegisterResult>({
    apiCall: () => postAccountRegister(registerRequest, createRequestOptions()),
    expectedStatuses: [201],
    mapApiData: (data) =>
      normalizeRegisterResult(data as Parameters<typeof normalizeRegisterResult>[0]),
    onApiSuccess: (data) => {
      rememberOfflineAccount({
        loginNr: data.loginNr,
        devPassword: registerRequest.password ?? "",
        type: data.type,
      });

      saveStoredSession({
        accountType: data.type,
      });
    },
    applyOffline: () => {
      if (!registerRequest.loginNr || !registerRequest.password) {
        throw new Error("Offline registration requires loginNr and password.");
      }

      const created: RegisterResult = {
        id: crypto.randomUUID(),
        loginNr: registerRequest.loginNr,
        type: inferAccountTypeFromLoginNr(registerRequest.loginNr),
      };

      rememberOfflineAccount({
        loginNr: created.loginNr,
        devPassword: registerRequest.password,
        type: created.type,
      });

      return created;
    },
    errorMessage: "Registration failed.",
  });
}

export async function logout(): Promise<void> {
  await logoutLocallyAndTryApi();
}
