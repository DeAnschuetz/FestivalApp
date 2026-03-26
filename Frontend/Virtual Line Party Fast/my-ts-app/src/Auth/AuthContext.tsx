import React, { createContext, useContext, useState } from "react";
import { AccountType } from "../Api/generated/ffbAPI.schemas";

type AuthContextType = {
  role: string | null;
  login: (role: AccountType) => void;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | null>(null);

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) throw new Error("useAuth must be used inside AuthProvider");
  return context;
};

export const AuthProvider = ({ children }: { children: React.ReactNode }) => {
  const [role, setRole] = useState<string | null>(
    localStorage.getItem("role")
  );

  const login = (role: AccountType) => {
    localStorage.setItem("role", role);
    setRole(role);
  };

  const logout = () => {
    localStorage.removeItem("role");
    setRole(null);
  };

  return (
    <AuthContext.Provider value={{ role, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};