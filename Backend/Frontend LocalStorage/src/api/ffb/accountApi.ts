import { postAccountLogin, postAccountRegister } from "../generated/ffbAPI";
import type {
  AccountType,
  LoginRequest,
  PostAccountLogin200,
  PostAccountRegister201,
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

type StoredOfflineAccount = {
  loginNr: string;
  password: string;
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
    // ignore storage write failures in this helper
  }
}

function rememberOfflineAccount(account: StoredOfflineAccount): void {
  const accounts = getOfflineAccounts().filter((item) => item.loginNr !== account.loginNr);
  accounts.push(account);
  setOfflineAccounts(accounts);
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

export async function login(loginRequest: LoginRequest): Promise<PostAccountLogin200> {
  return mutateWithOfflineFallback<PostAccountLogin200>({
    apiCall: () => postAccountLogin(loginRequest, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as PostAccountLogin200,
    onApiSuccess: (data) => {
      saveStoredSession({
        loginNr: data.loginNr,
        token: data.token,
      });

      if (loginRequest.loginNr && loginRequest.password) {
        rememberOfflineAccount({
          loginNr: loginRequest.loginNr,
          password: loginRequest.password,
          type: getStoredSession().accountType ?? "GUEST",
        });
      }
    },
    applyOffline: () => {
      const offlineAccount = getOfflineAccounts().find(
        (item) =>
          item.loginNr === loginRequest.loginNr && item.password === loginRequest.password,
      );

      if (!offlineAccount) {
        throw new Error(
          "Offline login failed. No cached login for this account was found.",
        );
      }

      const offlineResult: PostAccountLogin200 = {
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

export async function register(registerRequest: RegisterRequest): Promise<PostAccountRegister201> {
  return mutateWithOfflineFallback<PostAccountRegister201>({
    apiCall: () => postAccountRegister(registerRequest, createRequestOptions()),
    expectedStatuses: [201],
    mapApiData: (data) => data as PostAccountRegister201,
    onApiSuccess: (data) => {
      if (registerRequest.loginNr && registerRequest.password) {
        rememberOfflineAccount({
          loginNr: registerRequest.loginNr,
          password: registerRequest.password,
          type: data.type ?? "GUEST",
        });
      }
    },
    applyOffline: () => {
      if (!registerRequest.loginNr || !registerRequest.password) {
        throw new Error("Offline registration requires loginNr and password.");
      }

      const created: PostAccountRegister201 = {
        id: crypto.randomUUID(),
        loginNr: registerRequest.loginNr,
        type: "GUEST",
      };

      rememberOfflineAccount({
        loginNr: registerRequest.loginNr,
        password: registerRequest.password,
        type: created.type ?? "GUEST",
      });

      return created;
    },
    errorMessage: "Registration failed.",
  });
}

export async function logout(): Promise<void> {
  await logoutLocallyAndTryApi();
}
