import sustainabilityService from "../services/sustainabilityService.js";


function EcoActionLogger() {

    const [description, setDescription] = useState('');
    const [message, setMessage] = useState('');

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            await sustainabilityService.logEcoAction(description);

            setMessage('Eco action logged successfully!');
            setDescription('');
        } catch (error) {
            setMessage('Failed to log eco action. Try again!');
        }
    };

    return (
        <div
            style={{padding: '20px', border: '1px solid #ccc', margin: '20px', borderRadius: '8px', maxWidth: '400px'}}>
            <h3>Logga en Eco-Action</h3>

            <form onSubmit={handleSubmit}>
                <input type="text" placeholder="T.ex. Cyklade till jobbet." value={description}/>
                <button type='submit' style={{padding: '8px 16px', cursor: 'pointer'}}>Logga handling</button>
            </form>

            {message && <p style={{marginTop: '10px', fontWeight: 'bold'}}>{message}</p>}
        </div>
    );
}

export default EcoActionLogger;