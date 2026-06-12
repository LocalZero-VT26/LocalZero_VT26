import api from './api'

const userService = {
    getAvailableUser: async () => {
        const response = await api.get('/api/users/available')
        return response.data
    },

    getManageableUsers: async () => {
        const response = await api.get('/users/manageable')
        return response.data
    },

    assignRole: async (userId, role) => {
        await api.put('/users/assign-role', { userId, role })
    },
}

export default userService
