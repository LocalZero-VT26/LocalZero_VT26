import { useState } from "react";
import {useNavigate} from "react-router-dom";
import authService from "../services/authService";

function AuthPage() {
    const [tab, setTab] = useState("login");
    const [loginForm, setLoginForm] = useState({email: '', password: ''});
    const [registerForm, setRegisterForm] = useState({name:'', email:'', password:'', location:''});
    const [error, setError] = useState('');
    const navigate = useNavigate();

    const LOCATIONS = [
        'Norrmalm', 'Haga',  'Södervärn', 'Möllan', 'Västra Hamnen', 'Rosengård', 'Hyllie', 'Limhamn',
    ];

    const handleLoginChange = (e) => {
        setLoginForm({...loginForm, [e.target.name]: e.target.value});
    }

    const handleRegisterChange = (e) => {
        setRegisterForm({...registerForm, [e.target.name]: e.target.value});
    }

    const handleLogin = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await authService.login(loginForm.email, loginForm.password);
            navigate('/home');
        } catch (err) {
            setError(err.response?.data?.message || 'login failed');
        }
    }

    const handleRegister = async (e) => {
        e.preventDefault();
        setError('');
        try {
            await authService.register(registerForm.name, registerForm.location, registerForm.email, registerForm.password);
            navigate('/home');
        } catch (err) {
            setError(err.response?.data?.message || 'register failed');
        }
    }

    const tabStyle = (active) => ({
        padding: '8px 24px',
        cursor: 'pointer',
        border: 'none',
        borderBottom: active ? '2px solid #333' : '2px solid transparent',
        background: 'none',
        fontWeight: active ? 'bold' : 'normal',
    })

    const inputStyle = {
        display: 'block',
        width: '100%',
        padding: '8px',
        margin: '8px 0',
        boxSizing: 'border-box',
        border: '1px solid #ccc',
        borderRadius: '4px',
    }

    return (
        <div style={{maxWidth: '360px', margin: '80px auto', padding: '24px', border: '1px solid #ddd', borderRadius: '8px'}}>
            <div style={{display: 'flex', marginBottom: '24px'}}>
                <button style={tabStyle(tab === 'login')} onClick={() => { setTab('login'); setError('')}}>Login</button>
                <button style={tabStyle(tab === 'register')} onClick={() => {setTab('register'); setError('')}}>Register</button>
            </div>
            {error && <p style={{ color: 'red', marginBottom: '12px' }}>{error}</p>}

            {tab === 'login' && (
                <form onSubmit={handleLogin}>
                    <input style={inputStyle} type="email" name="email" placeholder="Email" value={loginForm.email} onChange={handleLoginChange} required />
                    <input style={inputStyle} type="password" name="password" placeholder="Password" value={loginForm.password} onChange={handleLoginChange} required />
                    <button type="submit" style={{ width: '100%', padding: '10px', marginTop: '8px', cursor: 'pointer' }}>Login</button>
                </form>
            )}

            {tab === 'register' && (
                <form onSubmit={handleRegister}>
                    <input style={inputStyle} type="text" name="name" placeholder="Name" value={registerForm.name} onChange={handleRegisterChange} required />
                    <select style={inputStyle} name="location" value={registerForm.location} onChange={handleRegisterChange} required>
                        <option value="">Select neighborhood</option>
                        {LOCATIONS.map(l => <option key={l} value={l}>{l}</option>)}
                    </select>
                    <input style={inputStyle} type="email" name="email" placeholder="Email" value={registerForm.email} onChange={handleRegisterChange} required />
                    <input style={inputStyle} type="password" name="password" placeholder="Password" value={registerForm.password} onChange={handleRegisterChange} required />
                    <button type="submit" style={{ width: '100%', padding: '10px', marginTop: '8px', cursor: 'pointer' }}>Register</button>
                </form>
            )}
        </div>
    )
}

export default AuthPage;