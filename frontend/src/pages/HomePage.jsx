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
        <div style={{ fontFamily: 'system-ui, sans-serif', backgroundColor: '#f9fafb', minHeight: '100vh' }}>

            <nav style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '12px 24px', borderBottom: '1px solid #ddd', backgroundColor: 'white' }}>
                <span>Welcome, <strong>{user?.name}</strong></span>
                <button onClick={handleLogout} style={{cursor: 'pointer', padding: '6px 16px' }}>Logout</button>
            </nav>

            <div style={{ padding: '40px', textAlign: 'center', maxWidth: '800px', margin: '0 auto' }}>
                <h1>LocalZero Dashboard</h1>
                <p style={{ color: '#666' }}>Ready to make a difference in {user?.location || 'your area'}?</p>

                <div style={{ marginTop: '30px', marginBottom: '50px' }}>
                    <button
                        onClick={() => navigate('/initiatives')}
                        style={{
                            padding: '16px 32px',
                            fontSize: '18px',
                            backgroundColor: '#28a745',
                            color: 'white',
                            border: 'none',
                            borderRadius: '8px',
                            cursor: 'pointer',
                            fontWeight: 'bold',
                            boxShadow: '0 4px 6px rgba(0,0,0,0.1)',
                            transition: 'transform 0.2s'
                        }}
                        onMouseOver={(e) => e.target.style.transform = 'scale(1.05)'}
                        onMouseOut={(e) => e.target.style.transform = 'scale(1)'}
                    >
                        Browse & Join Initiatives
                    </button>
                </div>


                <hr style={{ border: '0', borderTop: '1px solid #eee', margin: '40px 0' }} />

                <div style={{ textAlign: 'left', backgroundColor: 'white', padding: '24px', borderRadius: '12px', border: '1px solid #eee' }}>
                    <h2 style={{ marginTop: 0 }}>Log Eco-Actions</h2>
                    <p>Track your environmental impact below.</p>
                    <EcoActionLogger />
                </div>
            </div>
        </div>
    )
}

export default HomePage;