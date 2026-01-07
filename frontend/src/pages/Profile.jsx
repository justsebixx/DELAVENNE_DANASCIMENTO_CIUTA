import { useState, useEffect } from 'react';
import '../styles/Profile.css';
import { api, getErrorMessage, logout } from '../services/apiService';

function Profile() {
    const [user, setUser] = useState({
        nom: '',
        prenom: '',
        email: '',
        role: ''
    });
    const [loading, setLoading] = useState(true);
    const [message, setMessage] = useState({ text: '', type: '' });

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (!token) {
            logout();
            return;
        }
        fetchProfile();
    }, []);

    const fetchProfile = async () => {
        try {
            const data = await api.get('/utilisateurs/me');
            setUser(data);
        } catch (err) {
            setMessage({ text: getErrorMessage(err), type: 'error' });
            if (err?.status === 401) {
                logout();
            }
        } finally {
            setLoading(false);
        }
    };

    const handleChange = (e) => {
        setUser({
            ...user,
            [e.target.name]: e.target.value
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setMessage({ text: '', type: '' });
        
        try {
            const updatedUser = await api.put('/utilisateurs/me', user);
            setUser(updatedUser);
            setMessage({ text: 'Profil mis à jour avec succès', type: 'success' });
        } catch (err) {
            setMessage({ text: getErrorMessage(err), type: 'error' });
        }
    };

    const handleLogout = () => {
        logout();
    };

    if (loading) return <div className="loading-profile">Chargement...</div>;

    return (
        <div className="profile-container">
            <div className="profile-header">
                <h1>Mon Compte</h1>
                <span className={`profile-role-badge ${user.role === 'ADMIN' ? 'admin' : 'user'}`}>
                    {user.role}
                </span>
            </div>

            {message.text && (
                <div className={`message ${message.type}`}>
                    {message.text}
                </div>
            )}

            <form onSubmit={handleSubmit} className="profile-form">
                <div className="form-group">
                    <label htmlFor="nom">Nom</label>
                    <input
                        type="text"
                        id="nom"
                        name="nom"
                        value={user.nom}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="prenom">Prénom</label>
                    <input
                        type="text"
                        id="prenom"
                        name="prenom"
                        value={user.prenom}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={user.email}
                        onChange={handleChange}
                        required
                    />
                </div>

                <div className="profile-actions">
                    <button type="button" onClick={handleLogout} className="btn-logout">
                        Se déconnecter
                    </button>
                    <button type="submit" className="btn-save">
                        Enregistrer
                    </button>
                </div>
            </form>
        </div>
    );
}

export default Profile;
