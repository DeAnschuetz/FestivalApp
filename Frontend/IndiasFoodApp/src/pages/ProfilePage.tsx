import { Box, Button, Typography } from '@mui/material';
import Navigation from '../components/Navigation/Navigation';
import Header from '../components/Header/Header';
import burgerImg from '../assets/burger.png';
import { useNavigate } from 'react-router-dom';

const ProfilePage = () => {
  const navigate = useNavigate();
  return (
    <Box
      sx={{
        minHeight: '100vh',
        bgcolor: '#F0ECE9',
        pb: { xs: 10, sm: 11 },
        pt: { xs: 9, sm: 10, md: 12 },
      }}
    >
      <Header />

      <Box sx={{ px: { xs: 2, sm: 3 }, pt: { xs: 2, sm: 3 } }}>
        <Box
          component="img"
          src={burgerImg}
          alt="Burger"
          sx={{
            width: { xs: 190, sm: 220 },
            maxWidth: '80%',
            height: 'auto',
            display: 'block',
            mx: 'auto',
            mb: { xs: 2.5, sm: 3 },
          }}
        />

        <Box
          sx={{
            bgcolor: '#E7E3E0',
            borderRadius: 3,
            px: 2,
            py: 2,
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'space-between',
            mb: 2,
          }}
        >
          <Box>
            <Typography sx={{ fontSize: 16, fontWeight: 500, lineHeight: 1.1 }}>Guthaben</Typography>
            <Typography sx={{ fontSize: 16, color: '#3A3A3A', lineHeight: 1.1 }}>$100.08</Typography>
          </Box>

          <Button
            sx={{
              bgcolor: '#484C59',
              color: '#fff',
              borderRadius: '18px',
              textTransform: 'none',
              fontWeight: 700,
              px: 2,
              py: 0.75,
              minWidth: 0,
              '&:hover': {
                bgcolor: '#484C59',
              },
            }}
          >
            Aufladen
          </Button>
        </Box>

        <Button
          fullWidth
          sx={{
            bgcolor: '#E7E3E0',
            color: '#111',
            borderRadius: 3,
            textTransform: 'none',
            fontSize: 16,
            fontWeight: 500,
            py: 1.25,
            '&:hover': {
              bgcolor: '#E7E3E0',
            },
          }}
          onClick={() => navigate('/login')}
        >
          Logout
        </Button>
      </Box>

      <Navigation />
    </Box>
  );
};

export default ProfilePage;