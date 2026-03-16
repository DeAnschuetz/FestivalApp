import { Navigate } from "react-router-dom";
import { useAuth } from "./AuthContext";
import { JSX } from "react";

type Props = {
  children: JSX.Element;
  role: string;
};

function ProtectedRoute({ children, role }: Props) {
  const { role: userRole } = useAuth();

  if (!userRole) {
    return <Navigate to="/login" replace />;
  }

  if (userRole !== role) {
    return <Navigate to="/login" replace />;
  }

  return children;
}

export default ProtectedRoute;