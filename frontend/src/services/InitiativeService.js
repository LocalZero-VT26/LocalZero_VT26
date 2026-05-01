import api from './api'

const InitiativeService = {

    getAll: async () => {
        const response = await api.get('/initiatives')
        return response.data
    },

    create: async (initiativeData) => {
        const response = await api.post('/initiatives', initiativeData)
        return response.data
    },

    join: async (Id) => {
        const response = await api.post('/initiatives/join', {Id})
        return response.data
    },

    postUpdate: async (Id, initiativeData) => {
        const response = await api.put(`/initiatives/${Id}`, initiativeData)
        return response.data
    }
};