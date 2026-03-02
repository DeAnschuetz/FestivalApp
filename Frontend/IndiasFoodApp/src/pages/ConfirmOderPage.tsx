// ...existing code...
import { Box, Button, Typography } from '@mui/material';
import Navigation from '../components/Navigation/Navigation';
import Header from '../components/Header/Header';
import DetailViewCard from '../components/Menu/DetailViewCard';

import drinkMenuImg from '../assets/beer.png';
import burgerMenuImg from '../assets/burgerMenu.png';
import { useNavigate } from 'react-router-dom';

const cartItems = [
  { img: burgerMenuImg, title: 'Pizza Margaritha', price: '$8.09' },
  { img: drinkMenuImg, title: 'Blonde Roast', price: '$8.09' },
];

const ConfirmOrderPage = () => {
  const navigate = useNavigate();
  const total = '$16.39';

  return (
    <Box sx={{ pb: { xs: 10, sm: 12 }, pt: { xs: 9, sm: 10, md: 12 } }}>
      <Header />
      <Box sx={{ px: { xs: 1.5, sm: 2, md: 3 }, pt: 1 }}>
        {cartItems.map((item, idx) => (
          <DetailViewCard
            key={idx}
            MenuDetailViewImage={item.img}
            MenuTitle={item.title}
            price={item.price}
            imgWidth={70}
          />
        ))}
      </Box>

      {/* Checkout Button */}
      <Box sx={{ position: 'fixed', bottom: 64, left: 0, right: 0, px: 2, zIndex: 1050 }}>
        <Button
          variant="contained"
          fullWidth
          onClick={() => navigate('/success')}
          sx={{
            bgcolor: '#78C4A4',
            borderRadius: '25px',
            py: 1.5,
            textTransform: 'none',
            fontSize: 16,
            fontWeight: 'bold',
            display: 'flex',
            justifyContent: 'space-between',
            px: 3,
            '&:hover': { bgcolor: '#5fb090' },
          }}
        >
          <span>Checkout</span>
          <Typography component="span" sx={{ fontWeight: 'bold' }}>{total}</Typography>
        </Button>
      </Box>

      <Navigation />
    </Box>
  );
};

export default ConfirmOrderPage;