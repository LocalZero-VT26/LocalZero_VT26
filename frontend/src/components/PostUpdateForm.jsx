import { useState } from 'react';
import InitiativeService from '../services/InitiativeService';

function PostUpdateForm({ initiativeId, onSuccess }) {
    const [content, setContent] = useState('');
    const [imageUrl, setImageUrl] = useState('');
    const [message, setMessage] = useState('');
    const [submitting, setSubmitting] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();
        const trimmedContent = content.trim();
        if (!trimmedContent) return;

        setSubmitting(true);
        setMessage('');

        try {
            await InitiativeService.postUpdate(initiativeId, {
                content: trimmedContent,
                imageUrl: imageUrl.trim() || null,
            });
            setContent('');
            setImageUrl('');
            setMessage('Update posted.');
            onSuccess?.();
        } catch (err) {
            setMessage(err.response?.data?.message || 'Failed to post update.');
        } finally {
            setSubmitting(false);
        }
    };

    const isError = message.toLowerCase().includes('fail');

    return (
        <form onSubmit={handleSubmit} style={{ marginTop: '16px', paddingTop: '16px', borderTop: '1px solid #f3f4f6' }}>
            <p style={{ margin: '0 0 10px 0', fontSize: '13px', fontWeight: '600', color: '#374151' }}>Post update</p>
            <textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="Share progress with the team..."
                required
                rows={2}
                style={{
                    width: '100%',
                    padding: '8px 10px',
                    borderRadius: '6px',
                    border: '1px solid #d1d5db',
                    fontSize: '14px',
                    boxSizing: 'border-box',
                    fontFamily: 'inherit',
                    resize: 'vertical',
                    marginBottom: '8px',
                }}
            />
            <input
                type="url"
                value={imageUrl}
                onChange={(e) => setImageUrl(e.target.value)}
                placeholder="Image URL (optional)"
                style={{
                    width: '100%',
                    padding: '8px 10px',
                    borderRadius: '6px',
                    border: '1px solid #d1d5db',
                    fontSize: '14px',
                    boxSizing: 'border-box',
                    marginBottom: '8px',
                }}
            />
            <button
                type="submit"
                disabled={submitting}
                style={{
                    padding: '8px 14px',
                    backgroundColor: '#2563eb',
                    color: '#fff',
                    border: 'none',
                    borderRadius: '4px',
                    cursor: submitting ? 'default' : 'pointer',
                    fontWeight: '600',
                    fontSize: '13px',
                }}
            >
                {submitting ? 'Posting...' : 'Post update'}
            </button>
            {message && (
                <p style={{
                    marginTop: '8px',
                    fontSize: '13px',
                    color: isError ? '#b91c1c' : '#15803d',
                }}>
                    {message}
                </p>
            )}
        </form>
    );
}

export default PostUpdateForm;
