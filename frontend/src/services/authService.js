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

    // Logga ut genom att rensa localStorage
    logout: () => {
        localStorage.removeItem('user');
    },

    // Hämta aktuell inloggad användare
    getCurrentUser: () => {
        return JSON.parse(localStorage.getItem('user'));
    }
};

export default authService;