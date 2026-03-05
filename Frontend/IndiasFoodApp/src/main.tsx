import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.tsx';
import './style.css';

const appDiv = document.querySelector<HTMLDivElement>('#app');
if (appDiv) {
  ReactDOM.createRoot(appDiv).render(
    <React.StrictMode>
      <App />
    </React.StrictMode>
  );
}
