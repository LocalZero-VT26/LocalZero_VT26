import { useState, useEffect, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import InitiativeService from '../services/InitiativeService';
import UpdateCard from '../components/UpdateCard';
import PostUpdateForm from '../components/PostUpdateForm';

function InitiativeDetailPage() {
    const { id } = useParams();
    const navigate = useNavigate();

    const [initiative, setInitiative] = useState(null);
    const [updates, setUpdates] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [membershipPending, setMembershipPending] = useState(false);

    const loadUpdates = useCallback(async () => {
        try {
            const data = await InitiativeService.getUpdatesForInitiative(id);
            setUpdates(data);
        } catch (err) {
            console.error('Failed to load updates', err);
        }
    }, [id]);

    useEffect(() => {
        let isMounted = true;

        const loadInitiative = async () => {
            try {
                setError('');
                const data = await InitiativeService.getById(id);
                if (isMounted) {
                    setInitiative(data);
                }
                await loadUpdates();
            } catch (err) {
                if (isMounted) {
                    setError(err.response?.data?.message || 'Could not load this initiative.');
                }
            } finally {
                if (isMounted) {
                    setLoading(false);
                }
            }
        };

        loadInitiative();

        return () => {
            isMounted = false;
        };
    }, [id, loadUpdates]);

    const handleJoin = async () => {
        setMembershipPending(true);
        try {
            await InitiativeService.join(id);
            setInitiative(prev => ({
                ...prev,
                joinedByCurrentUser: true,
                participantCount: prev.participantCount + 1,
            }));
        } catch (err) {
            alert(err.response?.data?.message || 'Could not join this initiative.');
        } finally {
            setMembershipPending(false);
        }
    };

    const handleLeave = async () => {
        if (!window.confirm('Are you sure you want to leave this initiative?')) return;

        setMembershipPending(true);
        try {
            await InitiativeService.leave(id);
            setInitiative(prev => ({
                ...prev,
                joinedByCurrentUser: false,
                participantCount: Math.max(prev.participantCount - 1, 0),
            }));
        } catch (err) {
            alert(err.response?.data?.message || 'Could not leave this initiative.');
        } finally {
            setMembershipPending(false);
        }
    };

    if (loading) {
        return (
            <div style={{ fontFamily: 'system-ui, sans-serif', minHeight: '100vh', backgroundColor: '#f9fafb' }}>
                <div style={{ textAlign: 'center', padding: '80px 0', color: '#4b5563' }}>Loading initiative...</div>
            </div>
        );
    }

    if (error || !initiative) {
        return (
            <div style={{ fontFamily: 'system-ui, sans-serif', minHeight: '100vh', backgroundColor: '#f9fafb', padding: '40px 24px' }}>
                <div style={{ maxWidth: '700px', margin: '0 auto' }}>
                    <div style={{ color: '#b91c1c', backgroundColor: '#fef2f2', padding: '14px', borderRadius: '6px', border: '1px solid #fee2e2', fontSize: '14px', marginBottom: '16px' }}>
                        {error || 'Initiative not found.'}
                    </div>
                    <button
                        onClick={() => navigate('/initiatives')}
                        style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: '15px', color: '#4b5563', fontWeight: '500' }}
                    >
                        Back to Initiatives
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div style={{ fontFamily: 'system-ui, sans-serif', minHeight: '100vh', backgroundColor: '#f9fafb' }}>
            <nav style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', padding: '14px 28px', borderBottom: '1px solid #e5e7eb', backgroundColor: '#ffffff' }}>
                <button
                    onClick={() => navigate('/initiatives')}
                    style={{ background: 'none', border: 'none', cursor: 'pointer', fontSize: '15px', color: '#4b5563', fontWeight: '500' }}
                >
                    Back to Initiatives
                </button>
            </nav>

            <div style={{ maxWidth: '760px', margin: '0 auto', padding: '40px 24px' }}>
                <div style={{ backgroundColor: '#ffffff', borderRadius: '8px', border: '1px solid #e5e7eb', padding: '28px', marginBottom: '32px' }}>
                    <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '14px' }}>
                        <span style={{ fontSize: '11px', backgroundColor: '#eff6ff', color: '#1e40af', padding: '4px 10px', borderRadius: '9999px', fontWeight: '700', textTransform: 'uppercase' }}>
                            {initiative.category}
                        </span>
                        <span style={{ fontSize: '13px', color: '#9ca3af' }}>{initiative.visibility}</span>
                    </div>

                    <h1 style={{ margin: '0 0 12px 0', fontSize: '26px', color: '#111827', fontWeight: '700' }}>{initiative.title}</h1>
                    <p style={{ fontSize: '15px', color: '#4b5563', margin: '0 0 20px 0', lineHeight: '1.6' }}>{initiative.description}</p>

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '6px', fontSize: '14px', color: '#4b5563', borderTop: '1px solid #f3f4f6', paddingTop: '16px', marginBottom: '20px' }}>
                        <div><strong>Location:</strong> {initiative.location}</div>
                        <div><strong>Duration:</strong> {initiative.duration}</div>
                        <div><strong>Participants:</strong> {initiative.participantCount}</div>
                    </div>

                    {initiative.joinedByCurrentUser ? (
                        <button
                            onClick={handleLeave}
                            disabled={membershipPending}
                            style={{
                                padding: '10px 22px', backgroundColor: '#dc2626', color: '#ffffff', border: 'none',
                                borderRadius: '6px', cursor: membershipPending ? 'default' : 'pointer', fontWeight: '600', fontSize: '14px'
                            }}
                        >
                            {membershipPending ? 'Leaving...' : 'Leave Initiative'}
                        </button>
                    ) : (
                        <button
                            onClick={handleJoin}
                            disabled={membershipPending}
                            style={{
                                padding: '10px 22px', backgroundColor: '#10b981', color: '#ffffff', border: 'none',
                                borderRadius: '6px', cursor: membershipPending ? 'default' : 'pointer', fontWeight: '600', fontSize: '14px'
                            }}
                        >
                            {membershipPending ? 'Joining...' : 'Join Team'}
                        </button>
                    )}

                    {initiative.joinedByCurrentUser && (
                        <PostUpdateForm initiativeId={initiative.id} onSuccess={loadUpdates} />
                    )}
                </div>

                <h2 style={{ margin: '0 0 16px 0', fontSize: '20px', color: '#111827' }}>Updates</h2>
                {updates.length === 0 && <div style={{ color: '#666' }}>No updates yet</div>}
                <div style={{ display: 'flex', flexDirection: 'column', gap: '16px' }}>
                    {updates.map((update) => (
                        <UpdateCard key={update.id} update={update} canInteract={initiative.joinedByCurrentUser} />
                    ))}
                </div>
            </div>
        </div>
    );
}

export default InitiativeDetailPage;
