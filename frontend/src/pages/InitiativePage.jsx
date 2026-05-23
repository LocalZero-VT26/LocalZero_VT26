import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import authService from '../services/authService';

import InitiativeService from '../services/InitiativeService';

function InitiativePage() {
    const navigate = useNavigate();
    const user = authService.getCurrentUser();

    const [initiatives, setInitiatives] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');

    const [showCreateModal, setShowCreateModal] = useState(false);
    const [joiningId, setJoiningId] = useState(null);
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        location: user?.location || '',
        duration: '',
        category: '',
        visibility: 'Public'
    });

    useEffect(() => {
        let isMounted = true;

        const fetchInitiatives = async () => {
            try {
                setError('');
                const data = await InitiativeService.getAll();
                if (isMounted) {
                    setInitiatives(data);
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

        fetchInitiatives();

        return () => {
            isMounted = false;
        };
    }, []);



    const handleJoinInitiative = async (id) => {

        const currentInitiative = initiatives.find(item => item.id === id);

        const alreadyJoined = currentInitiative?.participants?.some(p => p.id === user.id);

        if (alreadyJoined) {
            alert('You have already joined this initiative!');
            return;
        }

        setJoiningId(id);
        try {
            await InitiativeService.join(id);

            setInitiatives(prevInitiatives =>
                prevInitiatives.map(item => {
                    if (item.id === id) {
                        const updatedParticipants = item.participants ? [...item.participants, user] : [user];
                        return {
                            ...item,
                            participantCount: item.participantCount + 1,
                            participants: updatedParticipants
                        };
                    }
                    return item;
                })
            );

            alert('You have successfully joined this initiative!');
        } catch (err) {
            alert(err.response?.data?.message || 'Could not join this initiative.');
        } finally {
            setJoiningId(null);
        }
    };
    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleCreateSubmit = async (e) => {
        e.preventDefault();
        setError('');

        try {

            const freshInitiative = await InitiativeService.create(formData);

            setInitiatives(prev => [freshInitiative, ...prev]);
            setShowCreateModal(false);

            setFormData({
                title: '',
                description: '',
                location: user?.location || '',
                duration: '',
                category: '',
                visibility: 'Public'
            });
        } catch (err) {
            setError(err.response?.data?.message || 'Failed to establish your initiative.');
        }
    };

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
                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', marginBottom: '32px' }}>
                    <div>
                        <h1 style={{ margin: 0, fontSize: '28px', color: '#111827', fontWeight: '700' }}>Active Initiatives</h1>
                        <p style={{ color: '#6b7280', margin: '6px 0 0 0', fontSize: '15px' }}>Find crowdsourced community work happening near you.</p>
                    </div>
                    <button
                        onClick={() => setShowCreateModal(true)}
                        style={{ padding: '12px 22px', backgroundColor: '#2563eb', color: '#ffffff', border: 'none', borderRadius: '6px', cursor: 'pointer', fontWeight: '600', fontSize: '15px' }}
                    >
                        Launch Initiative
                    </button>
                </div>

                {error && (
                    <div style={{ color: '#b91c1c', backgroundColor: '#fef2f2', padding: '14px', borderRadius: '6px', marginBottom: '24px', border: '1px solid #fee2e2', fontSize: '14px' }}>
                        {error}
                    </div>
                )}

                {loading ? (
                    <div style={{ textAlign: 'center', padding: '60px 0', color: '#4b5563' }}>Loading localized projects...</div>
                ) : initiatives.length === 0 ? (
                    <div style={{ textAlign: 'center', padding: '80px 20px', backgroundColor: '#ffffff', borderRadius: '8px', border: '2px dashed #e5e7eb' }}>
                        <h3 style={{ margin: '0 0 8px 0', color: '#374151' }}>No initiatives established yet</h3>
                        <p style={{ color: '#6b7280', margin: 0 }}>Be the first to step forward and launch a campaign in your zone!</p>
                    </div>
                ) : (

                    <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(340px, 1fr))', gap: '24px' }}>
                        {initiatives.map((initiative) => (
                            <div key={initiative.id} style={{ backgroundColor: '#ffffff', borderRadius: '8px', border: '1px solid #e5e7eb', padding: '24px', display: 'flex', flexDirection: 'column', justifyContent: 'space-between' }}>
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
                                <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', backgroundColor: '#fafafa', margin: '0 -24px -24px -24px', padding: '16px 24px', borderBottomLeftRadius: '8px', borderBottomRightRadius: '8px', borderTop: '1px solid #f3f4f6' }}>
                                    <span style={{ fontSize: '14px', color: '#374151', fontWeight: '500' }}><strong>{initiative.participantCount}</strong> participating</span>
                                    <button
                                        onClick={() => handleJoinInitiative(initiative.id)}
                                        disabled={joiningId === initiative.id}
                                        style={{ padding: '8px 18px', backgroundColor: joiningId === initiative.id ? '#ccc' : '#10b981', color: '#ffffff', border: 'none', borderRadius: '4px', cursor: 'pointer', fontWeight: '600', fontSize: '14px' }}
                                    >
                                        {joiningId === initiative.id ? "Joining..." : "Join Team"}
                                    </button>
                                </div>
                            </div>
                        ))}
                    </div>
                )}
            </div>

            {showCreateModal && (
                <div style={{ position: 'fixed', top: 0, left: 0, right: 0, bottom: 0, backgroundColor: 'rgba(0,0,0,0.4)', display: 'flex', justifyContent: 'center', alignItems: 'center', zIndex: 1000, padding: '20px' }}>
                    <div style={{ backgroundColor: '#ffffff', borderRadius: '#8px', padding: '32px', maxWidth: '550px', width: '100%', boxShadow: '0 20px 25px -5px rgba(0,0,0,0.1)' }}>
                        <h2 style={{ margin: '0 0 6px 0', color: '#111827', fontSize: '22px', fontWeight: '700' }}>Establish New Initiative</h2>
                        <p style={{ color: '#6b7280', fontSize: '14px', margin: '0 0 24px 0' }}>Deploy a crowdsourced campaign within the active community node.</p>

                        <form onSubmit={handleCreateSubmit}>
                            <div style={{ marginBottom: '16px' }}>
                                <label style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Campaign Title</label>
                                <input type="text" name="title" value={formData.title} onChange={handleInputChange} required style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box' }} />
                            </div>
                            <div style={{ marginBottom: '16px' }}>
                                <label style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Detailed Description</label>
                                <textarea name="description" value={formData.description} onChange={handleInputChange} required rows="3" style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box', fontFamily: 'inherit', resize: 'vertical' }}></textarea>
                            </div>
                            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
                                <div>
                                    <label style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Target Location</label>
                                    <input type="text" name="location" value={formData.location} onChange={handleInputChange} required style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box' }} />
                                </div>
                                <div>
                                    <label style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Expected Duration</label>
                                    <input type="text" name="duration" value={formData.duration} onChange={handleInputChange} placeholder="e.g. 3 hours, 2 days" required style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box' }} />
                                </div>
                            </div>
                            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '28px' }}>
                                <div>
                                    <label style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Category Scope</label>
                                    <select name="category" value={formData.category} onChange={handleInputChange} required style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box', backgroundColor: '#fff' }}>
                                        <option value="">Select category...</option>
                                        <option value="Environment">Environment</option>
                                        <option value="Education">Education</option>
                                        <option value="Safety">Safety</option>
                                        <option value="Social Support">Social Support</option>
                                        <option value="Infrastructure">Infrastructure</option>
                                    </select>
                                </div>
                                <div>
                                    <label style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Visibility Policy</label>
                                    <select name="visibility" value={formData.visibility} onChange={handleInputChange} style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box', backgroundColor: '#fff' }}>
                                        <option value="Public">Public</option>
                                        <option value="Internal">Internal</option>
                                    </select>
                                </div>
                            </div>
                            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', borderTop: '1px solid #f3f4f6', paddingTop: '20px' }}>
                                <button type="button" onClick={() => setShowCreateModal(false)} style={{ padding: '10px 18px', backgroundColor: '#ffffff', color: '#4b5563', border: '1px solid #d1d5db', borderRadius: '6px', cursor: 'pointer', fontSize: '14px', fontWeight: '500' }}>Cancel</button>
                                <button type="submit" style={{ padding: '10px 18px', backgroundColor: '#2563eb', color: '#ffffff', border: 'none', borderRadius: '6px', cursor: 'pointer', fontSize: '14px', fontWeight: '600' }}>Submit Broadcast</button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default InitiativePage;