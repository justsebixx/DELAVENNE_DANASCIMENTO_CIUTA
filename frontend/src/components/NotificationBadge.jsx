import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import './NotificationBadge.css';
import { apiFetch } from '../services/apiService';

function NotificationBadge() {
    const [count, setCount] = useState(0);
    const [showDropdown, setShowDropdown] = useState(false);
    const [notifications, setNotifications] = useState([]);

    useEffect(() => {
        const userId = localStorage.getItem('userId');
        const token = localStorage.getItem('token');
        if (userId && token) {
            fetchUnreadCount(userId);
            fetchRecentNotifications(userId);
            
            // Rafraîchir toutes les 30 secondes
            const interval = setInterval(() => {
                fetchUnreadCount(userId);
                fetchRecentNotifications(userId);
            }, 30000);

            return () => clearInterval(interval);
        }
    }, []);

    const fetchUnreadCount = async (userId) => {
        try {
            const data = await apiFetch(`/notifications/user/${userId}/count`, { method: 'GET' });
            setCount(data.count);
        } catch (err) {
            console.error('Erreur chargement compteur:', err);
        }
    };

    const fetchRecentNotifications = async (userId) => {
        try {
            const data = await apiFetch(`/notifications/user/${userId}/unread`, { method: 'GET' });
            setNotifications(data.slice(0, 5)); // 5 dernières
        } catch (err) {
            console.error('Erreur chargement notifications:', err);
        }
    };

    const getNotificationIcon = (type) => {
        switch(type) {
            case 'RAPPEL_J30': return '📅';
            case 'RAPPEL_J5': return '⏰';
            case 'RETARD': return '⚠️';
            case 'RETOUR': return '✅';
            default: return '🔔';
        }
    };

    const getNotificationMessage = (type) => {
        switch(type) {
            case 'RAPPEL_J30': return 'Rappel : retour dans 30 jours';
            case 'RAPPEL_J5': return 'Rappel : retour dans 5 jours';
            case 'RETARD': return 'Livre en retard !';
            case 'RETOUR': return 'Livre retourné';
            default: return 'Notification';
        }
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR', { day: '2-digit', month: '2-digit' });
    };

    if (!localStorage.getItem('userId') || !localStorage.getItem('token')) {
        return null;
    }

    return (
        <div className="notification-badge-container">
            <button 
                className="notification-button"
                onClick={() => setShowDropdown(!showDropdown)}
            >
                🔔
                {count > 0 && <span className="badge-count">{count}</span>}
            </button>

            {showDropdown && (
                <div className="notification-dropdown">
                    <div className="dropdown-header">
                        <h3>Notifications</h3>
                        <Link to="/notifications" onClick={() => setShowDropdown(false)}>
                            Voir tout
                        </Link>
                    </div>
                    
                    <div className="dropdown-content">
                        {notifications.length === 0 ? (
                            <p className="no-notifications">Aucune notification</p>
                        ) : (
                            notifications.map(notif => (
                                <div key={notif.idNotif} className="notification-item">
                                    <span className="notif-icon">{getNotificationIcon(notif.type)}</span>
                                    <div className="notif-content">
                                        <p className="notif-message">{getNotificationMessage(notif.type)}</p>
                                        <span className="notif-date">{formatDate(notif.dateEnvoi)}</span>
                                    </div>
                                </div>
                            ))
                        )}
                    </div>
                </div>
            )}
        </div>
    );
}

export default NotificationBadge;
