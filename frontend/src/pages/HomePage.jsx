import authService from '../services/authService'
import { useNavigate } from 'react-router-dom'

function HomePage() {
    const user = authService.getCurrentUser();
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await authService.logout();
        } catch (err) {
            console.warn("Backend logout failed, navigating to login anyway", err);
        } finally {
            navigate('/');
        }
    }

    return (
        <div>

            <nav style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 24px', borderBottom: '1px solid #ddd' }}>
                <span>Welcome, <strong>{user?.name}</strong></span>
                <button onClick={handleLogout} style={{cursor: 'pointer', padding: '6px 16px' }}>Logout</button>
            </nav>

            {/* Main Content Area */}
            <div style={{ padding: '40px', textAlign: 'center' }}>
                <h1>LocalZero Dashboard</h1>
                <p>Ready to make a difference in {user?.location || 'your area'}?</p>

                <div style={{ marginTop: '30px' }}>
                    {/* Detta är knapparna */}
                    <button
                        onClick={() => navigate('/initiatives')}
                        style={{
                            padding: '15px 30px',
                            fontSize: '18px',
                            backgroundColor: '#28a745',
                            color: 'white',
                            border: 'none',
                            borderRadius: '5px',
                            cursor: 'pointer',
                            fontWeight: 'bold'
                        }}
                    >
                        Browse & Join Initiatives
                    </button>
                </div>
            </div>
        </div>
    )
}

export default HomePage;