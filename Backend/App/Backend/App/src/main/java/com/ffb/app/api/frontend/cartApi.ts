import {
  deleteCartId,
  getCart as getCartRequest,
  putCartAddCartItem,
  putCartNewPriority,
  putCartUpdate,
} from "../generated/ffbAPI";
import type {
  CartItemCreationRequest,
  CartItemResponse,
  CartItemUpdateRequest,
  GetCart200,
  ProductResponse,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";

function getStoredCart(): GetCart200 | null {
  return readJson<GetCart200 | null>(STORAGE_KEYS.cart, null);
}

function setStoredCart(cart: GetCart200): GetCart200 {
  return writeJson(STORAGE_KEYS.cart, cart);
}

function getStoredProducts(): ProductResponse[] {
  return readJson<ProductResponse[]>(STORAGE_KEYS.products, []);
}

function findProduct(productId: Uuid | undefined): ProductResponse | undefined {
  return getStoredProducts().find((product) => product.id === productId);
}

function calculateTotal(cartItems: CartItemResponse[] | undefined): number {
  return (cartItems ?? []).reduce((sum, item) => {
    const ownPrice = (item.price ?? 0) * (item.count ?? 0);
    const subItemPrice = calculateTotal(item.subItems);
    return sum + ownPrice + subItemPrice;
  }, 0);
}

function ensureCart(): GetCart200 {
  return getStoredCart() ?? { hasPrio: false, total: 0, cartItems: [] };
}

export async function getCart(): Promise<GetCart200> {
  return readThroughCache<GetCart200>({
    apiCall: () => getCartRequest(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as GetCart200,
    readCache: () => getStoredCart(),
    writeCache: setStoredCart,
    errorMessage: "Cart could not be loaded.",
  });
}

export async function addCartItem(
  request: CartItemCreationRequest,
): Promise<GetCart200> {
  return mutateWithOfflineFallback<GetCart200>({
    apiCall: () => putCartAddCartItem(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as GetCart200,
    onApiSuccess: setStoredCart,
    applyOffline: () => {
      const cart = ensureCart();
      const product = findProduct(request.productId);

      const newItem: CartItemResponse = {
        id: crypto.randomUUID(),
        displayName: product?.displayName ?? "Offline product",
        symbolIdentifier: product?.symbolIdentifier,
        price: product?.price ?? 0,
        count: request.itemCount ?? 1,
        extra: request.extra,
        subItems: product?.subProducts?.map((subProduct) => ({
          id: subProduct.id,
          displayName: subProduct.displayName,
          symbolIdentifier: subProduct.symbolIdentifier,
          price: subProduct.price,
          count: 1,
          extra: undefined,
          subItems: [],
        })) ?? [],
      };

      const updatedItems = [...(cart.cartItems ?? []), newItem];
      const updatedCart: GetCart200 = {
        ...cart,
        cartItems: updatedItems,
        total: calculateTotal(updatedItems),
      };

      return setStoredCart(updatedCart);
    },
    errorMessage: "Cart item could not be added.",
  });
}

export async function updateCartItem(
  request: CartItemUpdateRequest,
): Promise<GetCart200> {
  return mutateWithOfflineFallback<GetCart200>({
    apiCall: () => putCartUpdate(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as GetCart200,
    onApiSuccess: setStoredCart,
    applyOffline: () => {
      const cart = ensureCart();

      const updatedItems = (cart.cartItems ?? []).map((item) =>
        item.id === request.cartItemId
          ? {
              ...item,
              count: request.itemCount ?? item.count,
              extra: request.extra ?? item.extra,
            }
          : item,
      );

      return setStoredCart({
        ...cart,
        cartItems: updatedItems,
        total: calculateTotal(updatedItems),
      });
    },
    errorMessage: "Cart item could not be updated.",
  });
}

export async function removeCartItem(id: Uuid): Promise<GetCart200> {
  return mutateWithOfflineFallback<GetCart200>({
    apiCall: () => deleteCartId(id, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as GetCart200,
    onApiSuccess: setStoredCart,
    applyOffline: () => {
      const cart = ensureCart();
      const updatedItems = (cart.cartItems ?? []).filter((item) => item.id !== id);

      return setStoredCart({
        ...cart,
        cartItems: updatedItems,
        total: calculateTotal(updatedItems),
      });
    },
    errorMessage: "Cart item could not be removed.",
  });
}

export async function setCartPriority(newPriority: boolean): Promise<GetCart200> {
  return mutateWithOfflineFallback<GetCart200>({
    apiCall: () => putCartNewPriority(newPriority, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => data as GetCart200,
    onApiSuccess: setStoredCart,
    applyOffline: () => {
      const cart = ensureCart();
      return setStoredCart({
        ...cart,
        hasPrio: newPriority,
      });
    },
    errorMessage: "Cart priority could not be changed.",
  });
}

export function clearStoredCart(): void {
  writeJson(STORAGE_KEYS.cart, {
    hasPrio: false,
    total: 0,
    cartItems: [],
  } satisfies GetCart200);
}
