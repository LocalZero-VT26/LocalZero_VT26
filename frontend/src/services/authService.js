import api from './api';

const authService = {
    register: async (name, location, email, password) => {
        const response = await api.post('/auth/register', {
            name,
            location,
            email,
            password
        });
        if (response.data.token) {
            localStorage.setItem('user', JSON.stringify(response.data));
        }
        return response.data;
    },

    login: async (email, password) => {
        const response = await api.post('/auth/login', { email, password });
        if (response.data.token) {
            // Sparar användardata och token lokalt i webbläsaren
            localStorage.setItem('user', JSON.stringify(response.data));
        }
        return response.data;
    },

    logout: async () => {
        try {
            await api.post('/auth/logout');
        } catch (err) {
            console.error('Logout error:', err);
        } finally {
            localStorage.removeItem('user');
        }
    },

    getCurrentUser: () => {
        const storedUser = localStorage.getItem('user');

        if (!storedUser) {
            return null;
        }

        try {
            return JSON.parse(storedUser);
        } catch (error) {
            localStorage.removeItem('user');
            return null;
        }
    }
};

export default authService;