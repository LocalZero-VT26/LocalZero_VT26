import {Route, Routes, Navigate, useLocation} from "react-router-dom";
import AuthPage from './pages/AuthPage'
import HomePage from './pages/HomePage'
import ProfilePage from './pages/ProfilePage'
import InboxPage from './pages/InboxPage'
import authService from './services/authService'

function App() {
  useLocation()
  const user = authService.getCurrentUser()

  return (
      <Routes>
        <Route path="/" element={user ? <Navigate to="/home" replace /> : <AuthPage />} />
        <Route path="/home" element={user ? <HomePage/> : <Navigate to="/" replace />} />
        <Route path="/profile" element={user ? <ProfilePage/> : <Navigate to="/" replace />} />
        <Route path="/inbox" element={user ? <InboxPage/> : <Navigate to="/" replace />} />
      </Routes>
  )
}



export default App
