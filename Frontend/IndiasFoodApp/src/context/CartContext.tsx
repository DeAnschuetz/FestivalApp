import React, { createContext, useContext, useState } from 'react';

export interface CartItem {
  id: string;
  title: string;
  img: string;
  price: number;
  count: number;
}

interface CartContextType {
  cartItems: CartItem[];
  latestOrderItems: CartItem[];
  latestOrderId: number | null;
  addToCart: (item: Omit<CartItem, 'count'>) => void;
  updateCount: (id: string, count: number) => void;
  getCount: (id: string) => number;
  getTotal: () => number;
  getCartItemsWithCount: () => CartItem[];
  checkoutOrder: () => void;
  clearCart: () => void;
}

const CartContext = createContext<CartContextType>({
  cartItems: [],
  latestOrderItems: [],
  latestOrderId: null,
  addToCart: () => {},
  updateCount: () => {},
  getCount: () => 0,
  getTotal: () => 0,
  getCartItemsWithCount: () => [],
  checkoutOrder: () => {},
  clearCart: () => {},
});

export const useCart = () => useContext(CartContext);

export const CartProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [cartItems, setCartItems] = useState<CartItem[]>([]);
  const [latestOrderItems, setLatestOrderItems] = useState<CartItem[]>([]);
  const [latestOrderId, setLatestOrderId] = useState<number | null>(null);

  const addToCart = (item: Omit<CartItem, 'count'>) => {
    setCartItems(prev => {
      const existing = prev.find(i => i.id === item.id);
      if (existing) {
        return prev; // already in cart
      }
      return [...prev, { ...item, count: 0 }];
    });
  };

  const updateCount = (id: string, count: number) => {
    setCartItems(prev => {
      const existing = prev.find(i => i.id === id);
      if (existing) {
        return prev.map(i => i.id === id ? { ...i, count: Math.max(0, count) } : i);
      }
      return prev;
    });
  };

  const getCount = (id: string) => {
    return cartItems.find(i => i.id === id)?.count || 0;
  };

  const getTotal = () => {
    return cartItems.reduce((sum, item) => sum + item.price * item.count, 0);
  };

  const getCartItemsWithCount = () => {
    return cartItems.filter(item => item.count > 0);
  };

  const clearCart = () => {
    setCartItems([]);
  };

  const checkoutOrder = () => {
    const orderedItems = cartItems.filter(item => item.count > 0);
    if (orderedItems.length === 0) {
      return;
    }

    setLatestOrderItems(orderedItems);
    setLatestOrderId(Math.floor(Math.random() * 90) + 10);
    setCartItems([]);
  };

  return (
    <CartContext.Provider
      value={{
        cartItems,
        latestOrderItems,
        latestOrderId,
        addToCart,
        updateCount,
        getCount,
        getTotal,
        getCartItemsWithCount,
        checkoutOrder,
        clearCart,
      }}
    >
      {children}
    </CartContext.Provider>
  );
};
