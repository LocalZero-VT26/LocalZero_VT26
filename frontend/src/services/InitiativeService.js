import api from './api'

const InitiativeService = {

    getAll: async () => {
        const response = await api.get('/api/initiatives')
        return response.data
    },

    create: async (initiativeData) => {
        const response = await api.post('/api/initiatives', initiativeData)
        return response.data
    },

    join: async (id) => {
        const response = await api.post(`/api/initiatives/${id}/join`)
        return response.data
    },

    postUpdate: async (id, updateData) => {
        const response = await api.post(`/api/initiatives/${id}/updates`, updateData)
        return response.data
    }
};

export default InitiativeService;
