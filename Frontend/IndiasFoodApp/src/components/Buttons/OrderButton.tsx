// ...existing code...
import { Button } from '@mui/material';

interface OrderButtonProps {
    cardTitle: string;
}

const OrderButton = ({ cardTitle }: OrderButtonProps) => {
  return (
        <Button 
            variant="contained"
            sx={{
                position: 'sticky',
                bottom: 100,
                zIndex: 1000,
                p: 1.5,
                width: '80%',
                bgcolor: '#78C4A4',
                borderRadius: '25px',
            }}
        >
        {cardTitle}
    </Button>
    );
};

export  { OrderButton };