// ...existing code...
import { Button } from '@mui/material';

interface OrderButtonProps {
    cardTitle: string;
    onClick?: () => void;
}

const OrderButton = ({ cardTitle, onClick }: OrderButtonProps) => {
  return (
        <Button 
            variant="contained"
            onClick={onClick}
            sx={{
                position: 'fixed',
                bottom: { xs: 55, sm: 65 },
                left: '50%',
                transform: 'translateX(-50%)',
                zIndex: 1050,
                p: { xs: 1, sm: 1.5 },
                width: { xs: 'calc(100% - 24px)', sm: 'calc(100% - 32px)', md: 'calc(100% - 48px)' },
                maxWidth: 500,
                bgcolor: '#78C4A4',
                borderRadius: '25px',
                fontSize: { xs: 13, sm: 15 },
                textTransform: 'none',
                boxShadow: '0 -2px 10px rgba(0,0,0,0.1)',
                '&:hover': { bgcolor: '#5fb090' },
            }}
        >
        {cardTitle}
    </Button>
    );
};

export  { OrderButton };