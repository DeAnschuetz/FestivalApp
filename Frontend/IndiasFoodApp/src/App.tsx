import React, { useState } from 'react';
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage.tsx';
import HomePage from './pages/HomePage.tsx';
import MenuDetailPage from './pages/MenuDetailPage.tsx';
import ProfilePage from './pages/ProfilePage.tsx';
import OrdersPage from './pages/OrdersPage.tsx';
import ConfirmOderPage from './pages/ConfirmOderPage.tsx';
import SuccessPage from './pages/SuccessPage.tsx';
import TrackingPage from './pages/TrackingPage.tsx';
import { CartProvider } from './context/CartContext.tsx';

export type GlobalContext = {
  signedIn: boolean;
  setSignedIn: (c: boolean) => void;
};

export const Context = React.createContext<GlobalContext>({
  signedIn: false,
  setSignedIn: () => {},
});

function App() {
  const [signedIn, setSignedIn] = useState(false);

  return (
    <Context.Provider value={{ signedIn, setSignedIn }}>
      <CartProvider>
        <BrowserRouter>
          <Routes>
            <Route path="/" element={<Navigate to="/login" />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/home" element={<HomePage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/orders" element={<OrdersPage />} />
            <Route path="/tracking" element={<TrackingPage />} />
            <Route path="/confirm-order" element={<ConfirmOderPage />} />
            <Route path="/success" element={<SuccessPage />} />
            <Route path="/menu/:menuTitle" element={<MenuDetailPage />} />
          </Routes>
        </BrowserRouter>
      </CartProvider>
    </Context.Provider>
  );
}

export default App;
