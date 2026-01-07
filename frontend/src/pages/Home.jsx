import { Link } from 'react-router-dom';
import '../styles/Home.css';

function Home() {
    const isLoggedIn = !!localStorage.getItem('token');

    return (
        <div className="home-container">
            {/* Section Hero */}
            <section className="hero-section">
                <div className="hero-content">
                    <h1>Bibliothèque Universitaire</h1>
                    <p>
                        Découvrez notre vaste collection de livres académiques et profitez d'un système 
                        de gestion moderne pour tous vos emprunts.
                    </p>
                    <div className="hero-buttons">
                        {isLoggedIn ? (
                            <>
                                <Link to="/search-books" className="hero-btn hero-btn-primary">
                                    Rechercher un livre
                                </Link>
                                <Link to="/my-borrows" className="hero-btn hero-btn-secondary">
                                    Mes emprunts
                                </Link>
                            </>
                        ) : (
                            <>
                                <Link to="/login" className="hero-btn hero-btn-primary">
                                    Se connecter
                                </Link>
                                <Link to="/search-books" className="hero-btn hero-btn-secondary">
                                    Voir le catalogue
                                </Link>
                            </>
                        )}
                    </div>
                </div>
            </section>

            {/* Section Fonctionnalités */}
            <section className="info-section">
                <div className="info-content">
                    <h2 className="info-title">Services de la Bibliothèque</h2>
                    <div className="features-grid">
                        <div className="feature-card">
                            <div className="feature-icon">📚</div>
                            <h3>Large Catalogue</h3>
                            <p>
                                Plus de 10 000 ouvrages dans tous les domaines académiques : 
                                sciences, littérature, histoire, arts et bien plus encore.
                            </p>
                        </div>
                        <div className="feature-card">
                            <div className="feature-icon">🔍</div>
                            <h3>Recherche Avancée</h3>
                            <p>
                                Trouvez rapidement le livre dont vous avez besoin grâce à notre 
                                système de recherche par titre, auteur, catégorie ou ISBN.
                            </p>
                        </div>
                        <div className="feature-card">
                            <div className="feature-icon">⏰</div>
                            <h3>Gestion Simple</h3>
                            <p>
                                Consultez vos emprunts en cours, prolongez-les en ligne et 
                                recevez des notifications avant les dates de retour.
                            </p>
                        </div>
                        <div className="feature-card">
                            <div className="feature-icon">🎓</div>
                            <h3>Accès Étudiant</h3>
                            <p>
                                Tous les étudiants et personnel de l'université ont accès à 
                                notre collection complète avec un compte personnalisé.
                            </p>
                        </div>
                        <div className="feature-card">
                            <div className="feature-icon">📱</div>
                            <h3>Plateforme Moderne</h3>
                            <p>
                                Interface responsive accessible depuis n'importe quel appareil : 
                                ordinateur, tablette ou smartphone.
                            </p>
                        </div>
                        <div className="feature-card">
                            <div className="feature-icon">🔔</div>
                            <h3>Notifications</h3>
                            <p>
                                Restez informé avec des alertes automatiques pour vos retours 
                                prévus et les nouvelles acquisitions.
                            </p>
                        </div>
                    </div>
                </div>
            </section>

            {/* Section Statistiques */}
            <section className="stats-section">
                <h2 style={{ fontSize: '2.5em', marginBottom: '50px' }}>Notre Bibliothèque en Chiffres</h2>
                <div className="stats-grid">
                    <div className="stat-item">
                        <div className="stat-number">10 000+</div>
                        <div className="stat-label">Livres disponibles</div>
                    </div>
                    <div className="stat-item">
                        <div className="stat-number">5 000+</div>
                        <div className="stat-label">Étudiants inscrits</div>
                    </div>
                    <div className="stat-item">
                        <div className="stat-number">20+</div>
                        <div className="stat-label">Catégories</div>
                    </div>
                    <div className="stat-item">
                        <div className="stat-number">24/7</div>
                        <div className="stat-label">Accès en ligne</div>
                    </div>
                </div>
            </section>

            {/* Section À propos */}
            <section className="about-section">
                <div className="about-content">
                    <h2>À propos de notre Bibliothèque</h2>
                    <p>
                        La Bibliothèque Universitaire de l'Université de Picardie Jules Verne est un espace 
                        moderne dédié à la recherche et à l'apprentissage. Située au cœur du campus d'Amiens, 
                        elle offre un environnement propice à l'étude avec des espaces de travail individuels 
                        et collectifs.
                    </p>
                    <p>
                        Notre mission est de soutenir la réussite académique de nos étudiants en leur donnant 
                        accès à des ressources documentaires de qualité, tout en facilitant la gestion de leurs 
                        emprunts grâce à notre plateforme numérique innovante.
                    </p>
                    <p>
                        Que vous soyez étudiant, enseignant ou chercheur, notre équipe est là pour vous accompagner 
                        dans vos démarches et vous aider à trouver les ressources dont vous avez besoin.
                    </p>
                </div>
            </section>
        </div>
    );
}

export default Home;
