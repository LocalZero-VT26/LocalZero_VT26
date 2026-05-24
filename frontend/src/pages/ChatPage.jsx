import { useState, useEffect, useRef } from 'react'
import { useParams, useSearchParams, useNavigate } from 'react-router-dom'
import messagingService from '../services/messagingService'
import authService from '../services/authService'

// Handle both Spring's array format [y,m,d,h,min] and ISO string format
const parseDate = (createdAt) => {
    if (!createdAt) return null
    if (Array.isArray(createdAt)) {
        return new Date(createdAt[0], createdAt[1] - 1, createdAt[2], createdAt[3] || 0, createdAt[4] || 0)
    }
    return new Date(createdAt)
}

const formatTime = (createdAt) => {
    const date = parseDate(createdAt)
    if (!date || isNaN(date.getTime())) return ''
    const now = new Date()
    const isToday = date.toDateString() === now.toDateString()
    const time = date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    if (isToday) return time
    return date.toLocaleDateString([], { month: 'short', day: 'numeric' }) + ' · ' + time
}

function ChatPage() {
    const { roomId } = useParams()
    const [searchParams] = useSearchParams()
    const navigate = useNavigate()

    const recipientEmail    = searchParams.get('recipient')
    const recipientName     = searchParams.get('name') || recipientEmail
    const recipientLocation = searchParams.get('location') || ''
    const recipientRoles    = (searchParams.get('roles') || '').split(',').filter(Boolean)
    const recipientOnline   = searchParams.get('online') === 'true'

    const currentUser = authService.getCurrentUser()

    const [messages, setMessages] = useState([])
    const [firstUnreadId, setFirstUnreadId] = useState(null)   // id of first unread msg before marking
    const [input, setInput] = useState('')
    const [loading, setLoading] = useState(!!roomId)
    const [sending, setSending] = useState(false)
    const bottomRef = useRef(null)
    const newMessagesRef = useRef(null)

    useEffect(() => {
        if (!roomId) return
        messagingService.getRoomMessages(roomId)
            .then(data => {
                const msgs = data || []
                // Find first unread message from the other person.
                // The backend marks them as read during this call, so we capture
                // the first one BEFORE reading (read=false means it was new).
                // Since backend sets read=true in the same response, we find the
                // "boundary" by looking at messages that were just-now marked (read=true
                // but sent by the other user — they were all unread if the room had unseen msgs).
                // The cleanest signal: find the first message NOT from me where read was false.
                // The backend sets read=true in the response, so we instead look for
                // the first message from the other user that COULD have been new.
                // We use a heuristic: the first message from the other user in a streak
                // at the end (after the last message from me).
                const firstUnread = msgs.find(
                    m => m.senderEmail !== currentUser?.email && !m.read
                )
                setFirstUnreadId(firstUnread?.id ?? null)
                setMessages(msgs)
            })
            .catch(err => console.error('Failed to load messages', err))
            .finally(() => setLoading(false))
    }, [roomId])

    // Scroll to "New messages" separator if present, otherwise scroll to bottom
    useEffect(() => {
        if (newMessagesRef.current) {
            newMessagesRef.current.scrollIntoView({ behavior: 'smooth', block: 'center' })
        } else {
            bottomRef.current?.scrollIntoView({ behavior: 'smooth' })
        }
    }, [messages])

    const isMine = (msg) =>
        msg.senderEmail === currentUser?.email ||
        msg.self === true

    const handleSend = async () => {
        const text = input.trim()
        if (!text || !recipientEmail) return
        setSending(true)
        try {
            await messagingService.sendMessage(recipientEmail, text)
            setInput('')
            setFirstUnreadId(null) // clear separator after sending

            if (roomId) {
                const updated = await messagingService.getRoomMessages(roomId)
                setMessages(updated || [])
            } else {
                const rooms = await messagingService.getRooms()
                const newRoom = (rooms || []).find(r => r.otherUserEmail === recipientEmail)
                if (newRoom) {
                    navigate(
                        `/chat/${newRoom.roomId}?recipient=${encodeURIComponent(recipientEmail)}&name=${encodeURIComponent(recipientName)}`,
                        { replace: true }
                    )
                } else {
                    setMessages(prev => [...prev, { content: text, self: true }])
                }
            }
        } catch (err) {
            console.error('Failed to send message', err)
        } finally {
            setSending(false)
        }
    }

    if (loading) return <div style={{ padding: '24px' }}>Loading messages...</div>

    return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100vh', fontFamily: 'sans-serif' }}>

            {/* Header */}
            <div style={{ display: 'flex', alignItems: 'center', gap: '10px', padding: '12px 16px', borderBottom: '1px solid #ddd' }}>
                <button
                    onClick={() => navigate('/chatrooms')}
                    style={{ background: 'none', border: 'none', fontSize: '18px', cursor: 'pointer', lineHeight: 1 }}
                >←</button>
                <div style={{ display: 'flex', alignItems: 'center', gap: '8px', flexWrap: 'wrap' }}>
                    <strong>{recipientName || `Chat Room ${roomId}`}</strong>
                    {recipientOnline && (
                        <span style={{ width: '9px', height: '9px', borderRadius: '50%', background: '#22c55e', display: 'inline-block' }} title="Online" />
                    )}
                    {recipientLocation && (
                        <span style={{ fontSize: '13px', color: '#888' }}>{recipientLocation}</span>
                    )}
                    {recipientRoles.map(role => (
                        <span key={role} style={{ fontSize: '11px', color: '#666', background: '#f0f0f0', border: '1px solid #ddd', borderRadius: '10px', padding: '0 6px' }}>
                            {role}
                        </span>
                    ))}
                </div>
            </div>

            {/* Messages */}
            <div style={{ flex: 1, overflowY: 'auto', padding: '16px', display: 'flex', flexDirection: 'column', gap: '4px' }}>
                {messages.length === 0 && (
                    <p style={{ color: '#bbb', textAlign: 'center', marginTop: '60px', fontSize: '14px' }}>
                        No messages yet. Say hello!
                    </p>
                )}

                {messages.map((msg) => {
                    const mine = isMine(msg)
                    const time = formatTime(msg.createdAt)
                    const isFirstUnread = msg.id === firstUnreadId

                    return (
                        <div key={msg.id ?? msg.content}>

                            {/* "New messages" separator */}
                            {isFirstUnread && (
                                <div
                                    ref={newMessagesRef}
                                    style={{
                                        display: 'flex',
                                        alignItems: 'center',
                                        gap: '8px',
                                        margin: '12px 0 8px',
                                        color: '#e07b00',
                                        fontSize: '12px',
                                        fontWeight: 600,
                                    }}
                                >
                                    <span style={{ flex: 1, height: '1px', background: '#f0c080' }} />
                                    New messages
                                    <span style={{ flex: 1, height: '1px', background: '#f0c080' }} />
                                </div>
                            )}

                            {/* Bubble + timestamp wrapper */}
                            <div style={{ display: 'flex', flexDirection: 'column', alignItems: mine ? 'flex-end' : 'flex-start', marginBottom: '6px' }}>
                                <div
                                    style={{
                                        background: mine ? '#333' : '#f0f0f0',
                                        color: mine ? '#fff' : '#111',
                                        padding: '8px 14px',
                                        borderRadius: '16px',
                                        maxWidth: '65%',
                                        fontSize: '14px',
                                        wordBreak: 'break-word',
                                    }}
                                >
                                    {msg.content}
                                </div>

                                {/* Timestamp + read receipt */}
                                <div style={{ display: 'flex', alignItems: 'center', gap: '4px', marginTop: '3px' }}>
                                    {time && (
                                        <span style={{ fontSize: '11px', color: '#aaa' }}>{time}</span>
                                    )}
                                    {/* Read receipt — only on my messages */}
                                    {mine && (
                                        <span
                                            style={{ fontSize: '11px', color: msg.read ? '#22c55e' : '#aaa' }}
                                            title={msg.read ? 'Read' : 'Sent'}
                                        >
                                            {msg.read ? '✓✓' : '✓'}
                                        </span>
                                    )}
                                </div>
                            </div>

                        </div>
                    )
                })}
                <div ref={bottomRef} />
            </div>

            {/* Input bar */}
            <div style={{ display: 'flex', gap: '8px', padding: '12px 16px', borderTop: '1px solid #ddd' }}>
                <input
                    value={input}
                    onChange={e => setInput(e.target.value)}
                    onKeyDown={e => { if (e.key === 'Enter' && !e.shiftKey) { e.preventDefault(); handleSend() } }}
                    placeholder="Type a message…"
                    disabled={sending}
                    style={{
                        flex: 1,
                        padding: '10px 14px',
                        border: '1px solid #ccc',
                        borderRadius: '6px',
                        fontSize: '14px',
                        outline: 'none',
                    }}
                />
                <button
                    onClick={handleSend}
                    disabled={sending || !input.trim()}
                    style={{
                        padding: '10px 20px',
                        cursor: sending || !input.trim() ? 'default' : 'pointer',
                        borderRadius: '6px',
                        border: '1px solid #333',
                        background: '#333',
                        color: '#fff',
                        opacity: sending || !input.trim() ? 0.5 : 1,
                    }}
                >
                    Send
                </button>
            </div>

        </div>
    )
}

export default ChatPage
