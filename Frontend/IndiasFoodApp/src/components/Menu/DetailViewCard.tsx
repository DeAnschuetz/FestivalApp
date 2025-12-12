import React from 'react';
import { Box, Typography } from '@mui/material';
interface OverViewCardProps {
    MenuDetailViewImage: string;
    MenuTitle?: string;
    cardWidth?: number | string;
    imgWidth?: number;
    cardHeight?: number | string;
}

const OverViewCard: React.FC<OverViewCardProps> = ({ MenuDetailViewImage, MenuTitle, cardWidth, imgWidth, cardHeight = 140 }) => {
  return (
    <Box
      sx={{
        backgroundColor: '#ffffffff',
        p: 2,
        m:1,
        borderRadius: 4,
        display: 'flex',
        justifyContent: 'space-between',
      }}
    >
      <Box
        sx={{
          width: cardWidth,
          minHeight: cardHeight,
          bgcolor: '#F0ECE9',
          borderRadius: 2,
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          p: 1,
          justifyContent: 'center',
        }}
      >
        <Box
          component="img"
          src={MenuDetailViewImage}
          alt={MenuTitle || ''}
          sx={{ width: imgWidth, height: 'auto', mb: 1 }}
        />
        
      </Box>
      <Box>
        <Typography variant="subtitle1" fontWeight="bold" sx={{ fontSize: { xs: 16, sm: 18 } }}>
          {MenuTitle}
        </Typography>
        <Typography variant="body2" sx={{ mt: 1 }}>
          $8.04        
        </Typography>
        <Box sx={{borderRadius: '20px', border: '2px solid black', height: '40px'}}>

        </Box>
        
      </Box>
    </Box>
  );
};

export default OverViewCard;