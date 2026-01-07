import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { api, getErrorMessage } from '../services/apiService';
import LoadingSpinner from '../components/LoadingSpinner';
import '../styles/AddBook.css';

function AddBook() {
    const [formData, setFormData] = useState({
        titre: '',
        auteur: '',
        categorie: '',
        isbn: '',
        annee: new Date().getFullYear(),
        nbExemplaires: 1,
        nbDisponibles: 1
    });
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    const navigate = useNavigate();

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));
        setError('');
    };

    const validateForm = () => {
        if (formData.titre.trim().length < 2) {
            setError('Le titre doit contenir au moins 2 caractères');
            return false;
        }
        if (formData.auteur.trim().length < 2) {
            setError('L\'auteur doit contenir au moins 2 caractères');
            return false;
        }
        if (formData.categorie.trim().length < 2) {
            setError('La catégorie doit contenir au moins 2 caractères');
            return false;
        }
        if (!/^[\d-]+$/.test(formData.isbn)) {
            setError('L\'ISBN doit contenir uniquement des chiffres et des tirets');
            return false;
        }
        if (formData.annee < 1000 || formData.annee > new Date().getFullYear()) {
            setError('L\'année doit être comprise entre 1000 et l\'année actuelle');
            return false;
        }
        if (formData.nbExemplaires < 1) {
            setError('Le nombre d\'exemplaires doit être supérieur à 0');
            return false;
        }
        if (formData.nbDisponibles < 0 || formData.nbDisponibles > formData.nbExemplaires) {
            setError('Le nombre d\'exemplaires disponibles est invalide');
            return false;
        }
        return true;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        
        if (!validateForm()) {
            return;
        }

        setLoading(true);

        try {
            await api.post('/livres', {
                ...formData,
                annee: parseInt(formData.annee),
                nbExemplaires: parseInt(formData.nbExemplaires),
                nbDisponibles: parseInt(formData.nbDisponibles)
            });

            setSuccess(true);
            setTimeout(() => {
                navigate('/manage-books');
            }, 1500);
        } catch (err) {
            setError(getErrorMessage(err));
        } finally {
            setLoading(false);
        }
    };

    const handleReset = () => {
        setFormData({
            titre: '',
            auteur: '',
            categorie: '',
            isbn: '',
            annee: new Date().getFullYear(),
            nbExemplaires: 1,
            nbDisponibles: 1
        });
        setError('');
        setSuccess(false);
    };

    if (loading) {
        return <LoadingSpinner fullScreen />;
    }

    return (
        <div className="add-book-container">
            <div className="add-book-header">
                <h1>Ajouter un Livre</h1>
                <p>Remplissez le formulaire pour ajouter un nouveau livre à la bibliothèque</p>
            </div>

            {success && (
                <div className="success-message">
                    ✓ Livre ajouté avec succès ! Redirection...
                </div>
            )}

            {error && (
                <div className="error-message">
                    ⚠ {error}
                </div>
            )}

            <form onSubmit={handleSubmit} className="add-book-form">
                <div className="form-row">
                    <div className="form-group">
                        <label htmlFor="titre">Titre *</label>
                        <input
                            type="text"
                            id="titre"
                            name="titre"
                            value={formData.titre}
                            onChange={handleChange}
                            required
                            placeholder="Ex: Le Petit Prince"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="auteur">Auteur *</label>
                        <input
                            type="text"
                            id="auteur"
                            name="auteur"
                            value={formData.auteur}
                            onChange={handleChange}
                            required
                            placeholder="Ex: Antoine de Saint-Exupéry"
                        />
                    </div>
                </div>

                <div className="form-row">
                    <div className="form-group">
                        <label htmlFor="categorie">Catégorie *</label>
                        <input
                            type="text"
                            id="categorie"
                            name="categorie"
                            value={formData.categorie}
                            onChange={handleChange}
                            required
                            placeholder="Ex: Fiction, Science, Histoire..."
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="isbn">ISBN *</label>
                        <input
                            type="text"
                            id="isbn"
                            name="isbn"
                            value={formData.isbn}
                            onChange={handleChange}
                            required
                            placeholder="Ex: 978-3-16-148410-0"
                        />
                    </div>
                </div>

                <div className="form-row">
                    <div className="form-group">
                        <label htmlFor="annee">Année de publication *</label>
                        <input
                            type="number"
                            id="annee"
                            name="annee"
                            value={formData.annee}
                            onChange={handleChange}
                            required
                            min="1000"
                            max={new Date().getFullYear()}
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="nbExemplaires">Nombre d'exemplaires *</label>
                        <input
                            type="number"
                            id="nbExemplaires"
                            name="nbExemplaires"
                            value={formData.nbExemplaires}
                            onChange={handleChange}
                            required
                            min="1"
                        />
                    </div>

                    <div className="form-group">
                        <label htmlFor="nbDisponibles">Exemplaires disponibles *</label>
                        <input
                            type="number"
                            id="nbDisponibles"
                            name="nbDisponibles"
                            value={formData.nbDisponibles}
                            onChange={handleChange}
                            required
                            min="0"
                            max={formData.nbExemplaires}
                        />
                    </div>
                </div>

                <div className="form-actions">
                    <button type="submit" className="btn-submit" disabled={loading}>
                        {loading ? 'Ajout en cours...' : 'Ajouter le livre'}
                    </button>
                    <button type="button" className="btn-reset" onClick={handleReset}>
                        Réinitialiser
                    </button>
                    <button type="button" className="btn-cancel" onClick={() => navigate('/manage-books')}>
                        Annuler
                    </button>
                </div>
            </form>
        </div>
    );
}

export default AddBook;
