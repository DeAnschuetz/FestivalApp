// ...existing code...
import { Box } from '@mui/material';
import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation';
import DetailViewCard from '../components/Menu/DetailViewCard';
import pizzaMenuImg from '../assets/pizza.png';

const orderItems = [
  { img: pizzaMenuImg, title: 'Pizza Margaritha', price: '$8.09' },
  { img: pizzaMenuImg, title: 'Pizza Margaritha', price: '$8.09' },
  { img: pizzaMenuImg, title: 'Pizza Margaritha', price: '$8.09' },
];

const OrdersPage = () => {
  return (
    <Box sx={{ pb: { xs: 7, sm: 8 }, pt: { xs: 9, sm: 10, md: 12 } }}>
      <Header />
      <Box sx={{ px: { xs: 1.5, sm: 2, md: 3 }, pt: 1 }}>
        {orderItems.map((item, idx) => (
          <DetailViewCard
            key={idx}
            MenuDetailViewImage={item.img}
            MenuTitle={item.title}
            price={item.price}
            imgWidth={70}
          />
        ))}
      </Box>
      <Navigation />
    </Box>
  );
}

export default OrdersPage;