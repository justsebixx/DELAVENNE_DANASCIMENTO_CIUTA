import { Link } from 'react-router-dom';

function Header() {
    return (
        <header className="header">
            <nav className="nav-left">
                <Link to="/" className="nav-link">Accueil</Link>
                <Link to="/books" className="nav-link">Livres</Link>
                <Link to="/search-books" className="nav-link">Rechercher</Link>
            </nav>
            <div className="search-container">
                <input type="text" className="search-input" placeholder="Rechercher un livre..." />
            </div>
            <div className="nav-right">
                <Link to="/login" className="nav-login">
                    <img src="https://cdn-icons-png.flaticon.com/512/747/747376.png" alt="Login" className="login-icon" />
                </Link>
            </div>
        </header>
    );
}

export default Header;