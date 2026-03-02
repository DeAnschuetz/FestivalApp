import React, { useState } from 'react';
import { Box, Typography, Button } from '@mui/material';
interface OverViewCardProps {
    MenuDetailViewImage: string;
    MenuTitle?: string;
    /* cardWidth?: number | string; */
    imgWidth?: number;
    /* cardHeight?: number | string; */
}

const OverViewCard: React.FC<OverViewCardProps> = ({ MenuDetailViewImage, MenuTitle,  imgWidth }) => {
  const [itemCount, setItemCount] = useState(0);
  return (
    <Box
      sx={{
        backgroundColor: '#ffffffff',
        p:1,
        m:1,
        borderRadius: 4,
        display: 'flex',
        flexDirection: 'column',
      }}
    >
      <Box
        sx={{
          bgcolor: '#F0ECE9',
          borderRadius: 2,
          flexDirection: 'row',
          p: 1,
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
        <Box sx={{borderRadius: '20px', border: '1px solid #E3DCD5',  display: 'flex', alignItems: 'center', width: '80%', justifyContent: 'center'}}>
          <Button onClick={() => setItemCount(itemCount - 1)}>
            —
          </Button>
          <Box>{itemCount}</Box>
          <Button onClick={() => setItemCount(itemCount + 1)}>
            +
          </Button>
        </Box>
        
      </Box>
    </Box>
  );
};

export default OverViewCard;