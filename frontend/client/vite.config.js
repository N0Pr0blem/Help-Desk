// vite.config.js
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// Обновите vite.config.js
export default defineConfig({
  plugins: [react()],
  server: {
    proxy: {
      '/api/v1': {  
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        rewrite: (path) => path.replace(/^\/api\/v1/, '')  // Удаляем префикс
      }
    }
  }
})

