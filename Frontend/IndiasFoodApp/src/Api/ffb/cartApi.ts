
import {
  deleteCartId,
  getCart as getCartRequest,
  putCartAddCartItem,
  putCartNewPriority,
  putCartUpdate,
} from "../generated/ffbAPI";
import type {
  CartItemCreationRequest,
  CartItemUpdateRequest,
  Uuid,
} from "../generated/ffbAPI.schemas";
import { createRequestOptions, mutateWithOfflineFallback, readThroughCache } from "./core/api";
import { STORAGE_KEYS } from "./core/keys";
import { readJson, writeJson } from "./core/storage";
import { normalizeCart } from "./normalizers";
import type { Cart, CartItem, Product } from "./types";

function getStoredCart(): Cart | null {
  return readJson<Cart | null>(STORAGE_KEYS.cart, null);
}

function setStoredCart(cart: Cart): Cart {
  return writeJson(STORAGE_KEYS.cart, cart);
}

function getStoredProducts(): Product[] {
  return readJson<Product[]>(STORAGE_KEYS.products, []);
}

function findProduct(productId: Uuid | undefined): Product | undefined {
  return getStoredProducts().find((product) => product.id === productId);
}

function calculateTotal(cartItems: CartItem[]): number {
  return cartItems.reduce((sum, item) => {
    const ownPrice = item.price * item.count;
    const subItemPrice = calculateTotal(item.subItems);
    return sum + ownPrice + subItemPrice;
  }, 0);
}

function ensureCart(): Cart {
  return getStoredCart() ?? { hasPrio: false, total: 0, cartItems: [] };
}

export async function getCart(): Promise<Cart> {
  return readThroughCache<Cart>({
    apiCall: () => getCartRequest(createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeCart(data as Parameters<typeof normalizeCart>[0]),
    readCache: () => getStoredCart(),
    writeCache: setStoredCart,
    errorMessage: "Cart could not be loaded.",
  });
}

export async function addCartItem(
  request: CartItemCreationRequest,
): Promise<Cart> {
  return mutateWithOfflineFallback<Cart>({
    apiCall: () => putCartAddCartItem(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeCart(data as Parameters<typeof normalizeCart>[0]),
    onApiSuccess: setStoredCart,
    applyOffline: () => {
      const cart = ensureCart();
      const product = findProduct(request.productId);

      if (!product) {
        throw new Error("Offline cart update failed because the product is not cached.");
      }

      const newItem: CartItem = {
        id: crypto.randomUUID(),
        displayName: product.displayName,
        symbolIdentifier: product.symbolIdentifier,
        price: product.price,
        count: request.itemCount ?? 1,
        extra: request.extra ?? "",
        subItems: product.subProducts.map((subProduct) => ({
          id: subProduct.id,
          displayName: subProduct.displayName,
          symbolIdentifier: subProduct.symbolIdentifier,
          price: subProduct.price,
          count: 1,
          extra: "",
          subItems: [],
        })),
      };

      const updatedItems = [...cart.cartItems, newItem];
      const updatedCart: Cart = {
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
): Promise<Cart> {
  return mutateWithOfflineFallback<Cart>({
    apiCall: () => putCartUpdate(request, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeCart(data as Parameters<typeof normalizeCart>[0]),
    onApiSuccess: setStoredCart,
    applyOffline: () => {
      const cart = ensureCart();

      const updatedItems = cart.cartItems.map((item) =>
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

export async function removeCartItem(id: Uuid): Promise<Cart> {
  return mutateWithOfflineFallback<Cart>({
    apiCall: () => deleteCartId(id, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeCart(data as Parameters<typeof normalizeCart>[0]),
    onApiSuccess: setStoredCart,
    applyOffline: () => {
      const cart = ensureCart();
      const updatedItems = cart.cartItems.filter((item) => item.id !== id);

      return setStoredCart({
        ...cart,
        cartItems: updatedItems,
        total: calculateTotal(updatedItems),
      });
    },
    errorMessage: "Cart item could not be removed.",
  });
}

export async function setCartPriority(newPriority: boolean): Promise<Cart> {
  return mutateWithOfflineFallback<Cart>({
    apiCall: () => putCartNewPriority(newPriority, createRequestOptions()),
    expectedStatuses: [200],
    mapApiData: (data) => normalizeCart(data as Parameters<typeof normalizeCart>[0]),
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
  } satisfies Cart);
}
