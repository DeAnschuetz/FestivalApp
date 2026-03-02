// ...existing code...
import { Box, Button, Typography } from '@mui/material';
import Navigation from '../components/Navigation/Navigation';
import Header from '../components/Header/Header';
import DetailViewCard from '../components/Menu/DetailViewCard';
import { useCart } from '../context/CartContext';
import { useNavigate } from 'react-router-dom';

const ConfirmOrderPage = () => {
  const navigate = useNavigate();
  const { getCartItemsWithCount, getTotal, clearCart } = useCart();
  const cartItems = getCartItemsWithCount();
  const total = getTotal();

  const handleCheckout = () => {
    clearCart();
    navigate('/success');
  };

  return (
    <Box sx={{ pb: { xs: 10, sm: 12 }, pt: { xs: 9, sm: 10, md: 12 } }}>
      <Header />
      <Box sx={{ px: { xs: 1.5, sm: 2, md: 3 }, pt: 1 }}>
        {cartItems.length === 0 ? (
          <Typography sx={{ textAlign: 'center', mt: 4, color: '#888' }}>
            Warenkorb ist leer.
          </Typography>
        ) : (
          cartItems.map((item) => (
            <DetailViewCard
              key={item.id}
              id={item.id}
              MenuDetailViewImage={item.img}
              MenuTitle={item.title}
              price={`$${item.price.toFixed(2)}`}
              priceNumber={item.price}
              imgWidth={70}
              showControls={true}
            />
          ))
        )}
      </Box>

      {/* Checkout Button */}
      {cartItems.length > 0 && (
        <Box sx={{ position: 'fixed', bottom: { xs: 50, sm: 64 }, left: 0, right: 0, px: 2, zIndex: 1050 }}>
          <Button
            variant="contained"
            fullWidth
            onClick={handleCheckout}
            sx={{
              bgcolor: '#78C4A4',
              borderRadius: '25px',
              py: { xs: 1, sm: 1.5 },
              textTransform: 'none',
              fontSize: { xs: 14, sm: 16 },
              fontWeight: 'bold',
              display: 'flex',
              justifyContent: 'space-between',
              px: 3,
              '&:hover': { bgcolor: '#5fb090' },
            }}
          >
            <span>Checkout</span>
            <Typography component="span" sx={{ fontWeight: 'bold' }}>${total.toFixed(2)}</Typography>
          </Button>
        </Box>
      )}

      <Navigation />
    </Box>
  );
};

export default ConfirmOrderPage;