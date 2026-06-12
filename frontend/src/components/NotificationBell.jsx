import { useState, useEffect, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import notificationService from '../services/notificationService';

const POLL_INTERVAL_MS = 20000;

function NotificationBell() {
    const navigate = useNavigate();
    const [unreadCount, setUnreadCount] = useState(0);
    const [notifications, setNotifications] = useState([]);
    const [open, setOpen] = useState(false);
    const containerRef = useRef(null);

    useEffect(() => {
        const refreshUnreadCount = () => {
            notificationService.getUnreadCount()
                .then(setUnreadCount)
                .catch(() => {
                    // Ignore polling errors (e.g. backend temporarily unreachable).
                });
        };

        refreshUnreadCount();
        const interval = setInterval(refreshUnreadCount, POLL_INTERVAL_MS);
        return () => clearInterval(interval);
    }, []);

    useEffect(() => {
        if (!open) return;
        const handleOutsideClick = (e) => {
            if (containerRef.current && !containerRef.current.contains(e.target)) {
                setOpen(false);
            }
        };
        document.addEventListener('mousedown', handleOutsideClick);
        return () => document.removeEventListener('mousedown', handleOutsideClick);
    }, [open]);

    const handleToggle = async () => {
        if (!open) {
            try {
                setNotifications(await notificationService.getNotifications());
            } catch {
                setNotifications([]);
            }
        }
        setOpen(prev => !prev);
    };

    const handleNotificationClick = async (notification) => {
        setOpen(false);
        if (!notification.read) {
            try {
                await notificationService.markRead(notification.id);
                setUnreadCount(prev => Math.max(0, prev - 1));
            } catch {
                // Navigation still works even if marking as read fails.
            }
        }
        navigate(notification.linkTarget);
    };

    const handleMarkAllRead = async () => {
        try {
            await notificationService.markAllRead();
            setNotifications(prev => prev.map(n => ({ ...n, read: true })));
            setUnreadCount(0);
        } catch {
            // Keep current state if the request fails.
        }
    };

    return (
        <div ref={containerRef} style={{ position: 'relative', display: 'inline-block' }}>
            <button
                onClick={handleToggle}
                title="Notifications"
                style={{ position: 'relative', cursor: 'pointer', padding: '6px 10px', display: 'flex', alignItems: 'center' }}
            >
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
                    <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
                    <path d="M13.73 21a2 2 0 0 1-3.46 0" />
                </svg>
                {unreadCount > 0 && (
                    <span style={{
                        position: 'absolute', top: '-6px', right: '-6px',
                        backgroundColor: '#dc2626', color: '#fff',
                        borderRadius: '9999px', fontSize: '11px', fontWeight: '700',
                        minWidth: '18px', height: '18px', lineHeight: '18px',
                        textAlign: 'center', padding: '0 4px'
                    }}>
                        {unreadCount > 9 ? '9+' : unreadCount}
                    </span>
                )}
            </button>

            {open && (
                <div style={{
                    position: 'absolute', right: 0, top: 'calc(100% + 8px)', zIndex: 2000,
                    width: '340px', backgroundColor: '#fff', border: '1px solid #e5e7eb',
                    borderRadius: '8px', boxShadow: '0 10px 15px -3px rgba(0,0,0,0.1)'
                }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 16px', borderBottom: '1px solid #eee' }}>
                        <strong style={{ fontSize: '14px', color: '#111827' }}>Notifications</strong>
                        {notifications.some(n => !n.read) && (
                            <button
                                onClick={handleMarkAllRead}
                                style={{ background: 'none', border: 'none', cursor: 'pointer', color: '#2563eb', fontSize: '13px' }}
                            >
                                Mark all as read
                            </button>
                        )}
                    </div>

                    <div style={{ maxHeight: '360px', overflowY: 'auto' }}>
                        {notifications.length === 0 ? (
                            <p style={{ padding: '20px 16px', margin: 0, color: '#888', textAlign: 'center', fontSize: '14px' }}>
                                No notifications yet.
                            </p>
                        ) : (
                            notifications.map(notification => (
                                <div
                                    key={notification.id}
                                    onClick={() => handleNotificationClick(notification)}
                                    style={{
                                        padding: '12px 16px',
                                        borderBottom: '1px solid #f3f4f6',
                                        cursor: 'pointer',
                                        backgroundColor: notification.read ? '#fff' : '#eff6ff'
                                    }}
                                >
                                    <div style={{ fontSize: '14px', color: '#1f2937', fontWeight: notification.read ? '400' : '600' }}>
                                        {notification.title}
                                    </div>
                                    <div style={{ fontSize: '12px', color: '#9ca3af', marginTop: '4px' }}>
                                        {new Date(notification.createdAt).toLocaleString()}
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}

export default NotificationBell;
