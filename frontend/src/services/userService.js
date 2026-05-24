import api from './api'

const userService = {
    getAvailableUser: async () => {
        const response = await api.get('/users/available')
        return response.data
    }
}

export default userService