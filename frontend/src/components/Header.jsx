import { useState, useRef, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import NotificationBadge from './NotificationBadge';

function Header() {
    const isLoggedIn = !!localStorage.getItem('token');
    const role = localStorage.getItem('role');
    const navigate = useNavigate();
    const [showDropdown, setShowDropdown] = useState(false);
    const dropdownRef = useRef(null);

    const isAdmin = role === 'ADMIN';
    const isBibliothecaire = role === 'BIBLIOTHECAIRE';
    const isStaff = isAdmin || isBibliothecaire;

    useEffect(() => {
        function handleClickOutside(event) {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
                setShowDropdown(false);
            }
        }
        document.addEventListener("mousedown", handleClickOutside);
        return () => {
            document.removeEventListener("mousedown", handleClickOutside);
        };
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userId');
        localStorage.removeItem('role');
        setShowDropdown(false);
        navigate('/login');
    };

    return (
        <header className="header">
            <nav className="nav-center">
                <Link to="/" className="nav-link">Accueil</Link>
                <Link to="/search-books" className="nav-link">Livres</Link>
                {isLoggedIn && <Link to="/my-borrows" className="nav-link">Mes Emprunts</Link>}
                {isStaff && <Link to="/manage-books" className="nav-link">Gestion Livres</Link>}
                {isStaff && <Link to="/statistics" className="nav-link">Statistiques</Link>}
            </nav>
            <div className="nav-right">
                {isLoggedIn && <NotificationBadge />}
                
                <div className="user-menu" ref={dropdownRef} style={{position: 'relative'}}>
                    {isLoggedIn ? (
                        <div 
                            className="nav-login" 
                            onClick={() => setShowDropdown(!showDropdown)}
                            style={{cursor: 'pointer'}}
                        >
                            <img src="https://cdn-icons-png.flaticon.com/512/747/747376.png" alt="Profile" className="login-icon" />
                        </div>
                    ) : (
                        <Link to="/login" className="nav-login">
                            <img src="https://cdn-icons-png.flaticon.com/512/747/747376.png" alt="Login" className="login-icon" />
                        </Link>
                    )}

                    {showDropdown && isLoggedIn && (
                        <div className="user-dropdown">
                            {isAdmin && (
                                <Link to="/admin" className="dropdown-item" onClick={() => setShowDropdown(false)}>
                                    Administration
                                </Link>
                            )}
                            {isStaff && (
                                <>
                                    <Link to="/manage-books" className="dropdown-item" onClick={() => setShowDropdown(false)}>
                                        Gestion des Livres
                                    </Link>
                                    <Link to="/statistics" className="dropdown-item" onClick={() => setShowDropdown(false)}>
                                        Statistiques
                                    </Link>
                                </>
                            )}
                            {!isStaff && (
                                <Link to="/my-borrows" className="dropdown-item" onClick={() => setShowDropdown(false)}>
                                    Mes Emprunts
                                </Link>
                            )}
                            <Link to="/profile" className="dropdown-item" onClick={() => setShowDropdown(false)}>
                                Mon Compte
                            </Link>
                            <div className="dropdown-divider"></div>
                            <button className="dropdown-item logout" onClick={handleLogout}>
                                Se déconnecter
                            </button>
                        </div>
                    )}
                </div>
            </div>
        </header>
    );
}

export default Header;