import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Auth.css';

const Login: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await axios.post('http://167.172.107.65:54653/api/login', { email, password });
      localStorage.setItem('token', response.data.token);
      localStorage.setItem('userId', response.data.userId);
      navigate('/home');
    } catch (error) {
      console.error('Error logging in', error);
    }
  };

  return (
    <div className="background">
      <div className="auth-container">
        <div className="auth-box">
          <h2>Login</h2>
          <form onSubmit={handleLogin}>
            <input
              type="email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="Email"
              required
            />
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Password"
              required
            />
            <button type="submit">Login</button>
          </form>
          <button onClick={() => navigate('/register')} className="register-button">Register</button>
        </div>
      </div>
    </div>
  );
};

export default Login;
