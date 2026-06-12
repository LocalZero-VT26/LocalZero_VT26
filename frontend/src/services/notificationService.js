import api from './api';

const notificationService = {
    getNotifications: async () => {
        const response = await api.get('/api/notifications');
        return response.data;
    },

    getUnreadCount: async () => {
        const response = await api.get('/api/notifications/unread-count');
        return response.data.count;
    },

    markRead: async (notificationId) => {
        await api.put(`/api/notifications/${notificationId}/read`);
    },

    markAllRead: async () => {
        await api.put('/api/notifications/read-all');
    }
};

export default notificationService;
