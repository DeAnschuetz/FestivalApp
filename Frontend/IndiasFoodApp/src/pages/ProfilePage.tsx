import React from 'react';
import { Box } from '@mui/material';
import Navigation from '../components/Navigation/Navigation';
import Header from '../components/Header';

const ProfilePage = () => {
  return (
    <Box>
        <Header />
        <h1>Profile Page</h1>
        <Navigation />
    </Box>
    );
};

export default ProfilePage;