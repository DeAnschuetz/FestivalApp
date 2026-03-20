import React from 'react';
import { Box, Typography, Button } from '@mui/material';
import { useCart } from '../../context/CartContext';

interface DetailViewCardProps {
    id: string;
    MenuDetailViewImage: string;
    MenuTitle?: string;
    imgWidth?: number;
    price?: string;
    priceNumber?: number;
    showControls?: boolean;
}

const DetailViewCard: React.FC<DetailViewCardProps> = ({ id, MenuDetailViewImage, MenuTitle, imgWidth = 80, price = '$8.09', priceNumber = 8.09, showControls = true }) => {
  const { getCount, updateCount, addToCart } = useCart();
  const itemCount = getCount(id);

  // Register item in cart on first render
  React.useEffect(() => {
    addToCart({ id, title: MenuTitle || '', img: MenuDetailViewImage, price: priceNumber });
  }, [id]);

  const handleIncrement = () => updateCount(id, itemCount + 1);
  const handleDecrement = () => updateCount(id, Math.max(0, itemCount - 1));
  return (
    <Box
      sx={{
        backgroundColor: '#fff',
        p: { xs: 1, sm: 1.5 },
        mx: 0,
        mb: { xs: 1, sm: 1.5 },
        borderRadius: { xs: 2.5, sm: 3 },
        display: 'flex',
        flexDirection: 'row',
        alignItems: 'center',
        gap: { xs: 1.5, sm: 2 },
      }}
    >
      {/* Image */}
      <Box
        sx={{
          bgcolor: '#F0ECE9',
          borderRadius: 2,
          p: { xs: 0.75, sm: 1 },
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          minWidth: { xs: imgWidth * 0.75 + 12, sm: imgWidth + 16 },
          flexShrink: 0,
        }}
      >
        <Box
          component="img"
          src={MenuDetailViewImage}
          alt={MenuTitle || ''}
          sx={{ width: { xs: imgWidth * 0.75, sm: imgWidth }, height: 'auto' }}
        />
      </Box>

      {/* Content */}
      <Box sx={{ flex: 1, display: 'flex', flexDirection: 'column', gap: 0.5, minWidth: 0 }}>
        <Typography variant="subtitle2" fontWeight="bold" sx={{ fontSize: { xs: 12, sm: 14 }, overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
          {MenuTitle}
        </Typography>
        <Typography variant="body2" fontWeight="bold" sx={{ fontSize: { xs: 14, sm: 16 } }}>
          {price}
        </Typography>
        
        {/* Quantity Controls */}
        <Box sx={{ 
          borderRadius: '20px', 
          border: '1px solid #E3DCD5', 
          display: 'flex', 
          alignItems: 'center', 
          justifyContent: 'space-between',
          mt: 0.5,
          maxWidth: { xs: 100, sm: 120 },
        }}>
          <Button 
            size="small" 
            sx={{ minWidth: { xs: 28, sm: 32 }, color: '#000', fontSize: { xs: 12, sm: 14 }, p: 0.5 }}
            onClick={handleDecrement}
          >
            —
          </Button>
          <Typography sx={{ fontSize: { xs: 12, sm: 14 }, fontWeight: 'bold' }}>{itemCount}</Typography>
          <Button 
            size="small" 
            sx={{ minWidth: { xs: 28, sm: 32 }, color: '#000', fontSize: { xs: 12, sm: 14 }, p: 0.5 }}
            onClick={handleIncrement}
          >
            +
          </Button>
        </Box>
      </Box>
    </Box>
  );
};

export default DetailViewCard;