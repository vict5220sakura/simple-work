import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import setupExtend from 'vite-plugin-vue-setup-extend'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    // vueDevTools(),
    setupExtend(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    },
  },
  base: '/simple-server/web/',
  server:{
    port: 5175,
    proxy:{
      '^/simple-server/api/.*': {
        changeOrigin: true,
        target: 'http://127.0.0.1:30001',
      },
      '^/simple-server/ws/.*': {
        changeOrigin: true,
        target: 'ws://127.0.0.1:30001',
      }
    }
  }
})
