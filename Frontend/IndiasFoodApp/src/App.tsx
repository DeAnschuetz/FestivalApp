import React, { useState } from 'react';
import { BrowserRouter, Route, Routes, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage.tsx';
import HomePage from './pages/HomePage.tsx';
import MenuDetailPage from './pages/MenuDetailPage.tsx';

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
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/home" element={<HomePage />} />
          <Route path="/menu/:menuTitle" element={<MenuDetailPage />} />
        </Routes>
      </BrowserRouter>
    </Context.Provider>
  );
}

export default App;
