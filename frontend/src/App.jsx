import {Routes, Route, Navigate, useLocation} from 'react-router-dom';
import AuthPage from './pages/AuthPage';
import HomePage from './pages/HomePage';
import InitiativePage from './pages/InitiativePage';
import authService from "./services/authService.js";

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
        </Routes>
    );
}

export default App;