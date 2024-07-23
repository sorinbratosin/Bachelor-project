import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import './Auth.css'; // Import the CSS file for styling

const Register: React.FC = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const navigate = useNavigate();

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await axios.post('http://localhost:54653/api/register', { firstName,lastName, email, password});
      navigate('/login');
    } catch (error) {
      console.error('Error registering', error);
    }
  };

  return (
    <div className="background">
      <div className="auth-container">
        <div className="auth-box">
          <h2>Register</h2>
          <form onSubmit={handleRegister}>
            <input
              type="text"
              value={firstName}
              onChange={(e) => setFirstName(e.target.value)}
              placeholder="First Name"
              required
            />
            <input
              type="text"
              value={lastName}
              onChange={(e) => setLastName(e.target.value)}
              placeholder="Last Name"
              required
            />
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
            <button type="submit">Register</button>
          </form>
          <button onClick={() => navigate('/login')} className="register-button">Login</button>
        </div>
      </div>
    </div>
  );
};

export default Register;
