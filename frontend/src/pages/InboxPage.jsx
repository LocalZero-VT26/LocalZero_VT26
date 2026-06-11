import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import messagingService from '../services/messagingService';
import authService from '../services/authService';
import NotificationBell from '../components/NotificationBell.jsx';

const InboxPage = () => {
    const navigate = useNavigate();
    const [rooms, setRooms] = useState([]);
    const [activeRoom, setActiveRoom] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [availableUsers, setAvailableUsers] = useState([]);
    const [showNewChat, setShowNewChat] = useState(false);
    const [selectedUser, setSelectedUser] = useState('');
    const [error, setError] = useState(null);
    const currentUser = authService.getCurrentUser();

    const fetchRooms = async () => {
        try {
            const data = await messagingService.getRooms();
            setRooms(data);
        } catch (error) {
            console.error("Failed to load rooms", error);
        }
    };

    useEffect(() => {
        fetchRooms();
    }, []);

    const fetchMessages = async (roomId) => {
        try {
            const data = await messagingService.getRoomMessages(roomId);
            setMessages(data);
            setActiveRoom(roomId);
            setShowNewChat(false);
            
            setRooms(rooms.map(room => room.roomId === roomId ? { ...room, hasUnseenMessages: false } : room));
        } catch (error) {
            console.error("Failed to load messages", error);
        }
    };

    const handleSendMessage = async (e) => {
        e.preventDefault();
        setError(null);
        if (!newMessage.trim()) return;

        let recipientEmail = '';
        if (showNewChat) {
            recipientEmail = selectedUser;
            if (!recipientEmail) return;
        } else {
            const room = rooms.find(r => r.roomId === activeRoom);
            if (room) recipientEmail = room.otherUserEmail;
        }

        try {
            await messagingService.sendMessage(recipientEmail, newMessage);
            setNewMessage('');
            if (showNewChat) {
                await fetchRooms();
                setShowNewChat(false);
                const newRooms = await messagingService.getRooms();
                const room = newRooms.find(r => r.otherUserEmail === recipientEmail);
                if (room) {
                    fetchMessages(room.roomId);
                }
            } else {
                fetchMessages(activeRoom);
            }
        } catch (err) {
            setError(err.response?.data?.message || "Failed to send message");
        }
    };

    const startNewChat = async () => {
        try {
            const users = await messagingService.getAvailableUsers();
            setAvailableUsers(users.filter(u => u.email !== currentUser.email));
            setShowNewChat(true);
            setActiveRoom(null);
            setMessages([]);
        } catch (error) {
            console.error("Failed to fetch available users", error);
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100vh', backgroundColor: '#f5f5f5' }}>
            <nav style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 24px', borderBottom: '1px solid #ddd', backgroundColor: '#fff' }}>
                <span style={{ fontWeight: 'bold' }}>Messaging Inbox</span>
                <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                    <NotificationBell />
                    <button onClick={() => navigate('/home')} style={{cursor: 'pointer', padding: '6px 16px' }}>Home</button>
                    <button onClick={() => navigate('/profile')} style={{cursor: 'pointer', padding: '6px 16px' }}>Profile</button>
                </div>
            </nav>

            <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
                <div style={{ width: '300px', borderRight: '1px solid #ddd', backgroundColor: '#fff', display: 'flex', flexDirection: 'column' }}>
                <div style={{ padding: '16px', borderBottom: '1px solid #ddd', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <h3 style={{ margin: 0 }}>Inbox</h3>
                    <button onClick={startNewChat} style={{ padding: '6px 12px', cursor: 'pointer' }}>New Chat</button>
                </div>
                <div style={{ flex: 1, overflowY: 'auto' }}>
                    {rooms.length === 0 ? (
                        <p style={{ padding: '16px', color: '#888', textAlign: 'center' }}>No active chats.</p>
                    ) : (
                        rooms.map(room => (
                            <div 
                                key={room.roomId} 
                                onClick={() => fetchMessages(room.roomId)}
                                style={{ 
                                    padding: '16px', 
                                    borderBottom: '1px solid #eee', 
                                    cursor: 'pointer',
                                    backgroundColor: activeRoom === room.roomId ? '#eef2ff' : '#fff'
                                }}
                            >
                                <div style={{ fontWeight: 'bold' }}>{room.otherUserName}</div>
                                <div style={{ fontSize: '0.85em', color: '#666' }}>
                                    {room.hasUnseenMessages ? <span style={{ color: 'red', fontWeight: 'bold' }}>New messages</span> : 'No new messages'}
                                </div>
                            </div>
                        ))
                    )}
                </div>
            </div>

            <div style={{ flex: 1, display: 'flex', flexDirection: 'column', backgroundColor: '#fff' }}>
                {(activeRoom || showNewChat) ? (
                    <>
                        <div style={{ padding: '16px', borderBottom: '1px solid #ddd', backgroundColor: '#f9f9f9' }}>
                            {showNewChat ? (
                                <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                                    <span style={{ fontWeight: 'bold' }}>To:</span>
                                    <select 
                                        value={selectedUser} 
                                        onChange={(e) => setSelectedUser(e.target.value)}
                                        style={{ padding: '6px' }}
                                    >
                                        <option value="">Select a user...</option>
                                        {availableUsers.map(u => (
                                            <option key={u.email} value={u.email}>{u.name} ({u.email})</option>
                                        ))}
                                    </select>
                                </div>
                            ) : (
                                <h3 style={{ margin: 0 }}>
                                    {rooms.find(r => r.roomId === activeRoom)?.otherUserName}
                                </h3>
                            )}
                        </div>
                        
                        <div style={{ flex: 1, padding: '16px', overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '8px' }}>
                            {messages.map(msg => (
                                <div 
                                    key={msg.id} 
                                    style={{ 
                                        alignSelf: msg.senderEmail === currentUser.email ? 'flex-end' : 'flex-start',
                                        backgroundColor: msg.senderEmail === currentUser.email ? '#007bff' : '#eee',
                                        color: msg.senderEmail === currentUser.email ? '#fff' : '#000',
                                        padding: '10px 14px',
                                        borderRadius: '16px',
                                        maxWidth: '70%'
                                    }}
                                >
                                    <div>{msg.content}</div>
                                    <div style={{ fontSize: '0.7em', marginTop: '4px', textAlign: 'right', opacity: 0.8 }}>
                                        {new Date(msg.createdAt).toLocaleTimeString()}
                                    </div>
                                </div>
                            ))}
                        </div>

                        {error && <div style={{ padding: '8px', color: 'red', textAlign: 'center' }}>{error}</div>}

                        <form onSubmit={handleSendMessage} style={{ padding: '16px', borderTop: '1px solid #ddd', display: 'flex', gap: '8px' }}>
                            <input 
                                type="text" 
                                value={newMessage}
                                onChange={(e) => setNewMessage(e.target.value)}
                                placeholder="Type a message..."
                                style={{ flex: 1, padding: '10px', borderRadius: '4px', border: '1px solid #ccc' }}
                            />
                            <button type="submit" disabled={!newMessage.trim() || (showNewChat && !selectedUser)} style={{ padding: '10px 20px', cursor: 'pointer' }}>
                                Send
                            </button>
                        </form>
                    </>
                ) : (
                    <div style={{ flex: 1, display: 'flex', justifyContent: 'center', alignItems: 'center', color: '#888' }}>
                        Select a chat or start a new one to begin messaging.
                    </div>
                )}
            </div>
            </div>
        </div>
    );
};

export default InboxPage;