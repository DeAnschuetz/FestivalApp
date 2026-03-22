import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./Auth/AuthContext";
import ProtectedRoute from "./Auth/ProtectedRoute";
import User from "./User/User";
import Admin from "./Admin/Admin";
import LogIn from "./LogIn/LogIn";
import { useEffect } from "react";
import { bootstrapOfflineDemo } from "./Api/ffb/initialImportBootstrap";
import HomePage from "./FoodCourt/HomePage";
import { AccountType } from "./Api/generated/ffbAPI.schemas";
import StandPage from "./FoodCourt/StandPage";

function App() {

  useEffect(() => {
    bootstrapOfflineDemo();
  }, []);
  
  return (
    <AuthProvider>
      <Router>
        <Routes>

          <Route path="/" element={<Navigate to="/login" />} />
          <Route path="/login" element={<LogIn mode="login" />} />
          <Route path="/register" element={<LogIn mode="register" />} />

          <Route
            path="/admin_view"
            element={
              <ProtectedRoute role={AccountType.ADMIN}>
                <Admin />
              </ProtectedRoute>
            }
          />

          <Route
            path="/user_view"
            element={
              <ProtectedRoute role={AccountType.GUEST}>
                <User />
              </ProtectedRoute>
            }
          />

          <Route
            path="/food_court_view"
            element={
              <ProtectedRoute role={AccountType.FOOD_COURT_WORKER}>
                <HomePage />
              </ProtectedRoute>
            }
          />
          <Route
            path="/food_court_view/stand"
            element={
            <ProtectedRoute role={AccountType.FOOD_COURT_WORKER}>
              <StandPage />
            </ProtectedRoute>
            }
          />
        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;