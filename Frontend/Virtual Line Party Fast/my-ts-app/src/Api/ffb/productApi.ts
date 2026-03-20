
import {
  deleteProductByIdId,
  deleteProductsAssignments,
  deleteProductsAssignmentsByIdId,
  getProductId,
  getProductList,
  getProductListAll,
  getProductListByFoodCourtIdFoodCourtId,
  postProduct,
  postProductsAssignments,
  putProductUpdateCountProductIdNewCount,
} from "../generated/ffbAPI";
import type {
  ProductLinkRequest,
  ProductRequestSimple,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";
import { getOwnFoodCourtId } from "./foodCourtApi";
import { normalizeProduct, normalizeProducts } from "./normalizers";
import type { Product } from "./types";

type StoredProductAssignment = {
  id: string;
  mainProductId: Uuid;
  subProductId: Uuid;
};

type ProductFoodCourtMap = Record<string, Uuid>;

function getStoredProducts(): Product[] {
  return readJson<Product[]>(STORAGE_KEYS.products, []);
}

function setStoredProducts(value: Product[]): Product[] {
  return writeJson(STORAGE_KEYS.products, value);
}

function mergeStoredProducts(value: Product[]): Product[] {
  const merged = [...getStoredProducts()];

  for (const nextProduct of value) {
    const index = merged.findIndex((item) => item.id === nextProduct.id);

    if (index >= 0) {
      merged[index] = nextProduct;
    } else {
      merged.push(nextProduct);
    }
  }

  return setStoredProducts(merged);
}

function getStoredAssignments(): StoredProductAssignment[] {
  return readJson<StoredProductAssignment[]>(STORAGE_KEYS.productAssignments, []);
}

function setStoredAssignments(value: StoredProductAssignment[]): StoredProductAssignment[] {
  return writeJson(STORAGE_KEYS.productAssignments, value);
}

function getStoredProductFoodCourtMap(): ProductFoodCourtMap {
  return readJson<ProductFoodCourtMap>(STORAGE_KEYS.productFoodCourtMap, {});
}

function setStoredProductFoodCourtMap(value: ProductFoodCourtMap): ProductFoodCourtMap {
  return writeJson(STORAGE_KEYS.productFoodCourtMap, value);
}

function assignProductsToFoodCourt(products: Product[], foodCourtId: Uuid): void {
  const nextMap = {
    ...getStoredProductFoodCourtMap(),
  };

  for (const product of products) {
    nextMap[product.id] = foodCourtId;
  }

  setStoredProductFoodCourtMap(nextMap);
}

function upsertProduct(product: Product): Product[] {
  const next = getStoredProducts().filter((item) => item.id !== product.id);
  next.push(product);
  return setStoredProducts(next);
}

function findProduct(productId: Uuid): Product | undefined {
  return getStoredProducts().find((item) => item.id === productId);
}

function filterProductsByFoodCourt(products: Product[], foodCourtId: Uuid): Product[] {
  const productFoodCourtMap = getStoredProductFoodCourtMap();
  return products.filter((product) => productFoodCourtMap[product.id] === foodCourtId);
}

function applyAssignments(products: Product[]): Product[] {
  const assignments = getStoredAssignments();
  const productIndex = new Map(getStoredProducts().map((product) => [product.id, product]));

  return products.map((product) => ({
    ...product,
    subProducts: assignments
      .filter((assignment) => assignment.mainProductId === product.id)
      .map((assignment) => productIndex.get(assignment.subProductId))
      .filter((item): item is Product => Boolean(item)),
  }));
}

export async function getAllProducts(): Promise<Product[]> {
  return readThroughCache<Product[]>({
    apiCall: () => getProductListAll(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) =>
      applyAssignments(normalizeProducts(data as Parameters<typeof normalizeProducts>[0])),
    readCache: () => {
      const products = getStoredProducts();
      return products.length > 0 ? applyAssignments(products) : null;
    },
    writeCache: setStoredProducts,
    errorMessage: "Products could not be loaded.",
  });
}

export async function getOwnFoodCourtProducts(): Promise<Product[]> {
  return readThroughCache<Product[]>({
    apiCall: () => getProductList(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) =>
      applyAssignments(normalizeProducts(data as Parameters<typeof normalizeProducts>[0])),
    readCache: () => {
      const ownFoodCourtId = getOwnFoodCourtId();

      if (!ownFoodCourtId) {
        return null;
      }

      const products = filterProductsByFoodCourt(getStoredProducts(), ownFoodCourtId);
      return products.length > 0 ? applyAssignments(products) : null;
    },
    writeCache: (products) => {
      const ownFoodCourtId = getOwnFoodCourtId();

      if (ownFoodCourtId) {
        assignProductsToFoodCourt(products, ownFoodCourtId);
      }

      mergeStoredProducts(products);
    },
    errorMessage: "Own food court products could not be loaded.",
  });
}

export async function getProductsByFoodCourtId(foodCourtId: Uuid): Promise<Product[]> {
  return readThroughCache<Product[]>({
    apiCall: () => getProductListByFoodCourtIdFoodCourtId(foodCourtId, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) =>
      applyAssignments(normalizeProducts(data as Parameters<typeof normalizeProducts>[0])),
    readCache: () => {
      const products = filterProductsByFoodCourt(getStoredProducts(), foodCourtId);
      return products.length > 0 ? applyAssignments(products) : null;
    },
    writeCache: (products) => {
      assignProductsToFoodCourt(products, foodCourtId);
      mergeStoredProducts(products);
    },
    errorMessage: "Products could not be loaded.",
  });
}

export async function getProductById(id: Uuid): Promise<Product> {
  return readThroughCache<Product>({
    apiCall: () => getProductId(id, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeProduct(data as Parameters<typeof normalizeProduct>[0]),
    readCache: () => findProduct(id) ?? null,
    writeCache: upsertProduct,
    errorMessage: "Product could not be loaded.",
  });
}

export async function createProduct(
  request: ProductRequestSimple,
): Promise<Product> {
  return mutateWithOfflineFallback<Product>({
    apiCall: () => postProduct(request, createRequestOptions()),
    expectedStatuses: [201],
    mapApiData: (data) => normalizeProduct(data as Parameters<typeof normalizeProduct>[0]),
    onApiSuccess: (product) => {
      upsertProduct(product);

      const ownFoodCourtId = getOwnFoodCourtId();
      if (ownFoodCourtId) {
        assignProductsToFoodCourt([product], ownFoodCourtId);
      }
    },
    applyOffline: () => {
      const created: Product = {
        id: crypto.randomUUID(),
        displayName: request.displayName ?? "Offline product",
        price: request.price ?? 0,
        symbolIdentifier: request.symbolIdentifier ?? "offline-product",
        minimalWarning: request.minimalWarning ?? 0,
        productCount: 0,
        subProducts: [],
      };

      upsertProduct(created);

      const ownFoodCourtId = getOwnFoodCourtId();
      if (ownFoodCourtId) {
        assignProductsToFoodCourt([created], ownFoodCourtId);
      }

      return created;
    },
    errorMessage: "Product could not be created.",
  });
}

export async function updateProductCount(
  productId: Uuid,
  newCount: number,
): Promise<Product> {
  return mutateWithOfflineFallback<Product>({
    apiCall: () =>
      putProductUpdateCountProductIdNewCount(
        productId,
        newCount,
        createRequestOptions(),
      ),
    expectedStatuses: [200],
    mapApiData: () => {
      const existing = findProduct(productId);

      if (!existing) {
        throw new Error("Updated product was not in cache.");
      }

      const updated = { ...existing, productCount: newCount };
      upsertProduct(updated);
      return updated;
    },
    applyOffline: () => {
      const existing = findProduct(productId);

      if (!existing) {
        throw new Error("Offline update failed because the product is not cached.");
      }

      const updated = { ...existing, productCount: newCount };
      upsertProduct(updated);
      return updated;
    },
    errorMessage: "Product count could not be updated.",
  });
}

export async function deleteProduct(productId: Uuid): Promise<void> {
  await mutateWithOfflineFallback<void>({
    apiCall: () => deleteProductByIdId(productId, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: () => undefined,
    onApiSuccess: () => {
      setStoredProducts(getStoredProducts().filter((item) => item.id !== productId));
      setStoredAssignments(
        getStoredAssignments().filter(
          (assignment) =>
            assignment.mainProductId !== productId && assignment.subProductId !== productId,
        ),
      );

      const currentOwners = { ...getStoredProductFoodCourtMap() };
      delete currentOwners[productId];
      setStoredProductFoodCourtMap(currentOwners);
    },
    applyOffline: () => {
      setStoredProducts(getStoredProducts().filter((item) => item.id !== productId));
      setStoredAssignments(
        getStoredAssignments().filter(
          (assignment) =>
            assignment.mainProductId !== productId && assignment.subProductId !== productId,
        ),
      );

      const currentOwners = { ...getStoredProductFoodCourtMap() };
      delete currentOwners[productId];
      setStoredProductFoodCourtMap(currentOwners);
    },
    errorMessage: "Product could not be deleted.",
  });
}

export async function createProductAssignment(
  request: ProductLinkRequest,
): Promise<boolean> {
  return mutateWithOfflineFallback<boolean>({
    apiCall: () => postProductsAssignments(request, createRequestOptions()),
    expectedStatuses: [201],
    mapApiData: () => true,
    onApiSuccess: () => {
      if (!request.mainProductId || !request.subProductId) {
        return;
      }

      const assignment: StoredProductAssignment = {
        id: crypto.randomUUID(),
        mainProductId: request.mainProductId,
        subProductId: request.subProductId,
      };

      setStoredAssignments([...getStoredAssignments(), assignment]);
    },
    applyOffline: () => {
      if (!request.mainProductId || !request.subProductId) {
        throw new Error("Offline assignment needs both product ids.");
      }

      const assignment: StoredProductAssignment = {
        id: crypto.randomUUID(),
        mainProductId: request.mainProductId,
        subProductId: request.subProductId,
      };

      setStoredAssignments([...getStoredAssignments(), assignment]);
      return true;
    },
    errorMessage: "Product assignment could not be created.",
  });
}

export async function deleteProductAssignment(
  request: ProductLinkRequest,
): Promise<void> {
  await mutateWithOfflineFallback<void>({
    apiCall: () => deleteProductsAssignments(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: () => undefined,
    onApiSuccess: () => {
      setStoredAssignments(
        getStoredAssignments().filter(
          (assignment) =>
            assignment.mainProductId !== request.mainProductId ||
            assignment.subProductId !== request.subProductId,
        ),
      );
    },
    applyOffline: () => {
      setStoredAssignments(
        getStoredAssignments().filter(
          (assignment) =>
            assignment.mainProductId !== request.mainProductId ||
            assignment.subProductId !== request.subProductId,
        ),
      );
    },
    errorMessage: "Product assignment could not be deleted.",
  });
}

export async function deleteProductAssignmentById(assignmentId: Uuid): Promise<void> {
  await mutateWithOfflineFallback<void>({
    apiCall: () => deleteProductsAssignmentsByIdId(assignmentId, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: () => undefined,
    onApiSuccess: () => {
      setStoredAssignments(
        getStoredAssignments().filter((assignment) => assignment.id !== assignmentId),
      );
    },
    applyOffline: () => {
      setStoredAssignments(
        getStoredAssignments().filter((assignment) => assignment.id !== assignmentId),
      );
    },
    errorMessage: "Product assignment could not be deleted.",
  });
}

export function getCurrentWorkerFoodCourtId(): Uuid | undefined {
  return getOwnFoodCourtId();
}
