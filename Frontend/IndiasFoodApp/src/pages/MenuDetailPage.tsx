import React from 'react';
import { Box } from '@mui/material';
import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation'; 
import OverViewCard from '../components/Menu/OverViewCard';
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
  <Box sx={{bgcolor: '#FAF7F5'}}>
    <Header />
    <Box sx={{pt:12, display: 'flex', flexWrap: 'wrap', justifyContent: 'center' }}>
      <DetailViewCard MenuDetailViewImage={cards[1].img} MenuTitle={`${cards[1].title} Margaritha`}/*  cardHeight={'10%'} cardWidth={130} */ imgWidth={130} />
      <DetailViewCard MenuDetailViewImage={cards[1].img} MenuTitle={`${cards[1].title} Salami`} /* cardHeight={140} cardWidth={130} */ imgWidth={130} />
      <DetailViewCard MenuDetailViewImage={cards[1].img} MenuTitle={`${cards[1].title} Funghi`} /* cardHeight={140} cardWidth={130} */ imgWidth={130} />
      <DetailViewCard MenuDetailViewImage={cards[1].img} MenuTitle={`${cards[1].title} Funghi`} /* cardHeight={140} cardWidth={130} */ imgWidth={130} />

        <OrderButton cardTitle='Add to card' />
      
    </Box>
    <Navigation />
  </Box>
);

export default MenuDetailPage;
