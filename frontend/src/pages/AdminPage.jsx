import { useState, useEffect, useMemo } from 'react';
import { useNavigate } from 'react-router-dom';
import AppNav from '../components/AppNav';
import authService from '../services/authService';
import userService from '../services/userService';
import { canManageRoles, isAdmin, hasRole } from '../utils/roleUtils';

const actionButtonStyle = {
    padding: '6px 12px',
    border: 'none',
    borderRadius: '4px',
    cursor: 'pointer',
    fontSize: '13px',
    fontWeight: '600',
};

function AdminPage() {
    const navigate = useNavigate();
    const currentUser = authService.getCurrentUser();
    const [users, setUsers] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [actionPending, setActionPending] = useState(null);
    const [locationFilter, setLocationFilter] = useState('');

    const userIsAdmin = isAdmin(currentUser);

    useEffect(() => {
        if (!canManageRoles(currentUser)) {
            navigate('/home');
            return;
        }
        loadUsers();
    }, [currentUser, navigate]);

    const loadUsers = async () => {
        try {
            setError('');
            setLoading(true);
            const data = await userService.getManageableUsers();
            setUsers(data);
        } catch (err) {
            setError(err.response?.data?.message || 'Could not load users.');
        } finally {
            setLoading(false);
        }
    };

    const locations = useMemo(() => {
        const unique = [...new Set(users.map((u) => u.location).filter(Boolean))];
        return unique.sort();
    }, [users]);

    const filteredUsers = useMemo(() => {
        if (!locationFilter) return users;
        return users.filter((u) => u.location === locationFilter);
    }, [users, locationFilter]);

    const handleAssignRole = async (userId, role) => {
        setActionPending(userId);
        setError('');
        try {
            await userService.assignRole(userId, role);
            await loadUsers();
        } catch (err) {
            setError(err.response?.data?.message || 'Could not update role.');
        } finally {
            setActionPending(null);
        }
    };

    if (!canManageRoles(currentUser)) {
        return null;
    }

    return (
        <div style={{ fontFamily: 'system-ui, sans-serif', backgroundColor: '#f9fafb', minHeight: '100vh' }}>
            <AppNav title="User Management" />

            <div style={{ maxWidth: '1000px', margin: '0 auto', padding: '32px 24px' }}>
                <h1 style={{ margin: '0 0 8px 0', fontSize: '26px' }}>Manage Users</h1>
                <p style={{ color: '#6b7280', margin: '0 0 24px 0' }}>
                    {userIsAdmin
                        ? 'As admin you can assign roles to all users across locations.'
                        : `As organizer you can promote residents to organizer in ${currentUser?.location || 'your area'}.`}
                </p>

                {userIsAdmin && locations.length > 0 && (
                    <div style={{ marginBottom: '20px' }}>
                        <label style={{ fontSize: '14px', fontWeight: '600', marginRight: '8px' }}>
                            Filter by location:
                        </label>
                        <select
                            value={locationFilter}
                            onChange={(e) => setLocationFilter(e.target.value)}
                            style={{ padding: '8px 12px', borderRadius: '6px', border: '1px solid #d1d5db' }}
                        >
                            <option value="">All locations</option>
                            {locations.map((loc) => (
                                <option key={loc} value={loc}>{loc}</option>
                            ))}
                        </select>
                    </div>
                )}

                {error && (
                    <div style={{
                        color: '#b91c1c',
                        backgroundColor: '#fef2f2',
                        padding: '12px',
                        borderRadius: '6px',
                        marginBottom: '16px',
                        border: '1px solid #fee2e2',
                    }}>
                        {error}
                    </div>
                )}

                {loading ? (
                    <p style={{ color: '#6b7280' }}>Loading users...</p>
                ) : filteredUsers.length === 0 ? (
                    <p style={{ color: '#6b7280' }}>No users to manage.</p>
                ) : (
                    <div style={{ display: 'flex', flexDirection: 'column', gap: '12px' }}>
                        {filteredUsers.map((user) => {
                            const targetIsOrganizer = hasRole(user, 'ORGANIZER');
                            const targetIsAdmin = hasRole(user, 'ADMIN');
                            const pending = actionPending === user.id;

                            return (
                                <div
                                    key={user.id}
                                    style={{
                                        backgroundColor: '#fff',
                                        border: '1px solid #e5e7eb',
                                        borderRadius: '8px',
                                        padding: '16px 20px',
                                        display: 'flex',
                                        justifyContent: 'space-between',
                                        alignItems: 'center',
                                        flexWrap: 'wrap',
                                        gap: '12px',
                                    }}
                                >
                                    <div>
                                        <div style={{ fontWeight: '600', fontSize: '16px' }}>{user.name}</div>
                                        <div style={{ fontSize: '14px', color: '#6b7280' }}>{user.email}</div>
                                        <div style={{ fontSize: '13px', color: '#9ca3af', marginTop: '4px' }}>
                                            {user.location} · {(user.roles || []).join(', ')}
                                        </div>
                                    </div>
                                    <div style={{ display: 'flex', gap: '8px', flexWrap: 'wrap' }}>
                                        {!targetIsOrganizer && (
                                            <button
                                                disabled={pending}
                                                onClick={() => handleAssignRole(user.id, 'ORGANIZER')}
                                                style={{ ...actionButtonStyle, backgroundColor: '#10b981', color: '#fff' }}
                                            >
                                                Make Organizer
                                            </button>
                                        )}
                                        {userIsAdmin && !targetIsAdmin && (
                                            <button
                                                disabled={pending}
                                                onClick={() => handleAssignRole(user.id, 'ADMIN')}
                                                style={{ ...actionButtonStyle, backgroundColor: '#2563eb', color: '#fff' }}
                                            >
                                                Make Admin
                                            </button>
                                        )}
                                        {userIsAdmin && (targetIsOrganizer || targetIsAdmin) && (
                                            <button
                                                disabled={pending}
                                                onClick={() => handleAssignRole(user.id, 'RESIDENT')}
                                                style={{ ...actionButtonStyle, backgroundColor: '#dc2626', color: '#fff' }}
                                            >
                                                Demote to Resident
                                            </button>
                                        )}
                                    </div>
                                </div>
                            );
                        })}
                    </div>
                )}
            </div>
        </div>
    );
}

export default AdminPage;
