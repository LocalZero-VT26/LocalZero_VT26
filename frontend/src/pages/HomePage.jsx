import authService from '../services/authService'
import { useNavigate } from 'react-router-dom'

function HomePage() {
    const user = authService.getCurrentUser();
    const navigate = useNavigate();

    const handleLogout = async () => {
        await authService.logout();
        navigate('/');
    }

    return (
        <div>
            <nav style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 24px', borderBottom: '1px solid #ddd' }}>
                <span>Welcome, <strong>{user?.name}</strong></span>
                <button onClick={handleLogout} style={{ cursor: 'pointer', padding: '6px 16px' }}>Logout</button>
            </nav>
            <div style={{ padding: '24px' }}>
                <button onClick={() => navigate('/chatrooms')} style={{ cursor: 'pointer', padding: '6px 16px' }}>Messages</button>
            </div>
        </div>
    )
}

export default HomePage;