import { Box, Divider, Typography } from '@mui/material';
import { useEffect, useMemo, useState } from 'react';
import QRCode from 'qrcode';
import Header from '../components/Header/Header';
import Navigation from '../components/Navigation/Navigation';
import { useCart } from '../context/CartContext';

const TrackingPage = () => {
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

    QRCode.toDataURL(qrPayload, { margin: 1, width: 88 })
      .then((url) => setQrCodeDataUrl(url))
      .catch(() => setQrCodeDataUrl(''));
  }, [latestOrderItems.length, qrPayload]);

  return (
    <Box sx={{ bgcolor: '#FAF7F5', minHeight: '100vh', pb: { xs: 10, sm: 12 }, pt: { xs: 9, sm: 10, md: 12 } }}>
      <Header />

      <Box sx={{ px: { xs: 1.5, sm: 2, md: 3 }, pt: 1.5, display: 'flex', flexDirection: 'column', gap: 1.25 }}>
        <Box
          sx={{
            bgcolor: '#D5E8CD',
            borderRadius: 2.5,
            p: 1.2,
            border: '3px solid #BCD7B2',
          }}
        >
          <Box
            sx={{
              width: 84,
              bgcolor: '#CAE1BF',
              borderRadius: 10,
              textAlign: 'center',
              py: 0.2,
              mb: 1,
              mx: 'auto',
            }}
          >
            <Typography sx={{ fontSize: 10, color: '#4F5D4A', fontWeight: 600 }}>
              Order #{latestOrderId ?? 1}
            </Typography>
          </Box>

          <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', px: 1 }}>
            <Box
              sx={{
                width: 72,
                height: 72,
                borderRadius: 1,
                bgcolor: '#E7F0E3',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                overflow: 'hidden',
              }}
            >
              {qrCodeDataUrl ? (
                <Box component="img" src={qrCodeDataUrl} alt="Order QR" sx={{ width: 64, height: 64 }} />
              ) : (
                <Typography sx={{ fontSize: 10, color: '#6C7769' }}>No QR</Typography>
              )}
            </Box>

            <Divider orientation="vertical" flexItem sx={{ mx: 1.5, borderColor: '#9EA99A' }} />

            <Box sx={{ minWidth: 80 }}>
              <Typography sx={{ color: '#5F5F5F', fontSize: 12 }}>Food Truck</Typography>
              <Typography sx={{ color: '#2E2E2E', fontSize: 28, lineHeight: 1, fontWeight: 500 }}>5</Typography>
            </Box>
          </Box>
        </Box>

        <Box
          sx={{
            bgcolor: '#FAF7F5',
            borderRadius: 2.5,
            p: 1.2,
            border: '2px solid #4C4C4C',
          }}
        >
          <Box
            sx={{
              width: 84,
              bgcolor: '#FFFFFF',
              borderRadius: 10,
              textAlign: 'center',
              py: 0.15,
              mb: 1,
              ml: 0.5,
              border: '1px solid #4C4C4C',
            }}
          >
            <Typography sx={{ fontSize: 10, color: '#4F5D4A', fontWeight: 600 }}>
              Order #1
            </Typography>
          </Box>

          <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', px: 0.6 }}>
            <Box>
              <Typography sx={{ color: '#5F5F5F', fontSize: 12, mb: 0.1 }}>Remaining time</Typography>
              <Typography sx={{ color: '#2E2E2E', fontSize: 28, lineHeight: 1, fontWeight: 500 }}>2mins</Typography>
            </Box>

            <Divider orientation="vertical" flexItem sx={{ mx: 1.5, borderColor: '#9EA99A' }} />

            <Box sx={{ minWidth: 80 }}>
              <Typography sx={{ color: '#5F5F5F', fontSize: 12 }}>Food Truck</Typography>
              <Typography sx={{ color: '#2E2E2E', fontSize: 28, lineHeight: 1, fontWeight: 500 }}>3</Typography>
            </Box>
          </Box>
        </Box>

        {latestOrderItems.length === 0 && (
          <Typography sx={{ textAlign: 'center', mt: 1, color: '#888', fontSize: 13 }}>
            Noch keine aktive Bestellung.
          </Typography>
        )}
      </Box>

      <Navigation />
    </Box>
  );
};

export default TrackingPage;
