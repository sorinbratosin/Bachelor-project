import { styled } from '@mui/system';
import { Box, Typography, Button } from '@mui/material';

export const Root = styled('div')({
  display: 'flex',
  justifyContent: 'center',
  alignItems: 'center',
  height: '100vh',
});

export const Sidebar = styled('div')({
  width: '200px',
  marginRight: '2rem',
  display: 'flex',
  flexDirection: 'column',
});

export const Main = styled('div')({
  display: 'flex',
  flexDirection: 'column',
  alignItems: 'center',
  backgroundColor: '#fff',
  padding: '2rem',
  borderRadius: '8px',
  boxShadow: '0 0 10px rgba(0, 0, 0, 0.1)',
  width: '80%', // Increased width to fit the content
  maxWidth: '1200px', // Increased max-width for better fit
});

export const PaperBox = styled(Box)({
  padding: '1.5rem',
  textAlign: 'center',
  color: '#333',
  marginBottom: '1rem',
  border: '1px solid #ccc',
  borderRadius: '8px',
  backgroundColor: '#f9f9f9',
  width: '100%', // Increased width to fit the content
  maxWidth: '800px', // Increased max-width for better fit
});

export const Header = styled(Typography)({
  marginBottom: '1rem',
});

export const Value = styled(Typography)({
  fontWeight: 'bold',
});

export const Link = styled(Button)({
  margin: '1rem 0',
  display: 'block',
});
