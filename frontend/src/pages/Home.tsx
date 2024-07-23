import React, { useEffect, useState } from 'react';
import axios from 'axios';
import { Container, Typography, Grid, Button } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import { Root, Sidebar, Main, PaperBox, Header, Value } from './HomeStyles';
import './styles.css';

const Home: React.FC = () => {
  const [userData, setUserData] = useState({ lastName: '', dataIrigare: '', umiditate: '', dataAdaugarii: '' });
  const navigate = useNavigate();

  useEffect(() => {
    const userId = localStorage.getItem('userId');
    const token = localStorage.getItem('token');

    if (!userId || !token) {
      navigate('/login');
      return;
    }

    axios.get(`http://167.172.107.65:54653/api/date-home/${userId}`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
      .then(response => {
        setUserData(response.data);
      })
      .catch(error => {
        console.error('Error fetching user data:', error);
        if (error.response && error.response.status === 403) {
          navigate('/login');
        }
      });
  }, [navigate]);

  const handleLogout = () => {
    localStorage.removeItem('message');
    localStorage.removeItem('token');
    localStorage.removeItem('userId');
    navigate('/login');
  };

  const formatDateTime = (dateTime: string) => {
    return new Date(dateTime).toLocaleString('en-GB', { hour12: false }).replace(',', '');
  };

  return (
    <div className="background">
      <div className="overlay"></div>
      <Container className="content">
        <Root>
          <Sidebar>
            <Button variant="contained" color="secondary" fullWidth onClick={handleLogout}>Logout</Button>
          </Sidebar>
          <Main>
            <Header variant="h4">Bine ai venit, {userData.lastName}!</Header>
            <PaperBox>
              <Typography variant="h6">Ultima irigare:</Typography>
              <Value variant="h4">{formatDateTime(userData.dataIrigare)}</Value>
            </PaperBox>
            <PaperBox>
              <Typography variant="h6">Cele mai recente date:</Typography>
              <Grid container spacing={2}>
                <Grid item xs={6}>
                  <Typography variant="h6">Umiditate:</Typography>
                  <Value variant="h4">{userData.umiditate} %</Value>
                </Grid>
                <Grid item xs={6}>
                  <Typography variant="h6">Data adaugarii:</Typography>
                  <Value variant="h4">{formatDateTime(userData.dataAdaugarii)}</Value>
                </Grid>
              </Grid>
            </PaperBox>
          </Main>
        </Root>
      </Container>
    </div>
  );
};

export default Home;
