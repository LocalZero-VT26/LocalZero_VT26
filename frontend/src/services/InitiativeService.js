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
,

    // New endpoints for comments and likes
    postComment: async (updateId, commentData) => {
        const response = await api.post(`/api/updates/${updateId}/comments`, commentData)
        return response.data
    },

    getComments: async (updateId) => {
        const response = await api.get(`/api/updates/${updateId}/comments`)
        return response.data
    },

    toggleLike: async (updateId) => {
        const response = await api.post(`/api/updates/${updateId}/likes`)
        return response.data
    },

    getLikeInfo: async (updateId) => {
        const response = await api.get(`/api/updates/${updateId}/likes`)
        return response.data
    }
};

export default InitiativeService;
