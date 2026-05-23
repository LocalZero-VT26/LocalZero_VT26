import { useState, useEffect} from 'react';
import sustainabilityService from "../services/sustainabilityService.js";


function EcoActionLogger() {
    const [description, setDescription] = useState('');
    const [message, setMessage] = useState('');
    const [actionsList, setActionsList] = useState([]);

    useEffect(() => {
        fetchHistory();
    }, []);

    const fetchHistory = async () => {
        try {
            const history = await sustainabilityService.getHistory();
            setActionsList(history);
        } catch (error) {
            console.error('Failed to fetch history:', error);
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await sustainabilityService.logEcoAction(description);

            setMessage('Eco action logged successfully!');
            setDescription('');
            fetchHistory()
        } catch (error) {
            setMessage('Failed to log eco action. Try again!');
        }
    };

    return (
        <div style={{padding: '20px', border: '1px solid #ccc', margin: '20px', borderRadius: '8px', maxWidth: '400px'}}>
            <h3>Log an Eco-Action</h3>

            <form onSubmit={handleSubmit} style={{ marginBottom: '15px' }}>
                <input
                    type="text"
                    placeholder="T.ex. Bicycled to work."
                    value={description}
                    onChange={(e) => setDescription(e.target.value)}
                    style={{ padding: '8px', marginRight: '10px', width: '60%' }}
                    required/>
                <button type='submit' style={{padding: '8px 16px', cursor: 'pointer'}}>Log Eco-action</button>
            </form>

            {message && <p style={{fontWeight: 'bold', color: 'green'}}>{message}</p>}

            {actionsList.length > 0 && (
                <div style={{ marginTop: '20px', borderTop: '1px solid #eee', paddingTop: '10px' }}>
                    <h4>Your Eco-action history:</h4>
                    <ul style={{ paddingLeft: '20px' }}>
                        {actionsList.map((action, index) => (
                            <li key={index} style={{ marginBottom: '5px' }}>
                                {action.description}
                                <span style={{ fontSize: '0.8em', color: '#666', marginLeft: '10px'}}>
                                    ({new Date(action.timestamp).toLocaleDateString()})
                                </span>
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
}

export default EcoActionLogger;