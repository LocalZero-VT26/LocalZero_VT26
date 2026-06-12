import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import NotificationBell from './NotificationBell.jsx';
import { canManageRoles } from '../utils/roleUtils';

const navButtonStyle = {
    cursor: 'pointer',
    padding: '6px 16px',
    background: 'none',
    border: '1px solid #e5e7eb',
    borderRadius: '6px',
    fontSize: '14px',
};

function AppNav({ title }) {
    const navigate = useNavigate();
    const user = authService.getCurrentUser();

    const handleLogout = async () => {
        try {
            await authService.logout();
        } catch (error) {
            console.error('Logout failed:', error);
            localStorage.removeItem('user');
        } finally {
            navigate('/');
        }
    };

    return (
        <nav style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: '12px 24px',
            borderBottom: '1px solid #ddd',
            backgroundColor: 'white',
        }}>
            <span style={{ fontWeight: '500' }}>
                {title || (
                    <>Welcome, <strong>{user?.name}</strong></>
                )}
            </span>
            <div style={{ display: 'flex', gap: '8px', alignItems: 'center' }}>
                <NotificationBell />
                <button onClick={() => navigate('/home')} style={navButtonStyle}>Dashboard</button>
                <button onClick={() => navigate('/initiatives')} style={navButtonStyle}>Initiatives</button>
                <button onClick={() => navigate('/inbox')} style={navButtonStyle}>Inbox</button>
                {canManageRoles(user) && (
                    <button onClick={() => navigate('/admin')} style={{ ...navButtonStyle, fontWeight: '600' }}>
                        Manage Users
                    </button>
                )}
                <button onClick={() => navigate('/profile')} style={navButtonStyle}>Profile</button>
                <button onClick={handleLogout} style={navButtonStyle}>Logout</button>
            </div>
        </nav>
    );
}

export default AppNav;
