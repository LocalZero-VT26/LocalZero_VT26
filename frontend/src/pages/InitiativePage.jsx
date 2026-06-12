import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import authService from '../services/authService';
import InitiativeService from '../services/InitiativeService';
import InitiativeForm from '../components/InitiativeForm';
import AppNav from '../components/AppNav';

function InitiativePage() {
    const navigate = useNavigate();
    const user = authService.getCurrentUser();

    const [initiatives, setInitiatives] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const [joinedInitiatives, setJoinedInitiatives] = useState([]);

    const [showCreateModal, setShowCreateModal] = useState(false);
    const [joiningId, setJoiningId] = useState(null);

    useEffect(() => {
        let isMounted = true;

        const fetchInitiatives = async () => {
            try {
                setError('');
                const data = await InitiativeService.getAll();
                if (isMounted) {
                    setInitiatives(data);

                    const alreadyJoinedIds = data
                        .filter(init => init.joinedByCurrentUser)
                        .map(init => init.id);

                    setJoinedInitiatives(alreadyJoinedIds);

                }
            } catch (err) {
                if (isMounted) {
                    setError(err.response?.data?.message || 'Something went wrong while loading data.');
                }
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        };

        if (user?.id) {
            fetchInitiatives();
        }

        return () => {
            isMounted = false;
        };
    }, [user?.id]);

    const handleJoinInitiative = async (id) => {
        if (joinedInitiatives.includes(id)) return;

        setJoiningId(id);
        try {
            await InitiativeService.join(id);
            setJoinedInitiatives(prev => [...prev, id]);
            setInitiatives(prevInitiatives =>
                prevInitiatives.map(item =>
                    item.id === id ? { ...item, participantCount: item.participantCount + 1 } : item
                )
            );
        } catch (err) {
            alert(err.response?.data?.message || 'Could not join this initiative.');
        } finally {
            setJoiningId(null);
        }
    };

    const handleCreateSubmit = async (formData) => { // Modified to accept formData as argument
        setError('');
        try {
            const freshInitiative = await InitiativeService.create(formData);
            setInitiatives(prev => [freshInitiative, ...prev]);
            if (freshInitiative.joinedByCurrentUser) {
                setJoinedInitiatives(prev => [...prev, freshInitiative.id]);
            }
            setShowCreateModal(false);
            // Reset form data is handled internally by InitiativeForm if needed or when it's unmounted/remounted
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to establish your initiative.');
        }
    };

    return (
        <div style={{ fontFamily: 'system-ui, sans-serif', minHeight: '100vh', backgroundColor: '#f9fafb' }}>
            <AppNav title="Active Initiatives" />

            <div style={{ maxWidth: '1200px', margin: '0 auto', padding: '40px 24px' }}>
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '32px' }}>
                    <div>
                        <h1 style={{ margin: 0, fontSize: '28px', color: '#111827', fontWeight: '700' }}>Active Initiatives</h1>
                        <p style={{ color: '#6b7280', margin: '6px 0 0 0', fontSize: '15px' }}>Find crowdsourced community work happening near you.</p>
                    </div>

                    {(user?.roles?.includes('ORGANIZER') || user?.roles?.includes('ADMIN')) && (
                        <button
                            onClick={() => setShowCreateModal(true)}
                            style={{ padding: '12px 22px', backgroundColor: '#2563eb', color:
                                    '#ffffff', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: '600', fontSize: '15px'
                            }}
                        >
                            Launch Initiative
                        </button>
                    )}
                </div>

                {error && (
                    <div style={{ color: '#b91c1c', backgroundColor: '#fef2f2', padding: '14px', borderRadius: '6px', marginBottom: '24px', border: '1px solid #fee2e2', fontSize: '14px' }}>
                        {error}
                    </div>
                )}

                {loading ? (
                    <div style={{ textAlign: 'center', padding: '60px 0', color: '#4b5563' }}>Loading localized projects...</div>
                ) : (
                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '24px' }}>
                        {initiatives.map((initiative) => (
                            <div
                                key={initiative.id}
                                onClick={() => navigate(`/initiatives/${initiative.id}`)}
                                style={{ backgroundColor: '#ffffff', borderRadius: '8px', border: '1px solid #e5e7eb', padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between', cursor: 'pointer' }}
                            >
                                <div>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '12px' }}>
                                        <span style={{ fontSize: '11px', backgroundColor: '#eff6ff', color: '#1e40af', padding: '4px 10px', borderRadius: '9999px', fontWeight: '700', textTransform: 'uppercase' }}>
                                            {initiative.category}
                                        </span>
                                        <span style={{ fontSize: '13px', color: '#9ca3af' }}>{initiative.visibility}</span>
                                    </div>
                                    <h3 style={{ margin: '0 0 10px 0', color: '#1f2937', fontSize: '18px', fontWeight: '600' }}>{initiative.title}</h3>
                                    <p style={{ fontSize: '14px', color: '#4b5563', margin: '0 0 20px 0', lineHeight: '1.5' }}>{initiative.description}</p>
                                    <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', fontSize: '13px', color: '#4b5563', borderTop: '1px solid #f3f4f6', paddingTop: '14px', marginBottom: '20px' }}>
                                        <div><strong>Location:</strong> {initiative.location}</div>
                                        <div><strong>Duration:</strong> {initiative.duration}</div>
                                    </div>
                                </div>
                                <div style={{ backgroundColor: '#fafafa', margin: '0 -24px -24px -24px', padding: '16px 24px', borderBottomLeftRadius: '8px', borderBottomRightRadius: '8px', borderTop: '1px solid #f3f4f6' }}>
                                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                                        <span style={{ fontSize: '14px', color: '#374151', fontWeight: '500' }}><strong>{initiative.participantCount}</strong> participating</span>
                                        <button
                                            onClick={(e) => {
                                                e.stopPropagation();
                                                handleJoinInitiative(initiative.id);
                                            }}
                                            disabled={joiningId === initiative.id || joinedInitiatives.includes(initiative.id)}
                                            style={{
                                                padding: '8px 18px',
                                                backgroundColor: (joinedInitiatives.includes(initiative.id)) ? '#ccc' : '#10b981',
                                                color: '#ffffff', border: 'none', borderRadius: '4px', cursor: (joinedInitiatives.includes(initiative.id)) ? 'default' : 'pointer', fontWeight: '600', fontSize: '14px'
                                            }}
                                        >
                                            {joiningId === initiative.id ? "Joining..." : joinedInitiatives.includes(initiative.id) ? "Joined" : "Join Team"}
                                        </button>
                                    </div>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {showCreateModal && (
                <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.4)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000, padding: '20px' }}>
                    <div style={{ backgroundColor: '#ffffff', borderRadius: '8px', padding: '32px', maxWidth: '550px', width: '100%', boxShadow: '0 20px 25px -5px rgba(0,0,0,0.1)' }}>
                        <h2 style={{ margin: '0 0 6px 0', color: '#111827', fontSize: '22px', fontWeight: '700' }}>Establish New Initiative</h2>
                        <InitiativeForm
                            onSubmit={handleCreateSubmit}
                            onCancel={() => setShowCreateModal(false)}
                            initialLocation={user?.location || ''}
                            error={error}
                        />
                    </div>
                </div>
            )}
        </div>
    );
}

export default InitiativePage;