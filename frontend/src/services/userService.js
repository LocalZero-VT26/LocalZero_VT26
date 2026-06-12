import api from './api'

const userService = {
    getAvailableUser: async () => {
        const response = await api.get('/api/users/available')
        return response.data
    }
}

export default userService