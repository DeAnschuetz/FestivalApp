import { AppBar, Box, Toolbar } from '@mui/material';
import LocalShippingIcon from '@mui/icons-material/LocalShipping';
import HomeIcon from '@mui/icons-material/Home';
import PersonIcon from '@mui/icons-material/Person';
import { useNavigate } from 'react-router-dom';

const Navigation = () => {
  const navigate = useNavigate();
  return (
    <Box sx={{ position: 'fixed', bottom: 0, left: 0, right: 0, zIndex: 1100, px: { xs: 1, sm: 2 }, pb: { xs: 0.5, sm: 1 } }}>
      <AppBar
        position="static"
        elevation={0}
        sx={{
          bgcolor:'#464646',
          color: '#FAF7F5',
          borderRadius: { xs: 2.5, sm: 3 },
        }}
      >
        <Toolbar sx={{ display: 'flex', justifyContent: 'space-around', minHeight: { xs: '42px !important', sm: '48px !important' }, py: 0.5 }}>
         
            <LocalShippingIcon sx={{ fontSize: { xs: 20, sm: 22 }, cursor: 'pointer' }} onClick={() => navigate('/tracking')} />
            <HomeIcon sx={{ fontSize: { xs: 20, sm: 22 }, cursor: 'pointer' }} onClick={() => navigate('/home')} />
            <PersonIcon sx={{ fontSize: { xs: 20, sm: 22 }, cursor: 'pointer' }} onClick={() => navigate('/profile')} />
        
        </Toolbar>
      </AppBar>
    </Box>
  );
};

export default Navigation;
