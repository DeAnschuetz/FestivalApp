import { AccountType } from "../Api/generated/ffbAPI.schemas";

export function getAccountTypeForRouting(loginNr: string,): AccountType {
  console.log(loginNr);

  if (loginNr.startsWith("F")) {
    console.log("WORKER")
    return AccountType.FOOD_COURT_WORKER;
  }

  return AccountType.GUEST;
}