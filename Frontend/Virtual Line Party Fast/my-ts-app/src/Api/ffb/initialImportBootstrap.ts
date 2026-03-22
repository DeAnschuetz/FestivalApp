import { applyImportedAccount, getImportedInitialData, importInitialData } from "./initialImport";
import initialImport from "./seed/initialImport.json";

export function bootstrapOfflineDemo(defaultLoginNr = "F-000-000-001"): void {
  if (!getImportedInitialData()) {
    importInitialData(initialImport);
  }
  applyImportedAccount("V-000-000-001")
  applyImportedAccount(defaultLoginNr);
}
