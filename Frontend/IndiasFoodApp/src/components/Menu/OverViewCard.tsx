import React from 'react';
import { Box, Typography } from '@mui/material';
interface OverViewCardProps {
    MenuOverViewImage: string;
    MenuTitle?: string;
    cardWidth?: number | string;
    imgWidth?: number;
    cardHeight?: number | string;
    navigateToMenuDetail?: () => void;
}

const OverViewCard: React.FC<OverViewCardProps> = ({ MenuOverViewImage, MenuTitle, cardWidth, imgWidth = 80, cardHeight = 140, navigateToMenuDetail }) => {
  return (
    <Box
      sx={{
        width: cardWidth,
        minHeight: { xs: cardHeight, sm: 160 },
        bgcolor: '#F5F1EE',
        borderRadius: { xs: 2.5, sm: 3 },
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        p: { xs: 1, sm: 1.5, md: 2 },
        justifyContent: 'center',
        cursor: 'pointer',
      }}
      onClick={navigateToMenuDetail}
    >
      <Box
        component="img"
        src={MenuOverViewImage}
        alt={MenuTitle || ''}
        sx={{
          width: { xs: imgWidth * 0.85, sm: imgWidth, md: imgWidth * 1.2 },
          maxWidth: '80%',
          height: 'auto',
          mb: 0.5,
          objectFit: 'contain',
        }}
      />
      <Typography variant="subtitle2" fontWeight="bold" sx={{ fontSize: { xs: 12, sm: 14, md: 15 } }}>
        {MenuTitle}
      </Typography>
    </Box>
  );
};

export default OverViewCard;