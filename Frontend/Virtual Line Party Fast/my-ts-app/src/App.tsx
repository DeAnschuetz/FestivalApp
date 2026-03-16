import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider } from "./Auth/AuthContext";
import ProtectedRoute from "./Auth/ProtectedRoute";
import User from "./User/User";
import FoodCourt from "./User/FoodCourt";
import Admin from "./Admin/Admin";
import LogIn from "./LogIn/LogIn";

function App() {
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
              <ProtectedRoute role="ADMIN">
                <Admin />
              </ProtectedRoute>
            }
          />

          <Route
            path="/user_view"
            element={
              <ProtectedRoute role="GUEST">
                <User />
              </ProtectedRoute>
            }
          />

          <Route
            path="/foodcourt_view"
            element={
              <ProtectedRoute role="FOOD_COURT_WORKER">
                <FoodCourt />
              </ProtectedRoute>
            }
          />

        </Routes>
      </Router>
    </AuthProvider>
  );
}

export default App;