// ...existing code...
import { Box, Typography } from '@mui/material';
import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation';
import DetailViewCard from '../components/Menu/DetailViewCard';
import { useCart } from '../context/CartContext';
import { OrderButton } from '../components/Buttons';
import { useNavigate } from 'react-router-dom';

const OrdersPage = () => {
  const { getCartItemsWithCount, getTotal } = useCart();
  const navigate = useNavigate();
  const cartItems = getCartItemsWithCount();
  const total = getTotal();

  return (
    <Box sx={{ pb: { xs: 14, sm: 16 }, pt: { xs: 9, sm: 10, md: 12 } }}>
      <Header />
      <Box sx={{ px: { xs: 1.5, sm: 2, md: 3 }, pt: 1 }}>
        {cartItems.length === 0 ? (
          <Typography sx={{ textAlign: 'center', mt: 4, color: '#888', fontSize: { xs: 14, sm: 16 } }}>
            Noch keine Artikel im Warenkorb.
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
            />
          ))
        )}
      </Box>
      {cartItems.length > 0 && (
        <OrderButton 
          cardTitle={`Zur Bestellübersicht`}
          onClick={() => navigate('/confirm-order')}
        />
      )}
      <Navigation />
    </Box>
  );
}

export default OrdersPage;