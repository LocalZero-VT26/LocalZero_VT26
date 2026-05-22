
import { useNavigate } from "react-router-dom";
import authService from '../services/authService';

function InitiativePage() {
    const navigate = useNavigate();
    const user = authService.getCurrentUser();

    return (
        <div style={{ fontFamily: 'system-ui, sans-serif', minHeight: '100vh', backgroundColor: '#f9fafb' }}>
            <nav style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '14px 28px', borderBottom: '1px solid #e5e7eb', backgroundColor: '#ffffff' }}>
                <button
                    onClick={() => navigate('/home')}
                    style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: '15px', color: '#4b5563', fontWeight: '500' }}
                >
                    Back to Dashboard
                </button>
                <span style={{ color: '#374151', fontSize: '14px' }}>Acting as: <strong>{user?.name || 'Local Member'}</strong></span>
            </nav>
            <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '40px 24px' }}>
                <p>Initiative management workspace initialized.</p>
            </div>
        </div>
    );
}

export default InitiativePage;