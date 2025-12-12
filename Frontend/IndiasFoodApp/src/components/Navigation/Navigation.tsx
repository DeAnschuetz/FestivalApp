import React, { use } from 'react';
import { AppBar, Box, Toolbar, IconButton } from '@mui/material';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import HomeIcon from '@mui/icons-material/Home';
import PersonIcon from '@mui/icons-material/Person';
import { useNavigate } from 'react-router-dom';

const Navigation = () => {
  const navigate = useNavigate();
  return (
    <Box sx={{ flexGrow: 1, position: 'fixed', bottom: 0, left: 0, right: 0, zIndex: 1100 }}>
      <AppBar
        position="static"
        elevation={0}
        sx={{
          bgcolor:'#464646',
          color: '#FAF7F5',
          borderRadius: 15,     
        }}
      >
        <Toolbar sx={{ display: 'flex', justifyContent: 'space-between', mx: 8, py: 2}}>
         
            <LocalShippingIcon fontSize="large" />
            <HomeIcon fontSize="large" onClick={() => navigate('/home')} />
            <PersonIcon fontSize="large" />
        
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default Navigation;
