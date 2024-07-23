import React from 'react';
import { Navigate } from 'react-router-dom';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Login from './pages/Login';
import Register from './pages/Register';
import Home from './pages/Home';

const App: React.FC = () => {
  const isAuthenticated = !!localStorage.getItem('token');

  return (
    <Router>
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/home" element={isAuthenticated ? <Home /> : <Navigate to="/login" />} />
        <Route path="/" element={<Navigate to="/home" />} />
      </Routes>
    </Router>
  );
};

export default App;
