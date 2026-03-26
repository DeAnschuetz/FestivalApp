import { readJson, writeJson } from "./storage";
import { STORAGE_KEYS } from "./keys";

type FoodCourtImageMap = Record<string, string>;

function getImageMap(): FoodCourtImageMap {
  return readJson<FoodCourtImageMap>(STORAGE_KEYS.foodCourtImages, {});
}

function setImageMap(value: FoodCourtImageMap): FoodCourtImageMap {
  return writeJson(STORAGE_KEYS.foodCourtImages, value);
}

export async function fileToDataUrl(file: Blob): Promise<string> {
  return new Promise<string>((resolve, reject) => {
    const reader = new FileReader();

    reader.onload = () => {
      if (typeof reader.result === "string") {
        resolve(reader.result);
        return;
      }

      reject(new Error("Image could not be converted to a data URL."));
    };

    reader.onerror = () => reject(reader.error ?? new Error("Image could not be read."));
    reader.readAsDataURL(file);
  });
}

export async function responseToDataUrl(response: Response): Promise<string> {
  const blob = await response.blob();
  return fileToDataUrl(blob);
}

export function getStoredFoodCourtImage(foodCourtId: string): string | null {
  const imageMap = getImageMap();
  return imageMap[foodCourtId] ?? null;
}

export function setStoredFoodCourtImage(foodCourtId: string, dataUrl: string): string {
  const imageMap = getImageMap();
  imageMap[foodCourtId] = dataUrl;
  setImageMap(imageMap);
  return dataUrl;
}
