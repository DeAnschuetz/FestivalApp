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
  ProductResponse,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";
import { getOwnFoodCourtId } from "./foodCourtApi";

type StoredProductAssignment = {
  id: string;
  mainProductId: Uuid;
  subProductId: Uuid;
};

type ProductFoodCourtMap = Record<string, Uuid>;

function getStoredProducts(): ProductResponse[] {
  return readJson<ProductResponse[]>(STORAGE_KEYS.products, []);
}

function setStoredProducts(value: ProductResponse[]): ProductResponse[] {
  return writeJson(STORAGE_KEYS.products, value);
}

function mergeStoredProducts(value: ProductResponse[]): ProductResponse[] {
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

function assignProductsToFoodCourt(products: ProductResponse[], foodCourtId: Uuid): void {
  const nextMap = {
    ...getStoredProductFoodCourtMap(),
  };

  for (const product of products) {
    if (product.id) {
      nextMap[product.id] = foodCourtId;
    }
  }

  setStoredProductFoodCourtMap(nextMap);
}

function upsertProduct(product: ProductResponse): ProductResponse[] {
  const next = getStoredProducts().filter((item) => item.id !== product.id);
  next.push(product);
  return setStoredProducts(next);
}

function findProduct(productId: Uuid): ProductResponse | undefined {
  return getStoredProducts().find((item) => item.id === productId);
}

function filterProductsByFoodCourt(products: ProductResponse[], foodCourtId: Uuid): ProductResponse[] {
  const productFoodCourtMap = getStoredProductFoodCourtMap();
  return products.filter((product) => product.id && productFoodCourtMap[product.id] === foodCourtId);
}

function applyAssignments(products: ProductResponse[]): ProductResponse[] {
  const assignments = getStoredAssignments();
  const productIndex = new Map(getStoredProducts().map((product) => [product.id, product]));

  return products.map((product) => ({
    ...product,
    subProducts: assignments
      .filter((assignment) => assignment.mainProductId === product.id)
      .map((assignment) => productIndex.get(assignment.subProductId))
      .filter((item): item is ProductResponse => Boolean(item)),
  }));
}

export async function getAllProducts(): Promise<ProductResponse[]> {
  return readThroughCache<ProductResponse[]>({
    apiCall: () => getProductListAll(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => applyAssignments(data as ProductResponse[]),
    readCache: () => {
      const products = getStoredProducts();
      return products.length > 0 ? applyAssignments(products) : null;
    },
    writeCache: (products) => {
      setStoredProducts(products);
    },
    errorMessage: "Products could not be loaded.",
  });
}

export async function getOwnFoodCourtProducts(): Promise<ProductResponse[]> {
  return readThroughCache<ProductResponse[]>({
    apiCall: () => getProductList(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => applyAssignments(data as ProductResponse[]),
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

export async function getProductsByFoodCourtId(foodCourtId: Uuid): Promise<ProductResponse[]> {
  return readThroughCache<ProductResponse[]>({
    apiCall: () => getProductListByFoodCourtIdFoodCourtId(foodCourtId, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => applyAssignments(data as ProductResponse[]),
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

export async function getProductById(id: Uuid): Promise<ProductResponse> {
  return readThroughCache<ProductResponse>({
    apiCall: () => getProductId(id, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as ProductResponse,
    readCache: () => findProduct(id) ?? null,
    writeCache: upsertProduct,
    errorMessage: "Product could not be loaded.",
  });
}

export async function createProduct(
  request: ProductRequestSimple,
): Promise<ProductResponse> {
  return mutateWithOfflineFallback<ProductResponse>({
    apiCall: () => postProduct(request, createRequestOptions()),
    expectedStatuses: [201],
    mapApiData: (data) => data as ProductResponse,
    onApiSuccess: (product) => {
      upsertProduct(product);

      const ownFoodCourtId = getOwnFoodCourtId();
      if (product.id && ownFoodCourtId) {
        assignProductsToFoodCourt([product], ownFoodCourtId);
      }
    },
    applyOffline: () => {
      const created: ProductResponse = {
        id: crypto.randomUUID(),
        displayName: request.displayName ?? "Offline product",
        price: request.price ?? 0,
        symbolIdentifier: request.symbolIdentifier,
        minimalWarning: request.minimalWarning,
        productCount: 0,
        subProducts: [],
      };

      upsertProduct(created);

      const ownFoodCourtId = getOwnFoodCourtId();
      if (created.id && ownFoodCourtId) {
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
): Promise<ProductResponse> {
  return mutateWithOfflineFallback<ProductResponse>({
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
    mapApiData: (data) => Boolean(data),
    onApiSuccess: () => {
      if (request.mainProductId && request.subProductId) {
        const assignment: StoredProductAssignment = {
          id: crypto.randomUUID(),
          mainProductId: request.mainProductId,
          subProductId: request.subProductId,
        };

        setStoredAssignments([...getStoredAssignments(), assignment]);
      }
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
