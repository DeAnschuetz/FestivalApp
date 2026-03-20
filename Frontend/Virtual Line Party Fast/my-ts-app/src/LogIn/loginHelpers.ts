import { getSession } from "../Api/ffb";
import { AccountType } from "../Api/generated/ffbAPI.schemas";

export function getAccountTypeForRouting(loginNr: string,): AccountType {
  const storedType = getSession()?.accountType;

  if (storedType) {
    return storedType;
  }

  if (loginNr.startsWith("F-")) {
    return "FOOD_COURT_WORKER";
  }

  return "GUEST";
}