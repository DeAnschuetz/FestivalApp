// ...existing code...
import { Box, Button, Typography } from '@mui/material';
import Navigation from '../components/Navigation/Navigation';
import Header from '../components/Header/Header';
import DetailViewCard from '../components/Menu/DetailViewCard';
import { useCart } from '../context/CartContext';
import { useNavigate } from 'react-router-dom';

const ConfirmOrderPage = () => {
  const navigate = useNavigate();
  const { getCartItemsWithCount, getTotal, checkoutOrder } = useCart();
  const cartItems = getCartItemsWithCount();
  const subtotal = getTotal();
  const vipFee = cartItems.length > 0 ? 5.25 : 0;
  const total = Math.max(0, subtotal - vipFee);

  const handleCheckout = () => {
    checkoutOrder();
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

      {cartItems.length > 0 && (
        <Box sx={{ px: { xs: 2, sm: 3 }, pt: 2 }}>
          <Typography sx={{ color: '#9b9b9b', fontSize: 14, mb: 1 }}>Bestellübersicht</Typography>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.8 }}>
            <Typography sx={{ color: '#666', fontSize: 14 }}>Subtotal</Typography>
            <Typography sx={{ color: '#666', fontSize: 14 }}>${subtotal.toFixed(2)}</Typography>
          </Box>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 0.8 }}>
            <Typography sx={{ color: '#666', fontSize: 14 }}>VIP Fee</Typography>
            <Typography sx={{ color: '#666', fontSize: 14 }}>-${vipFee.toFixed(2)}</Typography>
          </Box>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', pt: 0.4 }}>
            <Typography sx={{ color: '#2e2e2e', fontSize: 15, fontWeight: 'bold' }}>Total</Typography>
            <Typography sx={{ color: '#2e2e2e', fontSize: 15, fontWeight: 'bold' }}>${total.toFixed(2)}</Typography>
          </Box>
        </Box>
      )}

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