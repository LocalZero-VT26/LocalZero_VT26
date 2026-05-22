import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import authService from '../services/authService';

function InitiativePage() {
    const navigate = useNavigate();
    const user = authService.getCurrentUser();

    // Sätt loading till true som standard för att slippa sätta det synkront i effekten
    const [initiatives, setInitiatives] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    useEffect(() => {
        let isMounted = true;

        const fetchInitiatives = async () => {
            try {
                setError('');
                const response = await fetch('/api/initiatives', {
                    headers: { 'Content-Type': 'application/json' }
                });

                if (!response.ok) {
                    throw new Error('Failed to retrieve community initiatives.');
                }

                const data = await response.json();
                if (isMounted) {
                    setInitiatives(data);
                }
            } catch (err) {
                if (isMounted) {
                    setError(err.message || 'Something went wrong while loading data.');
                }
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        };

        fetchInitiatives();

        return () => {
            isMounted = false;
        };
    }, []);

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
                <h2>Initiatives Workspace</h2>
                <p>Status: {loading ? "Loading data..." : `Loaded ${initiatives.length} initiatives.`}</p>
                {error && <p style={{ color: 'red' }}>{error}</p>}
            </div>
        </div>
    );
}

export default InitiativePage;