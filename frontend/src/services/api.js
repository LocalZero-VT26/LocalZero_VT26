import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});


api.interceptors.request.use((config) => {
    const storedUser = localStorage.getItem('user');
    let user = null;

    if (storedUser) {
        try {
            user = JSON.parse(storedUser);
        } catch {
            user = null;
        }
    }

    if (user && user.token) {
        config.headers.Authorization = `Bearer ${user.token}`;
    }
    return config;
});

export default api;