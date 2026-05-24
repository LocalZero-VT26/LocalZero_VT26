import authService from '../services/authService';

function ProfilePage() {
    const user = authService.getCurrentUser();

    if (!user) {
        return <p>Inte inloggad.</p>;
    }

    return (
        <div style={{ maxWidth: '480px', margin: '40px auto', padding: '0 16px' }}>
            <h2>Min profil</h2>
            <p><strong>Namn:</strong> {user.name}</p>
            <p><strong>Email:</strong> {user.email}</p>
            <p><strong>Plats:</strong> {user.location}</p>
            <div>
                <strong>Roller:</strong>
                <ul>
                    {(user.roles || []).map((role) => (
                        <li key={role}>{role}</li>
                    ))}
                </ul>
            </div>
        </div>
    );
}

export default ProfilePage;

