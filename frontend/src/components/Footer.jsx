import { Link } from 'react-router-dom';

function Footer() {
    return (
        <footer className="footer">
            <div className="footer-content">
                <div className="footer-section">
                    <h3>Navigation</h3>
                    <nav className="footer-nav">
                        <Link to="/">Accueil</Link>
                        <Link to="/search-books">Livres</Link>
                        <Link to="/about">À propos</Link>
                        <Link to="/contact">Contact</Link>
                    </nav>
                </div>
                <div className="footer-section">
                    <h3>Informations légales</h3>
                    <nav className="footer-nav">
                        <Link to="/legal">Mentions légales</Link>
                        <Link to="/privacy">Politique de confidentialité</Link>
                    </nav>
                </div>
            </div>
            <div className="footer-bottom">
                <p>&copy; {new Date().getFullYear()} Bibliothèque Universitaire d'Amiens. Tous droits réservés.</p>
            </div>
        </footer>
    );
}

export default Footer;