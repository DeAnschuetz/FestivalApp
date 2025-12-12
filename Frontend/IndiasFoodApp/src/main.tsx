import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.tsx';

const appDiv = document.querySelector<HTMLDivElement>('#app');
if (appDiv) {
  ReactDOM.createRoot(appDiv).render(
    <React.StrictMode>
      <div id="app" style={{ minHeight: '100vh', background: '#FAF7F5', marginTop: 8 }}>        
        <App />
      </div>
    </React.StrictMode>
  );
}
