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

const OverViewCard: React.FC<OverViewCardProps> = ({ MenuOverViewImage, MenuTitle, cardWidth, imgWidth, cardHeight = 140, navigateToMenuDetail }) => {
  return (
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
      onClick={navigateToMenuDetail}
    >
      <Box
        component="img"
        src={MenuOverViewImage}
        alt={MenuTitle || ''}
        sx={{ width: imgWidth, height: 'auto', mb: 1 }}
      />
      <Typography variant="subtitle1" fontWeight="bold" sx={{ fontSize: { xs: 16, sm: 18 } }}>
        {MenuTitle}
      </Typography>
    </Box>
  );
};

export default OverViewCard;