import React, { useState, useEffect } from 'react';

function InitiativeForm({ onSubmit, onCancel, initialLocation = '', error }) {
    const [formData, setFormData] = useState({
        title: '',
        description: '',
        location: initialLocation,
        duration: '',
        category: '',
        visibility: 'public'
    });

    useEffect(() => {
        setFormData(prev => ({ ...prev, location: initialLocation }));
    }, [initialLocation]);

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(formData);
    };

    return (
        <form onSubmit={handleSubmit}>
            <div style={{ marginBottom: '16px' }}>
                <label htmlFor="title" style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Campaign Title</label>
                <input id="title" type="text" name="title" value={formData.title} onChange={handleInputChange} required style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box' }} />
            </div>
            <div style={{ marginBottom: '16px' }}>
                <label htmlFor="description" style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Detailed Description</label>
                <textarea id="description" name="description" value={formData.description} onChange={handleInputChange} required rows="3" style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box', fontFamily: 'inherit', resize: 'vertical' }}></textarea>
            </div>
            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '16px', marginBottom: '16px' }}>
                <div>
                    <label htmlFor="location" style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Target Location</label>
                    <input id="location" type="text" name="location" value={formData.location} onChange={handleInputChange} required style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box' }} />
                </div>
                <div>
                    <label htmlFor="duration" style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Expected Duration</label>
                    <input id="duration" type="text" name="duration" value={formData.duration} onChange={handleInputChange} placeholder="e.g. 3 hours, 2 days" required style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box' }} />
                </div>
            </div>
            <div style={{ marginBottom: '16px' }}>
                <label htmlFor="category" style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Category</label>
                <input id="category" type="text" name="category" value={formData.category} onChange={handleInputChange} required style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box' }} />
            </div>
            <div style={{ marginBottom: '16px' }}>
                <label htmlFor="visibility" style={{ display: 'block', fontSize: '13px', fontWeight: '600', color: '#374151', marginBottom: '6px' }}>Visibility</label>
                <select id="visibility" name="visibility" value={formData.visibility} onChange={handleInputChange} required style={{ width: '100%', padding: '10px 14px', borderRadius: '6px', border: '1px solid #d1d5db', fontSize: '14px', boxSizing: 'border-box' }}>
                    <option value="public">Public</option>
                    <option value="neighborhood-specific">Neighborhood</option>
                </select>
            </div>

            {error && (
                <div style={{ color: '#b91c1c', backgroundColor: '#fef2f2', padding: '14px', borderRadius: '6px', marginBottom: '24px', border: '1px solid #fee2e2', fontSize: '14px' }}>
                    {error}
                </div>
            )}

            <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '12px', borderTop: '1px solid #f3f4f6', paddingTop: '20px' }}>
                <button type="button" onClick={onCancel} style={{ padding: '10px 18px', backgroundColor: '#ffffff', color: '#4b5563', border: '1px solid #d1d5db', borderRadius: '6px', cursor: 'pointer', fontSize: '14px', fontWeight: '500' }}>Cancel</button>
                <button type="submit" style={{ padding: '10px 18px', backgroundColor: '#2563eb', color: '#ffffff', border: 'none', borderRadius: '6px', cursor: 'pointer', fontSize: '14px', fontWeight: '600' }}>Submit Broadcast</button>
            </div>
        </form>
    );
}

export default InitiativeForm;