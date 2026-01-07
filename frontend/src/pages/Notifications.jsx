import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Notifications.css';
import { apiFetch } from '../services/apiService';

function Notifications() {
    const [notifications, setNotifications] = useState([]);
    const [loading, setLoading] = useState(false);
    const [filter, setFilter] = useState('all'); // all, unread, read
    const navigate = useNavigate();

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        const token = localStorage.getItem('token');
        if (!userId || !token) {
            navigate('/login');
            return;
        }
        fetchNotifications(userId);
    }, [navigate]);

    const fetchNotifications = async (userId) => {
        setLoading(true);
        try {
            const data = await apiFetch(`/notifications/user/${userId}`, { method: 'GET' });
            setNotifications(data);
        } catch (err) {
            console.error('Erreur chargement notifications:', err);
        } finally {
            setLoading(false);
        }
    };

    const markAsRead = async (notifId) => {
        try {
            await apiFetch(`/notifications/${notifId}/read`, { method: 'PUT' });
            setNotifications(prev => 
                prev.map(n => n.idNotif === notifId ? { ...n, lue: true } : n)
            );
        } catch (err) {
            console.error('Erreur marquage comme lu:', err);
        }
    };

    const markAllAsRead = async () => {
        const userId = localStorage.getItem('userId');
        try {
            await apiFetch(`/notifications/user/${userId}/read-all`, { method: 'PUT' });
            setNotifications(prev => prev.map(n => ({ ...n, lue: true })));
        } catch (err) {
            console.error('Erreur marquage tout comme lu:', err);
        }
    };

    const getNotificationIcon = (type) => {
        switch(type) {
            case 'RAPPEL_J30': return '📅';
            case 'RAPPEL_J5': return '⏰';
            case 'RETARD': return '⚠️';
            case 'RETOUR': return '✅';
            case 'RAPPEL': return '🔔';
            default: return '📢';
        }
    };

    const getNotificationTitle = (type) => {
        switch(type) {
            case 'RAPPEL_J30': return 'Rappel : Retour dans 30 jours';
            case 'RAPPEL_J5': return 'Rappel : Retour dans 5 jours';
            case 'RETARD': return 'Livre en retard';
            case 'RETOUR': return 'Livre retourné avec succès';
            case 'RAPPEL': return 'Rappel de retour';
            default: return 'Notification';
        }
    };

    const getNotificationClass = (type) => {
        switch(type) {
            case 'RETARD': return 'error';
            case 'RAPPEL_J5': return 'warning';
            case 'RETOUR': return 'success';
            default: return 'info';
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR', {
            day: '2-digit',
            month: 'long',
            year: 'numeric',
            hour: '2-digit',
            minute: '2-digit'
        });
    };

    const filteredNotifications = notifications.filter(notif => {
        if (filter === 'unread') return !notif.lue;
        if (filter === 'read') return notif.lue;
        return true;
    });

    const unreadCount = notifications.filter(n => !n.lue).length;

    return (
        <div className="notifications-container">
            <div className="notifications-header">
                <h1>Mes Notifications</h1>
                {unreadCount > 0 && (
                    <button className="btn-mark-all" onClick={markAllAsRead}>
                        Tout marquer comme lu ({unreadCount})
                    </button>
                )}
            </div>

            <div className="notifications-filters">
                <button 
                    className={`filter-btn ${filter === 'all' ? 'active' : ''}`}
                    onClick={() => setFilter('all')}
                >
                    Toutes ({notifications.length})
                </button>
                <button 
                    className={`filter-btn ${filter === 'unread' ? 'active' : ''}`}
                    onClick={() => setFilter('unread')}
                >
                    Non lues ({unreadCount})
                </button>
                <button 
                    className={`filter-btn ${filter === 'read' ? 'active' : ''}`}
                    onClick={() => setFilter('read')}
                >
                    Lues ({notifications.length - unreadCount})
                </button>
            </div>

            {loading ? (
                <div className="loading">Chargement...</div>
            ) : filteredNotifications.length === 0 ? (
                <div className="no-notifications">
                    <p>Aucune notification à afficher</p>
                </div>
            ) : (
                <div className="notifications-list">
                    {filteredNotifications.map(notif => (
                        <div 
                            key={notif.idNotif} 
                            className={`notification-card ${getNotificationClass(notif.type)} ${notif.lue ? 'read' : 'unread'}`}
                        >
                            <div className="notif-icon-large">
                                {getNotificationIcon(notif.type)}
                            </div>
                            <div className="notif-body">
                                <h3>{getNotificationTitle(notif.type)}</h3>
                                <p className="notif-date">{formatDate(notif.dateEnvoi)}</p>
                                <p className="notif-emprunt">Emprunt #{notif.idEmprunt}</p>
                            </div>
                            {!notif.lue && (
                                <button 
                                    className="btn-mark-read"
                                    onClick={() => markAsRead(notif.idNotif)}
                                    title="Marquer comme lu"
                                >
                                    ✓
                                </button>
                            )}
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default Notifications;
