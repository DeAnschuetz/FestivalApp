import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LogIn from './LogIn/LogIn';
import User from './User/User';
import HomePage from './FoodCourt/HomePage';
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
      </Routes>
    </Router>
  );
}

export default App;
