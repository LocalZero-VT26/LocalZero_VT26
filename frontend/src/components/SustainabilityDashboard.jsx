import { useEffect, useState } from 'react';
import sustainabilityService from '../services/sustainabilityService';
import authService from '../services/authService';

function SustainabilityDashboard() {
    const [personalStats, setPersonalStats] = useState([]);
    const [communityStats, setCommunityStats] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const currentUser = authService.getCurrentUser();

    useEffect(() => {
        if (!currentUser?.token) return;

        const fetchData = async () => {
            try {
                const [personalRes, communityRes] = await Promise.all([
                    sustainabilityService.getHistory(),
                    sustainabilityService.getCommunityStats()
                ]);
                setPersonalStats(personalRes);
                setCommunityStats(communityRes);
            } catch (err) {
                console.error("Failed to load dashboard data:", err);
                setError('Failed to load dashboard data.');
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [currentUser?.token]);

    if (!currentUser?.token) {
        return <div>Vänligen logga in för att se dashboarden.</div>;
    }

    if (loading) {
        return <div>Laddar dashboard...</div>;
    }

    if (error) {
        return <div style={{ color: 'red' }}>{error}</div>;
    }

    return (
        <div style={{ display: 'flex', gap: '20px', flexWrap: 'wrap', marginTop: '20px' }}>
            <div style={{ flex: '1', minWidth: '300px', backgroundColor: '#f0fdf4', padding: '20px', borderRadius: '8px', border: '1px solid #bbf7d0' }}>
                <h3 style={{ color: '#166534', marginTop: 0 }}>Mina Eco-Actions</h3>
                {personalStats.length === 0 ? (
                    <p style={{ color: '#15803d' }}>Du har inte loggat några eco-actions ännu.</p>
                ) : (
                    <ul style={{ paddingLeft: '20px', color: '#166534' }}>
                        {personalStats.map((stat, idx) => (
                            <li key={idx} style={{ marginBottom: '8px' }}>
                                <strong>{stat.description}</strong>
                                <div style={{ fontSize: '0.85em', opacity: 0.8 }}>
                                    {new Date(stat.timestamp).toLocaleString()}
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>

            <div style={{ flex: '1', minWidth: '300px', backgroundColor: '#f8fafc', padding: '20px', borderRadius: '8px', border: '1px solid #e2e8f0' }}>
                <h3 style={{ color: '#0f172a', marginTop: 0 }}>Community Stats (Mina Initiativ)</h3>
                <p style={{ fontSize: '0.9em', color: '#475569' }}>Eco-actions från andra i dina initiativ.</p>
                {communityStats.length === 0 ? (
                    <p style={{ color: '#334155' }}>Inga uppdateringar från din community ännu.</p>
                ) : (
                    <ul style={{ paddingLeft: '20px', color: '#0f172a' }}>
                        {communityStats.map((stat, idx) => (
                            <li key={idx} style={{ marginBottom: '12px', borderBottom: '1px solid #e2e8f0', paddingBottom: '8px' }}>
                                <div style={{ fontWeight: 'bold' }}>{stat.userName}</div>
                                <div>{stat.description}</div>
                                <div style={{ fontSize: '0.85em', color: '#64748b' }}>
                                    {new Date(stat.timestamp).toLocaleString()}
                                </div>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
}

export default SustainabilityDashboard;
