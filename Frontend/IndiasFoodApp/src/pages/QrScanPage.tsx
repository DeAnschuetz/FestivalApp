import CloseIcon from '@mui/icons-material/Close';
import CropFreeIcon from '@mui/icons-material/CropFree';
import { Box, Button, IconButton, Typography } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import QRCode from 'qrcode';
import Navigation from '../components/Navigation/Navigation';
import { useCart } from '../context/CartContext';

const QrScanPage = () => {
  const navigate = useNavigate();
  const { latestOrderItems, latestOrderId } = useCart();
  const [qrCodeDataUrl, setQrCodeDataUrl] = useState('');

  const qrPayload = useMemo(() => {
    const payload = {
      orderId: latestOrderId,
      items: latestOrderItems.map((item) => ({
        id: item.id,
        title: item.title,
        count: item.count,
        price: item.price,
      })),
      total: latestOrderItems.reduce((sum, item) => sum + item.price * item.count, 0),
    };

    return JSON.stringify(payload);
  }, [latestOrderId, latestOrderItems]);

  useEffect(() => {
    if (latestOrderItems.length === 0) {
      setQrCodeDataUrl('');
      return;
    }

    QRCode.toDataURL(qrPayload, { margin: 1, width: 148 })
      .then((url) => setQrCodeDataUrl(url))
      .catch(() => setQrCodeDataUrl(''));
  }, [latestOrderItems.length, qrPayload]);

  return (
    <Box
      sx={{
        bgcolor: '#E9E9E9',
        minHeight: '100vh',
        height: '100vh',
        '@supports (height: 100dvh)': {
          minHeight: '100dvh',
          height: '100dvh',
        },
        boxSizing: 'border-box',
        pb: { xs: 10, sm: 12 },
        px: { xs: 0.75, sm: 1.5, md: 2 },
        pt: { xs: 0.75, sm: 1, md: 1.5 },
      }}
    >
      <Box
        sx={{
          bgcolor: '#4A4E5A',
          borderRadius: { xs: 0.75, sm: 1 },
          p: { xs: 1.5, sm: 2, md: 2.2 },
          width: '100%',
          maxWidth: 520,
          mx: 'auto',
          minHeight: { xs: 'calc(100vh - 90px)', sm: 'calc(100vh - 110px)' },
          '@supports (height: 100dvh)': {
            minHeight: { xs: 'calc(100dvh - 90px)', sm: 'calc(100dvh - 110px)' },
          },
          display: 'flex',
          flexDirection: 'column',
        }}
      >
        <IconButton
          onClick={() => navigate(-1)}
          sx={{
            width: { xs: 30, sm: 34, md: 36 },
            height: { xs: 30, sm: 34, md: 36 },
            color: '#E6E6E6',
            border: '1px solid #E6E6E6',
            p: 0,
            mb: { xs: 1.25, sm: 1.5, md: 1.8 },
          }}
        >
          <CloseIcon sx={{ fontSize: { xs: 18, sm: 19, md: 20 } }} />
        </IconButton>

        <Typography
          sx={{
            color: '#ECECEC',
            fontSize: { xs: 24, sm: 36, md: 47 },
            lineHeight: 1.06,
            fontWeight: 700,
            mb: { xs: 2.25, sm: 3, md: 3.5 },
          }}
        >
          Scan your QR Code
        </Typography>

        <Box sx={{ display: 'flex', justifyContent: 'center', mb: { xs: 2.5, sm: 3.2, md: 4 }, flexGrow: 1, alignItems: 'center' }}>
          <Box
            sx={{
              position: 'relative',
              width: { xs: 190, sm: 220, md: 246 },
              height: { xs: 190, sm: 220, md: 246 },
              display: 'flex',
              alignItems: 'center',
              justifyContent: 'center',
            }}
          >
            <Box
              sx={{
                position: 'absolute',
                inset: 0,
                '&::before, &::after': {
                  content: '""',
                  position: 'absolute',
                  width: { xs: 22, sm: 26, md: 30 },
                  height: { xs: 22, sm: 26, md: 30 },
                  borderColor: '#E6E6E6',
                },
                '&::before': {
                  left: 0,
                  top: 0,
                  borderLeft: { xs: '4px solid', sm: '4px solid', md: '5px solid' },
                  borderTop: { xs: '4px solid', sm: '4px solid', md: '5px solid' },
                  borderRadius: '4px 0 0 0',
                },
                '&::after': {
                  right: 0,
                  top: 0,
                  borderRight: { xs: '4px solid', sm: '4px solid', md: '5px solid' },
                  borderTop: { xs: '4px solid', sm: '4px solid', md: '5px solid' },
                  borderRadius: '0 4px 0 0',
                },
              }}
            />

            <Box
              sx={{
                position: 'absolute',
                inset: 0,
                '&::before, &::after': {
                  content: '""',
                  position: 'absolute',
                  width: { xs: 22, sm: 26, md: 30 },
                  height: { xs: 22, sm: 26, md: 30 },
                  borderColor: '#E6E6E6',
                },
                '&::before': {
                  left: 0,
                  bottom: 0,
                  borderLeft: { xs: '4px solid', sm: '4px solid', md: '5px solid' },
                  borderBottom: { xs: '4px solid', sm: '4px solid', md: '5px solid' },
                  borderRadius: '0 0 0 4px',
                },
                '&::after': {
                  right: 0,
                  bottom: 0,
                  borderRight: { xs: '4px solid', sm: '4px solid', md: '5px solid' },
                  borderBottom: { xs: '4px solid', sm: '4px solid', md: '5px solid' },
                  borderRadius: '0 0 4px 0',
                },
              }}
            />

            <Box
              sx={{
                width: { xs: 132, sm: 154, md: 174 },
                height: { xs: 132, sm: 154, md: 174 },
                bgcolor: '#F0F0F0',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
              }}
            >
              {qrCodeDataUrl ? (
                <Box
                  component="img"
                  src={qrCodeDataUrl}
                  alt="Order QR"
                  sx={{ width: { xs: 112, sm: 130, md: 148 }, height: { xs: 112, sm: 130, md: 148 } }}
                />
              ) : (
                <Typography sx={{ fontSize: 12, color: '#777' }}>No QR</Typography>
              )}
            </Box>
          </Box>
        </Box>

        <Button
          fullWidth
          sx={{
            textTransform: 'none',
            borderRadius: '999px',
            bgcolor: '#CFE3C3',
            color: '#3E4251',
            py: { xs: 0.9, sm: 1.05, md: 1.2 },
            fontSize: { xs: 17, sm: 25, md: 39 },
            minHeight: { xs: 50, sm: 58, md: 68 },
            mt: 'auto',
            '&:hover': { bgcolor: '#C2DAB4' },
          }}
        >
          <Box sx={{ display: 'inline-flex', alignItems: 'center', gap: { xs: 0.7, sm: 1, md: 1.2 } }}>
            <CropFreeIcon sx={{ fontSize: { xs: 22, sm: 27, md: 33 } }} />
            <span>Scan QR Code</span>
          </Box>
        </Button>
      </Box>

      <Navigation />
    </Box>
  );
};

export default QrScanPage;
