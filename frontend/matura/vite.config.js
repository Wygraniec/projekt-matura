import {defineConfig} from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            '/v1': {
                target: 'http://business-rangers.gl.at.ply.gg:6383/api',
                changeOrigin: true,
                secure: false,
            },
        },
    },
})