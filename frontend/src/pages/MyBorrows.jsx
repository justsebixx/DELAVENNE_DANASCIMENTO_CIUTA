import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/MyBorrows.css';

function MyBorrows() {
    const [borrows, setBorrows] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const navigate = useNavigate();

    useEffect(() => {
        const token = localStorage.getItem('token');
        const userId = localStorage.getItem('userId');
        
        if (!token || !userId) {
            navigate('/login');
            return;
        }

        fetchMyBorrows(userId);
    }, [navigate]);

    const fetchMyBorrows = async (userId) => {
        setLoading(true);
        setError('');
        
        try {
            const response = await fetch(`http://localhost:8080/api/emprunts/user/${userId}/actifs`);
            
            if (response.ok) {
                const data = await response.json();
                setBorrows(data);
            } else {
                setError('Erreur lors du chargement de vos emprunts');
            }
        } catch (err) {
            setError('Erreur de connexion au serveur');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleReturn = async (empruntId) => {
        if (!window.confirm('Confirmer le retour de ce livre ?')) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/emprunts/${empruntId}/retour`, {
                method: 'PUT'
            });

            if (response.ok) {
                alert('Livre retourné avec succès !');
                const userId = localStorage.getItem('userId');
                fetchMyBorrows(userId);
            } else {
                alert('Erreur lors du retour du livre');
            }
        } catch (err) {
            alert('Erreur de connexion au serveur');
            console.error(err);
        }
    };

    const handleExtend = async (empruntId) => {
        if (!window.confirm('Prolonger cet emprunt de 15 jours ?')) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/emprunts/${empruntId}/prolonger`, {
                method: 'PUT'
            });

            if (response.ok) {
                alert('Emprunt prolongé de 15 jours !');
                const userId = localStorage.getItem('userId');
                fetchMyBorrows(userId);
            } else {
                const errorData = await response.json();
                alert(`Erreur : ${errorData.message || 'Impossible de prolonger'}`);
            }
        } catch (err) {
            alert('Erreur de connexion au serveur');
            console.error(err);
        }
    };

    const calculateDaysRemaining = (dateRetourPrevue) => {
        const returnDate = new Date(dateRetourPrevue);
        const today = new Date();
        const diffTime = returnDate - today;
        const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));
        return diffDays;
    };

    const getStatusClass = (dateRetourPrevue) => {
        const days = calculateDaysRemaining(dateRetourPrevue);
        if (days < 0) return 'overdue';
        if (days <= 5) return 'warning';
        return 'ok';
    };

    const formatDate = (dateString) => {
        const date = new Date(dateString);
        return date.toLocaleDateString('fr-FR');
    };

    return (
        <div className="my-borrows-container">
            <h1>Mes Emprunts</h1>

            {error && <div className="error-message">{error}</div>}

            {loading ? (
                <div className="loading">Chargement...</div>
            ) : borrows.length === 0 ? (
                <div className="no-borrows">
                    <p>Vous n'avez aucun emprunt en cours</p>
                    <button onClick={() => navigate('/search-books')} className="btn-search-books">
                        Rechercher des livres
                    </button>
                </div>
            ) : (
                <div className="borrows-grid">
                    {borrows.map(borrow => {
                        const daysRemaining = calculateDaysRemaining(borrow.dateRetourPrevue);
                        const statusClass = getStatusClass(borrow.dateRetourPrevue);

                        return (
                            <div key={borrow.idEmprunt} className={`borrow-card ${statusClass}`}>
                                <div className="borrow-header">
                                    <h3>{borrow.livre?.titre || 'Titre non disponible'}</h3>
                                    <span className={`status-badge ${statusClass}`}>
                                        {daysRemaining < 0 
                                            ? `En retard de ${Math.abs(daysRemaining)} jour(s)`
                                            : `${daysRemaining} jour(s) restant(s)`
                                        }
                                    </span>
                                </div>

                                <div className="borrow-details">
                                    <p><strong>Auteur:</strong> {borrow.livre?.auteur || 'N/A'}</p>
                                    <p><strong>Date d'emprunt:</strong> {formatDate(borrow.dateEmprunt)}</p>
                                    <p><strong>Date de retour prévue:</strong> {formatDate(borrow.dateRetourPrevue)}</p>
                                    <p><strong>ISBN:</strong> {borrow.livre?.isbn || 'N/A'}</p>
                                </div>

                                <div className="borrow-actions">
                                    <button 
                                        className="btn-extend"
                                        onClick={() => handleExtend(borrow.idEmprunt)}
                                    >
                                        Prolonger
                                    </button>
                                    <button 
                                        className="btn-return"
                                        onClick={() => handleReturn(borrow.idEmprunt)}
                                    >
                                        Retourner
                                    </button>
                                </div>

                                {statusClass === 'overdue' && (
                                    <div className="overdue-warning">
                                        ⚠️ Ce livre est en retard ! Veuillez le retourner au plus vite.
                                    </div>
                                )}
                            </div>
                        );
                    })}
                </div>
            )}
        </div>
    );
}

export default MyBorrows;
