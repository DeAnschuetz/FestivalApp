
import React, { useState } from 'react';

import { Box, Button, FormControl, TextField, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import type { paths } from '../types/api.generated';

import burgerImg from '../assets/burger.png';

type LoginRequestBody = paths['/account/login']['post']['requestBody']['content']['application/json'];
type LoginSuccessResponse = paths['/account/login']['post']['responses'][200]['content']['application/json'];

const LoginPage = () => {
  const navigate = useNavigate();
  const [ticketNumber, setTicketNumber] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

  const handleLoginSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setIsLoading(true);

    try {
      const apiBaseUrl = import.meta.env.DEV
        ? '/api'
        : import.meta.env.VITE_API_BASE_URL || 'http://10.45.129.44:8080';
      const payload: LoginRequestBody = {
        loginNr: ticketNumber,
        password,
      };
      const response = await fetch(`${apiBaseUrl}/account/login`, {
        method: 'POST',
        credentials: 'include',
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
        body: JSON.stringify(payload),
      });

      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(errorText || `Login failed (${response.status})`);
      }

      const data: LoginSuccessResponse = await response.json();
      console.log('Login successful:', data);
      if (data?.token) {
        localStorage.setItem('authToken', data.token);
      }

      navigate('/home');
    } catch (loginError) {
      console.error('Login failed:', loginError);
      if (loginError instanceof TypeError) {
        setError('Backend nicht erreichbar (Netzwerk/CORS). Bitte Dev-Server neu starten und URL prüfen.');
      } else {
        setError('Login fehlgeschlagen. Bitte Zugangsdaten prüfen und erneut versuchen.');
      }
    } finally {
      setIsLoading(false);
    }
  };

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
              value={ticketNumber}
              onChange={(event) => setTicketNumber(event.target.value)}
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
              value={password}
              onChange={(event) => setPassword(event.target.value)}
            />

            <Button
              type="submit"
              fullWidth
              disabled={isLoading}
              sx={{
                mt: 2,
                bgcolor: '#2e2e2e',
                color: '#fff',
                textTransform: 'none',
                fontWeight: 'bold',
                borderRadius: '12px',
              }}
            >
              {isLoading ? 'Anmeldung läuft...' : 'Anmelden'}
            </Button>

            {error && (
              <Typography
                variant="body2"
                sx={{
                  textAlign: 'center',
                  mt: 2,
                  color: '#c62828',
                }}
              >
                {error}
              </Typography>
            )}

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
