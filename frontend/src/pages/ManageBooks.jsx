import { useState, useEffect } from 'react';
import '../styles/ManageBooks.css';

function ManageBooks() {
    const [books, setBooks] = useState([]);
    const [loading, setLoading] = useState(false);
    const [showModal, setShowModal] = useState(false);
    const [editMode, setEditMode] = useState(false);
    const [currentBook, setCurrentBook] = useState({
        idLivre: null,
        titre: '',
        auteur: '',
        categorie: '',
        isbn: '',
        annee: '',
        nbExemplaires: '',
        nbDisponibles: ''
    });
    const [error, setError] = useState('');

    useEffect(() => {
        fetchBooks();
    }, []);

    const fetchBooks = async () => {
        setLoading(true);
        try {
            const response = await fetch('http://localhost:8080/api/livres');
            if (response.ok) {
                const data = await response.json();
                setBooks(data);
            }
        } catch (err) {
            console.error('Erreur lors du chargement des livres:', err);
        } finally {
            setLoading(false);
        }
    };

    const handleInputChange = (e) => {
        const { name, value } = e.target;
        setCurrentBook(prev => ({
            ...prev,
            [name]: value
        }));
    };

    const openAddModal = () => {
        setEditMode(false);
        setCurrentBook({
            idLivre: null,
            titre: '',
            auteur: '',
            categorie: '',
            isbn: '',
            annee: '',
            nbExemplaires: '',
            nbDisponibles: ''
        });
        setError('');
        setShowModal(true);
    };

    const openEditModal = (book) => {
        setEditMode(true);
        setCurrentBook(book);
        setError('');
        setShowModal(true);
    };

    const closeModal = () => {
        setShowModal(false);
        setCurrentBook({
            idLivre: null,
            titre: '',
            auteur: '',
            categorie: '',
            isbn: '',
            annee: '',
            nbExemplaires: '',
            nbDisponibles: ''
        });
        setError('');
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');

        const bookData = {
            titre: currentBook.titre,
            auteur: currentBook.auteur,
            categorie: currentBook.categorie,
            isbn: currentBook.isbn,
            annee: parseInt(currentBook.annee),
            nbExemplaires: parseInt(currentBook.nbExemplaires),
            nbDisponibles: parseInt(currentBook.nbDisponibles)
        };

        try {
            const url = editMode 
                ? `http://localhost:8080/api/livres/${currentBook.idLivre}`
                : 'http://localhost:8080/api/livres';
            
            const response = await fetch(url, {
                method: editMode ? 'PUT' : 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bookData)
            });

            if (response.ok) {
                fetchBooks();
                closeModal();
                alert(editMode ? 'Livre modifié avec succès !' : 'Livre ajouté avec succès !');
            } else {
                const errorData = await response.json();
                setError(errorData.message || 'Erreur lors de l\'enregistrement');
            }
        } catch (err) {
            setError('Erreur de connexion au serveur');
            console.error(err);
        }
    };

    const handleDelete = async (bookId) => {
        if (!window.confirm('Êtes-vous sûr de vouloir supprimer ce livre ?')) {
            return;
        }

        try {
            const response = await fetch(`http://localhost:8080/api/livres/${bookId}`, {
                method: 'DELETE'
            });

            if (response.ok) {
                fetchBooks();
                alert('Livre supprimé avec succès !');
            } else {
                alert('Erreur lors de la suppression');
            }
        } catch (err) {
            alert('Erreur de connexion au serveur');
            console.error(err);
        }
    };

    return (
        <div className="manage-books-container">
            <div className="page-header">
                <h1>Gestion des Livres</h1>
                <button className="btn-add" onClick={openAddModal}>
                    + Ajouter un livre
                </button>
            </div>

            {loading ? (
                <div className="loading">Chargement...</div>
            ) : (
                <div className="books-table-container">
                    <table className="books-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Titre</th>
                                <th>Auteur</th>
                                <th>Catégorie</th>
                                <th>ISBN</th>
                                <th>Année</th>
                                <th>Exemplaires</th>
                                <th>Disponibles</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {books.map(book => (
                                <tr key={book.idLivre}>
                                    <td>{book.idLivre}</td>
                                    <td>{book.titre}</td>
                                    <td>{book.auteur}</td>
                                    <td>{book.categorie}</td>
                                    <td>{book.isbn}</td>
                                    <td>{book.annee}</td>
                                    <td>{book.nbExemplaires}</td>
                                    <td>{book.nbDisponibles}</td>
                                    <td>
                                        <div className="action-buttons">
                                            <button 
                                                className="btn-edit"
                                                onClick={() => openEditModal(book)}
                                            >
                                                ✏️
                                            </button>
                                            <button 
                                                className="btn-delete"
                                                onClick={() => handleDelete(book.idLivre)}
                                            >
                                                🗑️
                                            </button>
                                        </div>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            )}

            {showModal && (
                <div className="modal-overlay" onClick={closeModal}>
                    <div className="modal-content" onClick={(e) => e.stopPropagation()}>
                        <div className="modal-header">
                            <h2>{editMode ? 'Modifier le livre' : 'Ajouter un livre'}</h2>
                            <button className="modal-close" onClick={closeModal}>×</button>
                        </div>
                        
                        {error && <div className="error-message">{error}</div>}

                        <form onSubmit={handleSubmit} className="book-form">
                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="titre">Titre *</label>
                                    <input
                                        type="text"
                                        id="titre"
                                        name="titre"
                                        value={currentBook.titre}
                                        onChange={handleInputChange}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="auteur">Auteur *</label>
                                    <input
                                        type="text"
                                        id="auteur"
                                        name="auteur"
                                        value={currentBook.auteur}
                                        onChange={handleInputChange}
                                        required
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
                                        value={currentBook.categorie}
                                        onChange={handleInputChange}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="isbn">ISBN *</label>
                                    <input
                                        type="text"
                                        id="isbn"
                                        name="isbn"
                                        value={currentBook.isbn}
                                        onChange={handleInputChange}
                                        required
                                    />
                                </div>
                            </div>

                            <div className="form-row">
                                <div className="form-group">
                                    <label htmlFor="annee">Année *</label>
                                    <input
                                        type="number"
                                        id="annee"
                                        name="annee"
                                        value={currentBook.annee}
                                        onChange={handleInputChange}
                                        required
                                        min="1000"
                                        max="2100"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="nbExemplaires">Nombre d'exemplaires *</label>
                                    <input
                                        type="number"
                                        id="nbExemplaires"
                                        name="nbExemplaires"
                                        value={currentBook.nbExemplaires}
                                        onChange={handleInputChange}
                                        required
                                        min="0"
                                    />
                                </div>

                                <div className="form-group">
                                    <label htmlFor="nbDisponibles">Disponibles *</label>
                                    <input
                                        type="number"
                                        id="nbDisponibles"
                                        name="nbDisponibles"
                                        value={currentBook.nbDisponibles}
                                        onChange={handleInputChange}
                                        required
                                        min="0"
                                    />
                                </div>
                            </div>

                            <div className="modal-actions">
                                <button type="button" className="btn-cancel" onClick={closeModal}>
                                    Annuler
                                </button>
                                <button type="submit" className="btn-submit">
                                    {editMode ? 'Modifier' : 'Ajouter'}
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}

export default ManageBooks;
