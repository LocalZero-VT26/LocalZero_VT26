import { useState, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import messagingService from '../services/messagingService'
import userService from '../services/userService'
import authService from '../services/authService'

function ChatRoomPage() {
    const [rooms, setRooms] = useState([])
    const [users, setUsers] = useState([])
    const [loading, setLoading] = useState(true)
    const navigate = useNavigate()
    const currentUser = authService.getCurrentUser()

    useEffect(() => {
        const fetchData = async () => {
            try {
                const [roomsData, usersData] = await Promise.all([
                    messagingService.getRooms(),
                    userService.getAvailableUser()
                ])
                setRooms(roomsData || [])
                setUsers(usersData || [])
            } catch (err) {
                console.error('Failed to load chat data', err)
            } finally {
                setLoading(false)
            }
        }
        fetchData()
    }, [])

    // Quick lookup: email → full user object (location, roles, online)
    const userMap = Object.fromEntries(users.map(u => [u.email, u]))

    const buildChatUrl = (roomId, user, online = false) => {
        const params = new URLSearchParams({
            recipient: user.email,
            name:      user.name || '',
            location:  user.location || '',
            roles:     (user.roles || []).join(','),
            online:    online ? 'true' : 'false',
        })
        return roomId ? `/chat/${roomId}?${params}` : `/chat/new?${params}`
    }

    const handleRoomClick = (room) => {
        const fullUser = userMap[room.otherUserEmail] || {
            email:    room.otherUserEmail,
            name:     room.otherUserName,
            location: '',
            roles:    [],
        }
        navigate(buildChatUrl(room.roomId, fullUser, room.otherUserOnline))
    }

    const handleUserClick = (user) => {
        const existingRoom = rooms.find(r => r.otherUserEmail === user.email)
        navigate(buildChatUrl(existingRoom?.roomId, user, user.online))
    }

    const Dot = ({ color }) => (
        <span style={{ width: '9px', height: '9px', borderRadius: '50%', background: color, flexShrink: 0, display: 'inline-block' }} />
    )

    const rowStyle = {
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: '12px 14px',
        borderBottom: '1px solid #eee',
        cursor: 'pointer',
    }

    if (loading) return <div style={{ padding: '24px' }}>Loading...</div>

    return (
        <div style={{ display: 'flex', height: '100vh', fontFamily: 'sans-serif' }}>

            {/* Left panel — Conversations */}
            <div style={{ width: '50%', borderRight: '1px solid #ddd', display: 'flex', flexDirection: 'column' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px', padding: '14px 16px', borderBottom: '1px solid #ddd' }}>
                    <button
                        onClick={() => navigate('/home')}
                        style={{ background: 'none', border: 'none', fontSize: '18px', cursor: 'pointer', lineHeight: 1 }}
                    >←</button>
                    <strong>Conversations</strong>
                </div>

                <div style={{ overflowY: 'auto', flex: 1 }}>
                    {rooms.length === 0 ? (
                        <p style={{ color: '#999', padding: '20px 16px', margin: 0 }}>No conversations yet.</p>
                    ) : (
                        rooms.map(room => {
                            const fullUser = userMap[room.otherUserEmail]
                            const location = fullUser?.location
                            const roles    = fullUser?.roles || []
                            const isOnline = room.otherUserOnline

                            return (
                                <div key={room.roomId} onClick={() => handleRoomClick(room)} style={rowStyle}>
                                    <div style={{ flex: 1, minWidth: 0 }}>
                                        {/* Name + inline meta */}
                                        <div style={{ display: 'flex', alignItems: 'center', gap: '6px', flexWrap: 'wrap' }}>
                                            <span style={{ fontWeight: room.hasUnseenMessages ? 700 : 500 }}>
                                                {room.otherUserName}
                                            </span>
                                            {location && (
                                                <span style={{ fontSize: '12px', color: '#888' }}>{location}</span>
                                            )}
                                            {roles.map(role => (
                                                <span key={role} style={{ fontSize: '11px', color: '#666', background: '#f0f0f0', border: '1px solid #ddd', borderRadius: '10px', padding: '0 6px' }}>
                                                    {role}
                                                </span>
                                            ))}
                                        </div>

                                        {room.hasUnseenMessages && (
                                            <div style={{ fontSize: '12px', color: '#e07b00', marginTop: '2px' }}>
                                                New messages
                                            </div>
                                        )}
                                    </div>

                                    {/* Right-side indicators */}
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '6px', marginLeft: '8px', flexShrink: 0 }}>
                                        {isOnline && <Dot color="#22c55e" />}
                                        {room.hasUnseenMessages && <Dot color="#e07b00" />}
                                    </div>
                                </div>
                            )
                        })
                    )}
                </div>
            </div>

            {/* Right panel — Contacts */}
            <div style={{ width: '50%', display: 'flex', flexDirection: 'column' }}>
                <div style={{ padding: '14px 16px', borderBottom: '1px solid #ddd' }}>
                    <strong>Users</strong>
                </div>

                <div style={{ overflowY: 'auto', flex: 1 }}>
                    {users.length === 0 ? (
                        <p style={{ color: '#999', padding: '20px 16px', margin: 0 }}>No users found.</p>
                    ) : (
                        users
                            .filter(u => u.email !== currentUser?.email)
                            .map(user => (
                                <div key={user.email} onClick={() => handleUserClick(user)} style={rowStyle}>
                                    {/* All info on one line */}
                                    <div style={{ display: 'flex', alignItems: 'center', gap: '6px', flexWrap: 'wrap', flex: 1, minWidth: 0 }}>
                                        <span style={{ fontWeight: 500 }}>{user.name}</span>
                                        {user.location && (
                                            <span style={{ fontSize: '12px', color: '#888' }}>{user.location}</span>
                                        )}
                                        {(user.roles || []).map(role => (
                                            <span key={role} style={{ fontSize: '11px', color: '#666', background: '#f0f0f0', border: '1px solid #ddd', borderRadius: '10px', padding: '0 6px' }}>
                                                {role}
                                            </span>
                                        ))}
                                    </div>

                                    {user.online && <Dot color="#22c55e" />}
                                </div>
                            ))
                    )}
                </div>
            </div>

        </div>
    )
}

export default ChatRoomPage
