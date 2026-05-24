import { useCallback, useEffect, useRef, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import authService from '../services/authService';
import sustainabilityService from "../services/sustainabilityService.js";


function EcoActionLogger() {
    const [description, setDescription] = useState('');
    const [message, setMessage] = useState('');
    const textareaRef = useRef(null);
    const navigate = useNavigate();

    const currentUser = authService.getCurrentUser();

    useEffect(() => {
        if (textareaRef.current) {
            textareaRef.current.style.height = 'auto';
            textareaRef.current.style.height = `${textareaRef.current.scrollHeight}px`;
        }
    }, [description]);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!currentUser?.token) {
            setMessage('Please log in before logging an eco-action.');
            navigate('/');
            return;
        }

        try {
            const trimmedDescription = description.trim();
            await sustainabilityService.logEcoAction(trimmedDescription);

            setMessage('Eco action logged successfully!');
            setDescription('');
            
            window.location.reload();
        } catch (error) {
            if (error.response?.status === 401) {
                setMessage('Your session expired. Please log in again.');
                navigate('/');
                return;
            }

            setMessage('Failed to log eco action. Try again!');
        }
    };

    return (
        <div style={{ maxWidth: '400px' }}>
            {!currentUser?.token && (
                <p style={{fontWeight: 'bold', color: 'crimson'}}>
                    Please log in to view and save eco-actions.
                </p>
            )}

            <form onSubmit={handleSubmit} style={{ marginBottom: '15px' }}>
                <textarea
                    ref={textareaRef}
                    placeholder="For example: Bicycled to work."
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    rows={1}
                    style={{
                        padding: '8px',
                        marginRight: '10px',
                        width: '100%',
                        minHeight: '44px',
                        resize: 'none',
                        overflow: 'hidden',
                        boxSizing: 'border-box',
                        marginBottom: '10px'
                    }}
                    required
                />
                <button type='submit' style={{padding: '8px 16px', cursor: 'pointer'}}>Log Eco-action</button>
            </form>

            {message && <p style={{fontWeight: 'bold', color: message.toLowerCase().includes('failed') || message.toLowerCase().includes('expired') || message.toLowerCase().includes('log in') ? 'crimson' : 'green'}}>{message}</p>}
        </div>
    );
}

export default EcoActionLogger;