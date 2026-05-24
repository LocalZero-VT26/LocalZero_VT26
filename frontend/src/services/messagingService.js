import api from './api';

const messagingService = {
    getRooms: async () => {
        const response = await api.get('/api/messaging/rooms');
        return response.data;
    },

    getRoomMessages: async (roomId) => {
        const response = await api.get(`/api/messaging/rooms/${roomId}`);
        return response.data;
    },

    sendMessage: async (recipientEmail, content) => {
        const response = await api.post('/api/messaging/send', {
            recipientEmail,
            content
        });
        return response.data;
    },

    getAvailableUsers: async () => {
        const response = await api.get('/api/users/available');
        return response.data;
    }
};

export default messagingService;
