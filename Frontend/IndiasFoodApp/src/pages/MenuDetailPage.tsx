import { Box } from '@mui/material';
import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation';
import { OrderButton } from '../components/Buttons';
import burgerMenuImg from '../assets/burgerMenu.png';
import pizzaMenuImg from '../assets/pizza.png';
import drinkMenuImg from '../assets/beer.png';
import pommesMenuImg from '../assets/pommes.png';
import DetailViewCard from '../components/Menu/DetailViewCard';

const cards = [
  { img: burgerMenuImg, title: 'Burger' },
  { img: pizzaMenuImg, title: 'Pizza' },
  { img: drinkMenuImg, title: 'Drink' },
  { img: pommesMenuImg, title: 'Pommes' },
  { img: burgerMenuImg, title: 'Burger' },
];

const MenuDetailPage = () => (
  <Box sx={{bgcolor: '#FAF7F5', pb: { xs: 14, sm: 16 }, pt: { xs: 9, sm: 10, md: 12 }}}>
    <Header />
    <Box sx={{ px: { xs: 1.5, sm: 2, md: 3 }, display: 'flex', flexDirection: 'column' }}>
      <DetailViewCard MenuDetailViewImage={cards[1].img} MenuTitle={`${cards[1].title} Margaritha`} imgWidth={80} />
      <DetailViewCard MenuDetailViewImage={cards[1].img} MenuTitle={`${cards[1].title} Salami`} imgWidth={80} />
      <DetailViewCard MenuDetailViewImage={cards[1].img} MenuTitle={`${cards[1].title} Funghi`} imgWidth={80} />
      <DetailViewCard MenuDetailViewImage={cards[1].img} MenuTitle={`${cards[1].title} Funghi`} imgWidth={80} />
    </Box>

    <OrderButton cardTitle='Add to card' />
    <Navigation />
  </Box>
);

export default MenuDetailPage;
