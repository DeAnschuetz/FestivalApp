import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import LogIn from './LogIn/LogIn';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Navigate to="/login" />} />
        <Route path="/login" element={<LogIn mode="login" />} />
        <Route path="/register" element={<LogIn mode="register" />} />
      </Routes>
    </Router>
  );
}

export default App;
