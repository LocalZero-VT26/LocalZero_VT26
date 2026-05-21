import api from './api'

const userService = {
    getAvailableUser: async () => {
        const response = await api.post('/users/available')
        return response.data
    }
}

export default userService