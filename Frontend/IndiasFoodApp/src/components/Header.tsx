import React from 'react';
import { AppBar, Toolbar, Typography, IconButton } from '@mui/material';

const Header = () => {
  return (
    <AppBar
      position="fixed"
      elevation={0}
      sx={{
        bgcolor: '#d8eacc',
        color: '#000',
      }}
    >
      <Toolbar sx={{ display: 'flex', justifyContent: 'space-between' }}>
        {/* Guthaben-Text */}
        <Typography variant="subtitle1" fontWeight="bold">
          Guthaben: 100$
        </Typography>

        {/* Warenkorb Icon */}
        <IconButton edge="end" color="inherit">
          {/* Icon kann hier eingefügt werden */}
        </IconButton>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
