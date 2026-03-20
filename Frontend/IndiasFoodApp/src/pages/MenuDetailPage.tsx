import { Box } from '@mui/material';
import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation';
import { OrderButton } from '../components/Buttons';
import pizzaMenuImg from '../assets/pizza.png';
import DetailViewCard from '../components/Menu/DetailViewCard';
import { useCart } from '../context/CartContext';
import { useNavigate, useParams } from 'react-router-dom';

const menuItems: Record<string, { id: string; img: string; title: string; price: number }[]> = {
  pizza: [
    { id: 'pizza-margaritha', img: pizzaMenuImg, title: 'Pizza Margaritha', price: 8.09 },
    { id: 'pizza-salami', img: pizzaMenuImg, title: 'Pizza Salami', price: 9.09 },
    { id: 'pizza-funghi', img: pizzaMenuImg, title: 'Pizza Funghi', price: 8.59 },
  ],
  burger: [
    { id: 'burger-classic', img: pizzaMenuImg, title: 'Classic Burger', price: 7.99 },
    { id: 'burger-cheese', img: pizzaMenuImg, title: 'Cheese Burger', price: 8.99 },
  ],
  vegan: [
    { id: 'vegan-bowl', img: pizzaMenuImg, title: 'Vegan Bowl', price: 9.49 },
    { id: 'vegan-wrap', img: pizzaMenuImg, title: 'Vegan Wrap', price: 7.99 },
  ],
  pommes: [
    { id: 'pommes-classic', img: pizzaMenuImg, title: 'Pommes Classic', price: 4.09 },
    { id: 'pommes-loaded', img: pizzaMenuImg, title: 'Loaded Fries', price: 6.09 },
  ],
  getränke: [
    { id: 'drink-cola', img: pizzaMenuImg, title: 'Cola', price: 3.09 },
    { id: 'drink-beer', img: pizzaMenuImg, title: 'Blonde Roast', price: 5.09 },
  ],
};

const MenuDetailPage = () => {
  const { menuTitle } = useParams();
  const navigate = useNavigate();
  const { getCartItemsWithCount } = useCart();
  const items = menuItems[menuTitle || ''] || menuItems.pizza;

  const handleAddToCart = () => {
    const itemsInCart = getCartItemsWithCount();
    if (itemsInCart.length > 0) {
      navigate('/orders');
    }
  };

  return (
    <Box sx={{bgcolor: '#FAF7F5', pb: { xs: 14, sm: 16 }, pt: { xs: 9, sm: 10, md: 12 }}}>
      <Header />
      <Box sx={{ px: { xs: 1.5, sm: 2, md: 3 }, display: 'flex', flexDirection: 'column' }}>
        {items.map(item => (
          <DetailViewCard
            key={item.id}
            id={item.id}
            MenuDetailViewImage={item.img}
            MenuTitle={item.title}
            price={`$${item.price.toFixed(2)}`}
            priceNumber={item.price}
            imgWidth={80}
          />
        ))}
      </Box>

      <OrderButton cardTitle='Add to cart' onClick={handleAddToCart} />
      <Navigation />
    </Box>
  );
};

export default MenuDetailPage;
