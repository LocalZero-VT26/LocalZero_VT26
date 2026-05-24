import api from './api';

const logEcoAction = async (description) => {
    const response = await api.post('/api/sustainability/log', {description})
    return response.data;
};

const getHistory = async () => {
    const response = await api.get('/api/sustainability/history');
    return response.data;
}

const getCommunityStats = async () => {
    const response = await api.get('/api/sustainability/dashboard/community');
    return response.data;
}

export default {
    logEcoAction,
    getHistory,
    getCommunityStats,
};