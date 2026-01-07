import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/Profile.css';

function Profile() {
    const navigate = useNavigate();
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
            navigate('/login');
            return;
        }
        fetchProfile();
    }, [navigate]);

    const fetchProfile = async () => {
        try {
            const token = localStorage.getItem('token');
            const response = await fetch('http://localhost:8080/api/utilisateurs/me', {
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });

            if (response.ok) {
                const data = await response.json();
                setUser(data);
            } else {
                setMessage({ text: 'Impossible de charger le profil', type: 'error' });
                if (response.status === 401) {
                    handleLogout();
                }
            }
        } catch (err) {
            setMessage({ text: 'Erreur de connexion', type: 'error' });
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
            const token = localStorage.getItem('token');
            const response = await fetch('http://localhost:8080/api/utilisateurs/me', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(user)
            });

            if (response.ok) {
                const updatedUser = await response.json();
                setUser(updatedUser);
                setMessage({ text: 'Profil mis à jour avec succès', type: 'success' });
            } else {
                const errorData = await response.json();
                setMessage({ text: errorData.message || 'Erreur lors de la mise à jour', type: 'error' });
            }
        } catch (err) {
            setMessage({ text: 'Erreur de connexion', type: 'error' });
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
        localStorage.removeItem('role');
        navigate('/login');
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
