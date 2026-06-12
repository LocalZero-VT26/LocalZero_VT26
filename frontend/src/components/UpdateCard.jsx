import { useEffect, useState } from 'react';
import InitiativeService from '../services/InitiativeService';
import authService from '../services/authService';

function UpdateCard({ update, canInteract = false }) {
    const [comments, setComments] = useState([]);
    const [newComment, setNewComment] = useState('');
    const [likeInfo, setLikeInfo] = useState({ count: 0, likedByCurrentUser: false });
    const currentUser = authService.getCurrentUser();

    useEffect(() => {
        let isMounted = true;

        const loadData = async () => {
            try {
                const commentsData = await InitiativeService.getComments(update.id);
                if (isMounted) setComments(commentsData);
            } catch (err) {
                console.error('Failed to get comments', err);
            }

            try {
                const likeData = await InitiativeService.getLikeInfo(update.id);
                if (isMounted) setLikeInfo(likeData);
            } catch (err) {
                console.error('Failed to get like info', err);
            }
        };

        loadData();

        return () => {
            isMounted = false;
        };
    }, [update.id]);

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
                {canInteract ? (
                    <button onClick={handleToggleLike} style={{ cursor: 'pointer' }}>
                        {likeInfo.likedByCurrentUser ? 'Unlike' : 'Like'} ({likeInfo.count})
                    </button>
                ) : (
                    <span style={{ color: '#666', fontSize: '14px' }}>{likeInfo.count} likes</span>
                )}
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

                {canInteract && currentUser && (
                    <form onSubmit={handleAddComment} style={{ marginTop: '8px' }}>
                        <textarea value={newComment} onChange={e => setNewComment(e.target.value)} rows={3} style={{ width: '100%', padding: '8px' }} />
                        <button type="submit" style={{ marginTop: '6px', cursor: 'pointer' }}>Post comment</button>
                    </form>
                )}
                {!canInteract && currentUser && (
                    <p style={{ marginTop: '8px', color: '#666', fontSize: '14px' }}>
                        Join this initiative to like and comment.
                    </p>
                )}
            </div>
        </div>
    );
}

export default UpdateCard;
