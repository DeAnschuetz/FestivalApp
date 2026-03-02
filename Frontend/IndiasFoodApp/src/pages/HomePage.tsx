import { Box } from '@mui/material';
import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation'; 
import OverViewCard from '../components/Menu/OverViewCard';
import burgerMenuImg from '../assets/burgerMenu.png';
import pizzaMenuImg from '../assets/pizza.png';
import drinkMenuImg from '../assets/beer.png';
import pommesMenuImg from '../assets/pommes.png';
import { useNavigate } from 'react-router-dom';

const foodCards = [
  { img: pizzaMenuImg, title: 'Pizza' },
  { img: burgerMenuImg, title: 'Burger' },
  { img: drinkMenuImg, title: 'Vegan' },
  { img: pommesMenuImg, title: 'Pommes' },
];

const drinkCards = [
  { img: drinkMenuImg, title: 'Getränke' },
];

const HomePage = () => {

  const navigate = useNavigate();
  return (
    <Box sx={{ pb: { xs: 7, sm: 8 }, pt: { xs: 9, sm: 10, md: 12 } }}>
      <Header />

      {/* Food Cards Grid - 2 Spalten */}
      <Box sx={{ px: { xs: 1.5, sm: 2, md: 3 }, pt: 1 }}>
        <Box
          sx={{
            display: 'grid',
            gridTemplateColumns: '1fr 1fr',
            gap: { xs: 1, sm: 1.5, md: 2 },
            mb: { xs: 1, sm: 1.5 },
          }}
        >
          {foodCards.map((card, idx) => (
            <Box key={card.title + idx}>
              <OverViewCard 
                MenuOverViewImage={card.img} 
                MenuTitle={card.title} 
                navigateToMenuDetail={() => navigate(`/menu/${card.title.toLowerCase()}`)}
                cardWidth="100%"
                imgWidth={80}
              />
            </Box>
          ))}
        </Box>

        {/* Drink Cards - Zentriert, halbe Breite */}
        <Box sx={{ display: 'flex', justifyContent: 'center' }}>
          {drinkCards.map((card, idx) => (
            <Box key={card.title + idx} sx={{ width: '48%' }}>
              <OverViewCard 
                MenuOverViewImage={card.img} 
                MenuTitle={card.title} 
                navigateToMenuDetail={() => navigate(`/menu/${card.title.toLowerCase()}`)}
                cardWidth="100%"
                imgWidth={60}
                cardHeight={120}
              />
            </Box>
          ))}
        </Box>
      </Box>

      <Navigation />
    </Box>
  );
};

export default HomePage;
