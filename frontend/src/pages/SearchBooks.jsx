import { useState, useEffect } from 'react';
import '../styles/SearchBooks.css';
import { api, getErrorMessage } from '../services/apiService';

function SearchBooks() {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState('');
    const [filters, setFilters] = useState({
        titre: '',
        auteur: '',
        categorie: '',
        disponible: ''
    });
    const [categories, setCategories] = useState([]);

    useEffect(() => {
        fetchCategories();
        fetchBooks();
    }, []);

    const fetchCategories = async () => {
        try {
            const data = await api.get('/livres/categories');
            setCategories(data);
        } catch (err) {
            console.error('Erreur lors du chargement des catégories:', err);
        }
    };

    const fetchBooks = async () => {
        setLoading(true);
        setError('');
        
        try {
            const params = new URLSearchParams();
            if (filters.titre) params.append('titre', filters.titre);
            if (filters.auteur) params.append('auteur', filters.auteur);
            if (filters.categorie) params.append('categorie', filters.categorie);
            if (filters.disponible !== '') params.append('disponible', filters.disponible);

            const endpoint = filters.titre || filters.auteur || filters.categorie || filters.disponible !== '' 
                ? `/livres/search?${params.toString()}`
                : '/livres';

            const data = await api.get(endpoint);
            setBooks(data);
        } catch (err) {
            setError(getErrorMessage(err));
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    const handleFilterChange = (e) => {
        const { name, value } = e.target;
        setFilters(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSearch = (e) => {
        e.preventDefault();
        fetchBooks();
    };

    const resetFilters = () => {
        setFilters({
            titre: '',
            auteur: '',
            categorie: '',
            disponible: ''
        });
        setTimeout(() => fetchBooks(), 100);
    };

    const handleBorrow = async (bookId) => {
        const token = localStorage.getItem('token');
        const userId = localStorage.getItem('userId');
        
        if (!token || !userId) {
            alert('Vous devez être connecté pour emprunter un livre');
            return;
        }

        try {
            await api.post('/emprunts', {
                idLivre: bookId,
                idUser: parseInt(userId)
            });

            alert('Livre emprunté avec succès !');
            fetchBooks(); // Rafraîchir la liste
        } catch (err) {
            alert(getErrorMessage(err));
            console.error(err);
        }
    };

    return (
        <div className="search-books-container">
            <h1>Recherche de Livres</h1>
            
            <form className="search-form" onSubmit={handleSearch}>
                <div className="filters-row">
                    <div className="filter-group">
                        <label htmlFor="titre">Titre</label>
                        <input
                            type="text"
                            id="titre"
                            name="titre"
                            value={filters.titre}
                            onChange={handleFilterChange}
                            placeholder="Rechercher par titre..."
                        />
                    </div>

                    <div className="filter-group">
                        <label htmlFor="auteur">Auteur</label>
                        <input
                            type="text"
                            id="auteur"
                            name="auteur"
                            value={filters.auteur}
                            onChange={handleFilterChange}
                            placeholder="Rechercher par auteur..."
                        />
                    </div>

                    <div className="filter-group">
                        <label htmlFor="categorie">Catégorie</label>
                        <select
                            id="categorie"
                            name="categorie"
                            value={filters.categorie}
                            onChange={handleFilterChange}
                        >
                            <option value="">Toutes les catégories</option>
                            {categories.map((cat, index) => (
                                <option key={index} value={cat}>{cat}</option>
                            ))}
                        </select>
                    </div>

                    <div className="filter-group">
                        <label htmlFor="disponible">Disponibilité</label>
                        <select
                            id="disponible"
                            name="disponible"
                            value={filters.disponible}
                            onChange={handleFilterChange}
                        >
                            <option value="">Tous</option>
                            <option value="true">Disponibles uniquement</option>
                            <option value="false">Non disponibles</option>
                        </select>
                    </div>
                </div>

                <div className="search-actions">
                    <button type="submit" className="btn-search">Rechercher</button>
                    <button type="button" className="btn-reset" onClick={resetFilters}>Réinitialiser</button>
                </div>
            </form>

            {error && <div className="error-message">{error}</div>}

            {loading ? (
                <div className="loading">Chargement...</div>
            ) : (
                <div className="books-grid">
                    {books.length === 0 ? (
                        <p className="no-results">Aucun livre trouvé</p>
                    ) : (
                        books.map(book => (
                            <div key={book.idLivre} className="book-card">
                                <div className="book-header">
                                    <h3>{book.titre}</h3>
                                    <span className={`status ${book.nbDisponibles > 0 ? 'available' : 'unavailable'}`}>
                                        {book.nbDisponibles > 0 ? 'Disponible' : 'Indisponible'}
                                    </span>
                                </div>
                                <div className="book-details">
                                    <p><strong>Auteur:</strong> {book.auteur}</p>
                                    <p><strong>Catégorie:</strong> {book.categorie}</p>
                                    <p><strong>ISBN:</strong> {book.isbn}</p>
                                    <p><strong>Année:</strong> {book.annee}</p>
                                    <p><strong>Exemplaires:</strong> {book.nbDisponibles} / {book.nbExemplaires}</p>
                                </div>
                                {book.nbDisponibles > 0 && (
                                    <button 
                                        className="btn-borrow"
                                        onClick={() => handleBorrow(book.idLivre)}
                                    >
                                        Emprunter
                                    </button>
                                )}
                            </div>
                        ))
                    )}
                </div>
            )}
        </div>
    );
}

export default SearchBooks;
