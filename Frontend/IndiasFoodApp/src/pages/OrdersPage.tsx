// ...existing code...
import { Box } from '@mui/material';
import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation';

const OrdersPage = () => {
  return (
    <Box>
        <Header />
        <h1>Orders Page</h1>
        <Navigation />
    </Box>
    );
}

export default OrdersPage;