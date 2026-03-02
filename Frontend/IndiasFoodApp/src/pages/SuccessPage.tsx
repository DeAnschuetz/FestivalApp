import CheckCircleOutlineIcon from '@mui/icons-material/CheckCircleOutline';
import CloseIcon from '@mui/icons-material/Close';
import { Box, Button, IconButton, Typography } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Navigation from '../components/Navigation/Navigation';

const SuccessPage = () => {
  const navigate = useNavigate();

  return (
    <Box sx={{ minHeight: '100vh', bgcolor: '#F7F7F7', pb: { xs: 10, sm: 12 } }}>
      <Box sx={{ px: 2, pt: 2 }}>
        <IconButton
          onClick={() => navigate('/home')}
          sx={{
            bgcolor: '#F2F2F2',
            width: 28,
            height: 28,
            '&:hover': { bgcolor: '#E8E8E8' },
          }}
        >
          <CloseIcon sx={{ fontSize: 16, color: '#666' }} />
        </IconButton>
      </Box>

      <Box
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          textAlign: 'center',
          px: 3,
          pt: { xs: 6, sm: 8 },
        }}
      >
        <Box
          sx={{
            width: 78,
            height: 78,
            borderRadius: '50%',
            bgcolor: '#EFEFEF',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            mb: 3,
          }}
        >
          <CheckCircleOutlineIcon sx={{ fontSize: 38, color: '#4C4C4C' }} />
        </Box>

        <Typography sx={{ fontSize: 24, fontWeight: 500, color: '#1F1F1F', mb: 1 }}>
          Order successfully placed
        </Typography>
        <Typography sx={{ maxWidth: 300, fontSize: 14, color: '#9A9A9A', lineHeight: 1.5, mb: 4 }}>
          Your order has been successfully processed and will soon be ready for you to pick up.
        </Typography>

        <Button
          onClick={() => navigate('/tracking')}
          sx={{
            width: '100%',
            maxWidth: 280,
            borderRadius: '999px',
            py: 1.2,
            textTransform: 'none',
            bgcolor: '#78C4A4',
            color: '#fff',
            fontWeight: 500,
            '&:hover': { bgcolor: '#65B695' },
            mb: 2,
          }}
        >
          Track Delivery
        </Button>

        <Button
          variant="text"
          onClick={() => navigate('/home')}
          sx={{ textTransform: 'none', color: '#78C4A4', fontWeight: 500 }}
        >
          Continue Shopping
        </Button>
      </Box>

      <Navigation />
    </Box>
  );
};

export default SuccessPage;