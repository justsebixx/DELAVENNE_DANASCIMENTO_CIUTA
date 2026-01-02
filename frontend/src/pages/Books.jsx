import { useState, useEffect } from 'react';
import { livreService } from '../services/livreService';
import '../styles/Books.css';

function Books() {
    const [livres, setLivres] = useState([]);
    const [filteredLivres, setFilteredLivres] = useState([]);
    const [searchTerm, setSearchTerm] = useState('');
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        loadLivres();
    }, []);

    useEffect(() => {
        const filtered = livreService.filterLivres(livres, searchTerm);
        setFilteredLivres(filtered);
    }, [searchTerm, livres]);

    const loadLivres = async () => {
        try {
            setLoading(true);
            setError(null);
            const data = await livreService.getAllLivres();
            setLivres(data);
            setFilteredLivres(data);
        } catch (err) {
            setError('Impossible de charger les livres. Vérifiez que le backend est démarré.');
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = (e) => {
        setSearchTerm(e.target.value);
    };

    if (loading) {
        return (
            <div className="container">
                <div className="books-loading">
                    <p>Chargement des livres...</p>
                </div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="container">
                <div className="books-error">
                    <p>{error}</p>
                    <button onClick={loadLivres}>
                        Réessayer
                    </button>
                </div>
            </div>
        );
    }

    return (
        <div className="container books-container">
            <h1>Liste des Livres</h1>
            <p>Tous les livres de la bibliothèque</p>

            {/* Barre de recherche */}
            <div className="books-search-bar">
                <input
                    type="text"
                    placeholder="Rechercher par titre, auteur, catégorie ou ISBN..."
                    value={searchTerm}
                    onChange={handleSearch}
                    className="books-search-input"
                />
            </div>

            {/* Statistiques */}
            <div className="books-stats">
                <p>
                    <strong>{filteredLivres.length}</strong> livre(s) trouvé(s)
                    {searchTerm && ` sur ${livres.length} au total`}
                </p>
            </div>

            {/* Liste des livres */}
            {filteredLivres.length === 0 ? (
                <p>Aucun livre trouvé.</p>
            ) : (
                <div className="books-grid">
                    {filteredLivres.map((livre) => (
                        <div key={livre.idLivre} className="book-card">
                            <h3>{livre.titre}</h3>
                            <p className="book-info">
                                <strong>Auteur:</strong> {livre.auteur}
                            </p>
                            <p className="book-info">
                                <strong>Catégorie:</strong> {livre.categorie}
                            </p>
                            <p className="book-info">
                                <strong>ISBN:</strong> {livre.isbn}
                            </p>
                            <p className="book-info">
                                <strong>Année:</strong> {livre.annee}
                            </p>
                            <div className={`book-availability ${livre.nb_disponibles > 0 ? 'available' : 'unavailable'}`}>
                                <p>
                                    <strong>Disponibles:</strong> {livre.nb_disponibles} / {livre.nb_exemplaires}
                                </p>
                                {livre.nb_disponibles === 0 && (
                                    <p className="unavailable-message">
                                        Aucun exemplaire disponible
                                    </p>
                                )}
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default Books;
