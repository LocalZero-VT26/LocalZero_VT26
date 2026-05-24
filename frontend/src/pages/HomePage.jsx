import authService from '../services/authService'
import { useNavigate } from 'react-router-dom'
import EcoActionLogger from "../components/EcoActionLogger.jsx";

function HomePage() {
    const user = authService.getCurrentUser();
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await authService.logout();
        } catch (error) {
            console.log('Logout failed:', error);
            localStorage.removeItem('user');
        } finally {
            navigate('/');
        }
    }

    return (
        <div>
            <nav style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 24px', borderBottom: '1px solid #ddd' }}>
                <span>Welcome, <strong>{user?.name}</strong></span>
                <div style={{ display: 'flex', gap: '8px' }}>
                    <button onClick={() => navigate('/inbox')} style={{cursor: 'pointer', padding: '6px 16px' }}>Inbox</button>
                    <button onClick={() => navigate('/profile')} style={{cursor: 'pointer', padding: '6px 16px' }}>Profile</button>
                    <button onClick={handleLogout} style={{cursor: 'pointer', padding: '6px 16px' }}>Logout</button>
                </div>
            </nav>

            <div style={{padding: '24px'}}>
                <h2>Home</h2>
                <p>You can see your initiatives and log eco-actions here.</p>

                <EcoActionLogger />
            </div>
        </div>
    )
}

export default HomePage;