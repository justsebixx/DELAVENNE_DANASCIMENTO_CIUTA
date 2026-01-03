const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api').replace(/\/+$/, '');
const API_URL = `${API_BASE_URL}/livres`;

export const livreService = {
    /**
     * Récupère tous les livres
     */
    async getAllLivres() {
        try {
            const response = await fetch(API_URL);
            if (!response.ok) {
                throw new Error('Erreur lors de la récupération des livres');
            }
            return await response.json();
        } catch (error) {
            console.error('Erreur:', error);
            throw error;
        }
    },

    /**
     * Récupère un livre par son ID
     */
    async getLivreById(id) {
        try {
            const response = await fetch(`${API_URL}/${id}`);
            if (!response.ok) {
                throw new Error('Livre non trouvé');
            }
            return await response.json();
        } catch (error) {
            console.error('Erreur:', error);
            throw error;
        }
    },

    /**
     * Filtre les livres localement (côté frontend)
     * @param {Array} livres - Liste des livres
     * @param {string} searchTerm - Terme de recherche
     */
    filterLivres(livres, searchTerm) {
        if (!searchTerm) return livres;
        
        const term = searchTerm.toLowerCase();
        return livres.filter(livre => 
            livre.titre?.toLowerCase().includes(term) ||
            livre.auteur?.toLowerCase().includes(term) ||
            livre.categorie?.toLowerCase().includes(term) ||
            livre.isbn?.toLowerCase().includes(term)
        );
    }
};
