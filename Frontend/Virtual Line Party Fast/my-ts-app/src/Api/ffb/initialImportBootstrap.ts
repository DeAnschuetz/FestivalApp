import { applyImportedAccount, getImportedInitialData, importInitialData } from "./initialImport";
import initialImport from "./seed/initialImport.json";

export function bootstrapOfflineDemo(): void {
  if (!getImportedInitialData()) {
    importInitialData(initialImport);
  }
  applyImportedAccount("V-000-000-001")
  applyImportedAccount("F-000-000-001");
}
