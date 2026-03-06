import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LogIn from './LogIn/LogIn';
import User from './User/User';
import HomePage from './FoodCourt/HomePage';
import StandPage from './FoodCourt/StandPage';
import HomePageAdmin from './Admin/HomePageAdmin';
import '@fortawesome/fontawesome-free/css/all.min.css';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LogIn mode="login" />} />
        <Route path="/register" element={<LogIn mode="register" />} />
        <Route path="/user_view" element={<User />} />
        <Route path="/food_court" element={<HomePage />} />
        <Route path="/admin" element={<HomePageAdmin />} />
        <Route path="/food_court/stand" element={<StandPage />} />
      </Routes>
    </Router>
  );
}

export default App;
