import { useEffect, useState } from 'react';
import InitiativeService from '../services/InitiativeService';
import authService from '../services/authService';

function UpdateCard({ update }) {
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [likeInfo, setLikeInfo] = useState({ count: 0, likedByCurrentUser: false });
    const currentUser = authService.getCurrentUser();

    useEffect(() => {
        fetchComments();
        fetchLikeInfo();
    }, [update]);

    const fetchComments = async () => {
        try {
            const data = await InitiativeService.getComments(update.id);
            setComments(data);
        } catch (err) {
            console.error('Failed to get comments', err);
        }
    };

    const fetchLikeInfo = async () => {
        try {
            const data = await InitiativeService.getLikeInfo(update.id);
            setLikeInfo(data);
        } catch (err) {
            console.error('Failed to get like info', err);
        }
    };

    const handleAddComment = async (e) => {
        e.preventDefault();
        if (!newComment.trim()) return;
        try {
            const saved = await InitiativeService.postComment(update.id, { content: newComment });
            setComments(prev => [...prev, saved]);
            setNewComment('');
        } catch (err) {
            console.error('Failed to post comment', err);
        }
    };

    const handleToggleLike = async () => {
        try {
            const res = await InitiativeService.toggleLike(update.id);
            setLikeInfo(res);
        } catch (err) {
            console.error('Failed to toggle like', err);
        }
    };

    return (
        <div style={{ border: '1px solid #ddd', padding: '12px', marginBottom: '12px', borderRadius: '6px' }}>
            <div style={{ marginBottom: '8px' }}>
                <strong>{update.authorName}</strong> <small style={{ color: '#666' }}>{new Date(update.createdAt).toLocaleString()}</small>
            </div>
            <div style={{ marginBottom: '8px' }}>{update.content}</div>
            {update.imageUrl && <img src={update.imageUrl} alt="" style={{ maxWidth: '100%', marginBottom: '8px' }} />}
            <div style={{ display: 'flex', gap: '8px', alignItems: 'center', marginBottom: '8px' }}>
                <button onClick={handleToggleLike} style={{ cursor: 'pointer' }}>
                    {likeInfo.likedByCurrentUser ? 'Unlike' : 'Like'} ({likeInfo.count})
                </button>
            </div>

            <div>
                <h4 style={{ margin: '8px 0' }}>Comments</h4>
                {comments.length === 0 && <div style={{ color: '#666' }}>No comments yet</div>}
                {comments.map(c => (
                    <div key={c.id} style={{ padding: '8px 0', borderBottom: '1px solid #f0f0f0' }}>
                        <div style={{ fontSize: '14px' }}><strong>{c.authorName}</strong> <small style={{ color: '#666' }}>{new Date(c.createdAt).toLocaleString()}</small></div>
                        <div>{c.content}</div>
                    </div>
                ))}

                {currentUser && (
                    <form onSubmit={handleAddComment} style={{ marginTop: '8px' }}>
                        <textarea value={newComment} onChange={e => setNewComment(e.target.value)} rows={3} style={{ width: '100%', padding: '8px' }} />
                        <button type="submit" style={{ marginTop: '6px', cursor: 'pointer' }}>Post comment</button>
                    </form>
                )}
            </div>
        </div>
    );
}

export default UpdateCard;
