import api from './api';

const logEcoAction = async (description) => {
    const response = await api.post('/api/sustainability/log', {description})
    return response.data;
};

const getHistory = async () => {
    const response = await api.get('/api/sustainability/history');
    return response.data;
}

export default {
    logEcoAction,
    getHistory,
};