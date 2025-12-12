import React from 'react';
import { Box } from '@mui/material';
import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation'; 
import OverViewCard from '../components/Menu/OverViewCard';
import burgerMenuImg from '../assets/burgerMenu.png';
import pizzaMenuImg from '../assets/pizza.png';
import drinkMenuImg from '../assets/beer.png';
import pommesMenuImg from '../assets/pommes.png';
import { useNavigate } from 'react-router-dom';

const cards = [
  { img: burgerMenuImg, title: 'Burger' },
  { img: pizzaMenuImg, title: 'Pizza' },
  { img: drinkMenuImg, title: 'Drink' },
  { img: pommesMenuImg, title: 'Pommes' },
  { img: burgerMenuImg, title: 'Burger' },
];

const HomePage = () => {

  const navigate = useNavigate();
  return (
    <Box>
      <Header />
      <Box>
        <Box
          sx={{
            display: 'grid',
            gridTemplateColumns: '1fr 1fr',
            gap: 1,
            justifyItems: 'center',
          }}
        >
          {cards.map((card, idx) => {
            const isLast = idx === cards.length - 1;
            const isOdd = cards.length % 2 === 1;
            const cardProps = isLast && isOdd
              ? { cardWidth: '93%', imgWidth: 80 }
              : { cardWidth: 160, imgWidth: 80 };
            return (
              <Box key={card.title + idx} sx={isLast && isOdd ? { gridColumn: 'span 2', width: '100%', display: 'flex', justifyContent: 'start' } : {}}>
                <OverViewCard MenuOverViewImage={card.img} MenuTitle={card.title} navigateToMenuDetail={() => navigate(`/menu/${card.title.toLowerCase()}`)} {...cardProps} />
              </Box>
            );
          })}
        </Box>
      </Box>
      <Navigation />
    </Box>
  );
};

export default HomePage;
