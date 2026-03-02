// ...existing code...

// MUI Components
import { AppBar, Box, Typography } from '@mui/material';
import ShoppingBasketIcon from '@mui/icons-material/ShoppingBasket';
import { useNavigate } from 'react-router-dom';

const Header = () => {
  const navigate = useNavigate();
  return (
    <AppBar
      position="fixed"
      elevation={0}
      sx={{
        bgcolor: '#FAF7F5',
        color: '#000',
      }}
    >
      {/* Guthaben Bar */}
      <Box sx={{ px: { xs: 1.5, sm: 2, md: 3 }, pt: 1, pb: 1 }}>
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            bgcolor: '#D5E8CD',
            borderRadius: 1,
            px: { xs: 1, sm: 1.5 },
            py: { xs: 0.5, sm: 0.75 },
          }}
        >
          <Typography variant="subtitle2" fontWeight="bold" sx={{ fontSize: { xs: 12, sm: 13, md: 14 } }}>
            Guthaben: 100$
          </Typography>
          <ShoppingBasketIcon 
            sx={{ fontSize: { xs: 18, sm: 20 }, cursor: 'pointer' }} 
            onClick={() => navigate('/confirm-order')}
          />
        </Box>
      </Box>
    </AppBar>
  );
};

export default Header;
