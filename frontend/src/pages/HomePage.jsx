import { useEffect, useState } from 'react'
import authService from '../services/authService'
import { useNavigate } from 'react-router-dom'
import EcoActionLogger from "../components/EcoActionLogger.jsx";
import InitiativeService from '../services/InitiativeService'
import UpdateCard from '../components/UpdateCard'

function HomePage() {
    const user = authService.getCurrentUser();
    const navigate = useNavigate();
    const [updates, setUpdates] = useState([]);
    const [updatesError, setUpdatesError] = useState('');

    useEffect(() => {
        const loadUpdates = async () => {
            try {
                const data = await InitiativeService.getUpdates();
                setUpdates(data);
            } catch (error) {
                console.error('Failed to load updates:', error);
                setUpdatesError('Could not load updates');
            }
        };

        loadUpdates();
    }, []);

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
                    <button onClick={() => navigate('/chatrooms')} style={{ cursor: 'pointer', padding: '6px 16px' }}>Messages</button>
                    <button onClick={handleLogout} style={{ cursor: 'pointer', padding: '6px 16px' }}>Logout</button>
                    <button onClick={() => navigate('/profile')} style={{cursor: 'pointer', padding: '6px 16px' }}>Profile</button>
                </div>
            </nav>

            <div style={{padding: '24px'}}>
                <h2>Home</h2>
                <p>You can see your initiatives and log eco-actions here.</p>

                <EcoActionLogger />

                <div style={{ marginTop: '24px' }}>
                    <h3>Activity Feed</h3>
                    {updatesError && <div style={{ color: '#b00020' }}>{updatesError}</div>}
                    {!updatesError && updates.length === 0 && <div style={{ color: '#666' }}>No updates yet</div>}
                    {updates.map((update) => (
                        <UpdateCard key={update.id} update={update} />
                    ))}
                </div>
            </div>
        </div>
    )
}

export default HomePage;