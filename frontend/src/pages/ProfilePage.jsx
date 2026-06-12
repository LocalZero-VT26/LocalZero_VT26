import authService from '../services/authService';
import AppNav from '../components/AppNav';

function ProfilePage() {
    const user = authService.getCurrentUser();

    if (!user) {
        return <p>Inte inloggad.</p>;
    }

    return (
        <div style={{ fontFamily: 'system-ui, sans-serif', backgroundColor: '#f9fafb', minHeight: '100vh' }}>
            <AppNav title="My Profile" />
            <div style={{ maxWidth: '480px', margin: '40px auto', padding: '0 16px' }}>
                <h2>Min profil</h2>
                <p><strong>Namn:</strong> {user.name}</p>
                <p><strong>Email:</strong> {user.email}</p>
                <p><strong>Plats:</strong> {user.location || 'Ej angiven'}</p>
                <div>
                    <strong>Roller:</strong>
                    <ul>
                        {(user.roles || []).map((role) => (
                            <li key={role}>{role}</li>
                        ))}
                    </ul>
                </div>
            </div>
        </div>
    );
}

export default ProfilePage;
