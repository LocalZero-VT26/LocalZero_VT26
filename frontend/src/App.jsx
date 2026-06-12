import {Routes, Route, Navigate, useLocation} from 'react-router-dom';
import AuthPage from './pages/AuthPage';
import HomePage from './pages/HomePage';
import InitiativePage from './pages/InitiativePage';
import InitiativeDetailPage from './pages/InitiativeDetailPage';
import ProfilePage from './pages/ProfilePage';
import InboxPage from './pages/InboxPage';
import AdminPage from './pages/AdminPage';
import authService from './services/authService';
import { canManageRoles } from './utils/roleUtils';

function App() {
    useLocation()
    const user = authService.getCurrentUser();

    return (
        <Routes>
            <Route path="/" element={user ? <Navigate to="/home" replace /> : <AuthPage />} />
            <Route path="/home" element={user ? <HomePage/> : <Navigate to="/" replace />} />
            <Route
                path="/initiatives"
                element={user ? <InitiativePage /> : <Navigate to="/" replace />}
            />
            <Route
                path="/initiatives/:id"
                element={user ? <InitiativeDetailPage /> : <Navigate to="/" replace />}
            />
            <Route path="/profile" element={user ? <ProfilePage/> : <Navigate to="/" replace />} />
            <Route path="/inbox" element={user ? <InboxPage/> : <Navigate to="/" replace />} />
            <Route
                path="/admin"
                element={user && canManageRoles(user) ? <AdminPage /> : <Navigate to="/home" replace />}
            />
        </Routes>
    );
}

export default App;