// ...existing code...
import { Box } from '@mui/material';
import Navigation from '../components/Navigation/Navigation';
import Header from '../components/Header';

const ProfilePage = () => {
  return (
    <Box sx={{ pb: { xs: 7, sm: 8 }, pt: { xs: 9, sm: 10, md: 12 } }}>
        <Header />
        <h1>Profile Page</h1>
        <Navigation />
    </Box>
    );
};

export default ProfilePage;