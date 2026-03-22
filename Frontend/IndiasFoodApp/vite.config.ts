import { defineConfig, loadEnv } from 'vite';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const backendTarget = env.VITE_API_BASE_URL || 'http://0.0.0.0:8080';
  const backendOrigin = new URL(backendTarget).origin;

  return {
    server: {
      host: true,
      port: 5173,
      proxy: {
        '/ffb': {
          target: backendTarget,
          changeOrigin: true,
          secure: false,
          headers: {
            origin: backendOrigin,
            referer: `${backendOrigin}/`,
          },
        },
      },
    },
    preview: {
      host: true,
      port: 5173,
    },
  };
});
