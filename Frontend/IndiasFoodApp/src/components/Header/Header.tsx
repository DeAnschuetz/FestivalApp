import React from 'react';

// MUI Components
import { AppBar, Toolbar, Typography } from '@mui/material';
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket';

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
        <Typography variant="subtitle1" fontWeight="bold">
          Guthaben: 100$
        </Typography>
        <ShoppingBasketIcon fontSize="large" />
      </Toolbar>
    </AppBar>
  );
};

export default Header;
