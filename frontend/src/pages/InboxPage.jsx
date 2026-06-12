import { useState, useEffect, useCallback } from 'react';
import messagingService from '../services/messagingService';
import authService from '../services/authService';
import AppNav from '../components/AppNav';

const InboxPage = () => {
    const [rooms, setRooms] = useState([]);
    const [activeRoom, setActiveRoom] = useState(null);
    const [messages, setMessages] = useState([]);
    const [newMessage, setNewMessage] = useState('');
    const [availableUsers, setAvailableUsers] = useState([]);
    const [showNewChat, setShowNewChat] = useState(false);
    const [selectedUser, setSelectedUser] = useState('');
    const [error, setError] = useState(null);
    const [loadError, setLoadError] = useState(null);
    const currentUser = authService.getCurrentUser();

    const loadRooms = useCallback(async () => {
        const data = await messagingService.getRooms();
        setRooms(data);
        return data;
    }, []);

    useEffect(() => {
        let cancelled = false;

        messagingService.getRooms()
            .then((data) => {
                if (!cancelled) {
                    setRooms(data);
                    setLoadError(null);
                }
            })
            .catch((err) => {
                if (!cancelled) {
                    setLoadError(err.response?.data?.message || 'Failed to load chats.');
                }
            });

        return () => {
            cancelled = true;
        };
    }, []);

    const fetchMessages = async (roomId) => {
        try {
            const data = await messagingService.getRoomMessages(roomId);
            setMessages(data);
            setActiveRoom(roomId);
            setShowNewChat(false);
            setRooms((prev) =>
                prev.map((room) =>
                    room.roomId === roomId ? { ...room, hasUnseenMessages: false } : room
                )
            );
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to load messages.');
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
            const room = rooms.find((r) => r.roomId === activeRoom);
            if (room) recipientEmail = room.otherUserEmail;
        }

        try {
            await messagingService.sendMessage(recipientEmail, newMessage);
            setNewMessage('');
            if (showNewChat) {
                setShowNewChat(false);
                const newRooms = await loadRooms();
                const room = newRooms.find((r) => r.otherUserEmail === recipientEmail);
                if (room) {
                    fetchMessages(room.roomId);
                }
            } else {
                fetchMessages(activeRoom);
            }
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to send message');
        }
    };

    const startNewChat = async () => {
        try {
            setLoadError(null);
            const users = await messagingService.getAvailableUsers();
            setAvailableUsers(users);
            setShowNewChat(true);
            setActiveRoom(null);
            setMessages([]);
        } catch (err) {
            setLoadError(err.response?.data?.message || 'Failed to load users.');
        }
    };

    return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100vh', backgroundColor: '#f5f5f5' }}>
            <AppNav title="Messaging Inbox" />

            <div style={{ display: 'flex', flex: 1, overflow: 'hidden' }}>
                <div style={{ width: '300px', borderRight: '1px solid #ddd', backgroundColor: '#fff', display: 'flex', flexDirection: 'column' }}>
                    <div style={{ padding: '16px', borderBottom: '1px solid #ddd', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                        <h3 style={{ margin: 0 }}>Inbox</h3>
                        <button onClick={startNewChat} style={{ padding: '6px 12px', cursor: 'pointer' }}>New Chat</button>
                    </div>
                    {loadError && (
                        <p style={{ padding: '12px 16px', color: '#b91c1c', fontSize: '13px', margin: 0 }}>{loadError}</p>
                    )}
                    <div style={{ flex: 1, overflowY: 'auto' }}>
                        {rooms.length === 0 ? (
                            <p style={{ padding: '16px', color: '#888', textAlign: 'center' }}>No active chats.</p>
                        ) : (
                            rooms.map((room) => (
                                <div
                                    key={room.roomId}
                                    onClick={() => fetchMessages(room.roomId)}
                                    style={{
                                        padding: '16px',
                                        borderBottom: '1px solid #eee',
                                        cursor: 'pointer',
                                        backgroundColor: activeRoom === room.roomId ? '#eef2ff' : '#fff',
                                    }}
                                >
                                    <div style={{ fontWeight: 'bold' }}>{room.otherUserName}</div>
                                    <div style={{ fontSize: '0.85em', color: '#666' }}>
                                        {room.hasUnseenMessages
                                            ? <span style={{ color: 'red', fontWeight: 'bold' }}>New messages</span>
                                            : 'No new messages'}
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
                                            {availableUsers.map((u) => (
                                                <option key={u.email} value={u.email}>{u.name} ({u.email})</option>
                                            ))}
                                        </select>
                                    </div>
                                ) : (
                                    <h3 style={{ margin: 0 }}>
                                        {rooms.find((r) => r.roomId === activeRoom)?.otherUserName}
                                    </h3>
                                )}
                            </div>

                            <div style={{ flex: 1, padding: '16px', overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '8px' }}>
                                {messages.map((msg) => (
                                    <div
                                        key={msg.id}
                                        style={{
                                            alignSelf: msg.senderEmail === currentUser.email ? 'flex-end' : 'flex-start',
                                            backgroundColor: msg.senderEmail === currentUser.email ? '#007bff' : '#eee',
                                            color: msg.senderEmail === currentUser.email ? '#fff' : '#000',
                                            padding: '10px 14px',
                                            borderRadius: '16px',
                                            maxWidth: '70%',
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
