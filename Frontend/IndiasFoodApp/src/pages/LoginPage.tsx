
import React, { useState } from 'react';

import { Box, Button, FormControl, TextField, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';

import burgerImg from '../assets/burger.png';

const LoginPage = () => {
  const navigate = useNavigate();
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  const handleLoginSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // look for ticketnumber that starts with 3
    const ticketNumber = (document.getElementById('ticketNumber') as HTMLInputElement | null)?.value || '';
    if (ticketNumber.startsWith('3')) {
      console.log('Ticket number starts with 3');
      setIsLoggedIn(true);
      navigate('/home');
    } else {
      console.log('Ticket number does not start with 3');
      setIsLoggedIn(false);
    }
  }

  return (
    <Box
      sx={{
        minHeight: '100vh',
        width: '100vw',
        display: 'flex',
        justifyContent: 'center',
        alignItems: 'center',
        bgcolor: '#f5f5f5',
      }}
    >
      <Box
        sx={{
          width: '100%',
          maxWidth: 400,
          bgcolor: '#fff',
          borderRadius: 2,
          overflow: 'hidden',
        }}
      >
        {/* Header */}
        <Box sx={{ bgcolor: '#d8eacc', py: 2, textAlign: 'center', position: 'absolute', top: 0, width: '100vw' }}>
          <Typography variant="h6" fontWeight="bold">
            Login
          </Typography>
        </Box>

        {/* Burger Bild  */}
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'center',
            mt: 2,
            mb: 1,
          }}
        >
          <Box
            component="img"
            src={burgerImg}
            alt="Burger"
            sx={{ width: '150px', height: 'auto' }}
          />
        </Box>

        {/* Login-Formular */}
        <Box component="form" sx={{ px: 3, pb: 3 }} onSubmit={handleLoginSubmit}>

          <FormControl fullWidth>
            <TextField
              fullWidth
              required
              id="ticketNumber"
              margin="normal"
              placeholder="Ticketnummer"
              variant="outlined"
              name="ticketNumber"
            />

            <TextField
              fullWidth
              required
              id="password"
              type="password"
              margin="normal"
              placeholder="Passwort"
              variant="outlined"
              name="password"
            />

            <Button
              type="submit"
              fullWidth
			        onSubmit={handleLoginSubmit}
              sx={{
                mt: 2,
                bgcolor: '#2e2e2e',
                color: '#fff',
                textTransform: 'none',
                fontWeight: 'bold',
                borderRadius: '12px',
              }}
            >
              Anmelden
            </Button>

            <Typography
              variant="body2"
              sx={{
                textAlign: 'center',
                mt: 2,
                color: '#444',
                textDecoration: 'underline',
                cursor: 'pointer',
              }}
              onClick={() => console.log('Passwort vergessen')}
            >
              Passwort vergessen?
            </Typography>
          </FormControl>
        </Box>
      </Box>
    </Box>
  );
};

export default LoginPage;
