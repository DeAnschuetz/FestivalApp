
import { useState } from 'react';

import { Box, Button, FormControl, TextField, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import handleLoginSubmit from '../api-communication/login';

import burgerImg from '../assets/burger.png';

const LoginPage = () => {
  const navigate = useNavigate();
  const [ticketNumber, setTicketNumber] = useState('');
  const [password, setPassword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState('');

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
        <Box component="form" sx={{ px: 3, pb: 3 }} onSubmit={(e) => handleLoginSubmit({ ticketNumber, password, setError, setIsLoading, navigate }, e)}>

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
