import api from './api';

const authService = {
    register: async (name, location, email, password) => {
        const response = await api.post('/auth/register', {
            name,
            location,
            email,
            password
        });
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

    // Logga ut genom att blacklista token på servern och rensa localStorage
    logout: async () => {
        try {
            const user = JSON.parse(localStorage.getItem('user'));
            const token = user?.token;

            if (!token) {
                localStorage.removeItem('user');
                return;
            }

            await api.post('/auth/logout');
            localStorage.removeItem('user');
        } catch (error) {
            console.error('Logout error:', error);
            throw error;
        }
    },

    // Hämta aktuell inloggad användare
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