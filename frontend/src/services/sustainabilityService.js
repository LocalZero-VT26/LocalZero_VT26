import api from './api';

const logEcoAction = async (description) => {
    const responce = await api.post('/api/sustainability/log', {description})
    return responce.data;
};

export default {
    logEcoAction,
};