import axios from 'axios';

const api = axios.create({
    baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

const isAuthEndpoint = (url = '') => url.includes('/auth/login') || url.includes('/auth/register');

const clearSession = () => {
    localStorage.removeItem('user');
};

const isJwtExpired = (token) => {
    try {
        const payloadPart = token.split('.')[1];
        if (!payloadPart) {
            return true;
        }

        const payload = JSON.parse(
            atob(payloadPart.replace(/-/g, '+').replace(/_/g, '/'))
        );

        return typeof payload.exp === 'number' && Date.now() >= payload.exp * 1000;
    } catch {
        return true;
    }
};

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
        if (isJwtExpired(user.token)) {
            clearSession();

            if (!isAuthEndpoint(config.url)) {
                window.location.replace('/');
            }

            return Promise.reject(new Error('Session expired'));
        }

        config.headers = config.headers ?? {};
        config.headers.Authorization = `Bearer ${user.token}`;
    }

    return config;
});

api.interceptors.response.use(
    (response) => response,
    (error) => {
        const status = error?.response?.status;
        const url = error?.config?.url ?? '';

        if (status === 401 && !isAuthEndpoint(url)) {
            clearSession();
            window.location.replace('/');
        }

        return Promise.reject(error);
    }
);

export default api;