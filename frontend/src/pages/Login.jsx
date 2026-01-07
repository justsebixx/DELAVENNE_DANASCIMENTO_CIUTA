import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import '../styles/Login.css';

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api').replace(/\/+$/, '');

function Login() {
  const [isLogin, setIsLogin] = useState(true);
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    nom: '',
    prenom: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    // Validation côté client
    if (!isLogin) {
      if (formData.nom.trim().length < 2) {
        setError('Le nom doit contenir au moins 2 caractères');
        setLoading(false);
        return;
      }
      if (formData.prenom.trim().length < 2) {
        setError('Le prénom doit contenir au moins 2 caractères');
        setLoading(false);
        return;
      }
    }

    if (!/\S+@\S+\.\S+/.test(formData.email)) {
      setError('Veuillez entrer une adresse email valide');
      setLoading(false);
      return;
    }

    if (formData.password.length < 6) {
      setError('Le mot de passe doit contenir au moins 6 caractères');
      setLoading(false);
      return;
    }

    try {
      const endpoint = isLogin ? '/auth/login' : '/auth/register';
      const body = isLogin
        ? { email: formData.email, password: formData.password }
        : formData;

      const response = await fetch(`${API_BASE_URL}${endpoint}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(body),
      });

      const data = await response.json();

      if (!response.ok) {
        throw new Error(data.message || 'Une erreur est survenue');
      }

      if (isLogin) {
        // Stockage du token et des informations utilisateur
        localStorage.setItem('token', data.token);
        localStorage.setItem('userId', data.idUser);
        localStorage.setItem('role', data.role);
        
        // Redirection après connexion
        navigate('/');
      } else {
        // Inscription réussie, basculer vers le formulaire de connexion
        setIsLogin(true);
        setFormData({
          email: formData.email,
          password: '',
          nom: '',
          prenom: '',
        });
        alert('Inscription réussie ! Vous pouvez maintenant vous connecter.');
      }
    } catch (err) {
      setError(err.message || 'Erreur de connexion au serveur');
    } finally {
      setLoading(false);
    }
  };

  const toggleMode = () => {
    setIsLogin(!isLogin);
    setError('');
    setFormData({
      email: '',
      password: '',
      nom: '',
      prenom: '',
    });
  };

  return (
    <div className="login-container">
      <div className="login-box">
        <div className="login-header">
          <h1>{isLogin ? 'Connexion' : 'Inscription'}</h1>
          <p>{isLogin ? 'Connectez-vous à votre compte' : 'Créez votre compte'}</p>
        </div>

        <form onSubmit={handleSubmit} className="login-form">
          {!isLogin && (
            <>
              <div className="form-group">
                <label htmlFor="nom">Nom</label>
                <input
                  type="text"
                  id="nom"
                  name="nom"
                  value={formData.nom}
                  onChange={handleChange}
                  required={!isLogin}
                  placeholder="Votre nom"
                />
              </div>

              <div className="form-group">
                <label htmlFor="prenom">Prénom</label>
                <input
                  type="text"
                  id="prenom"
                  name="prenom"
                  value={formData.prenom}
                  onChange={handleChange}
                  required={!isLogin}
                  placeholder="Votre prénom"
                />
              </div>
            </>
          )}

          <div className="form-group">
            <label htmlFor="email">Email</label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleChange}
              required
              placeholder="votre@email.com"
            />
          </div>

          <div className="form-group">
            <label htmlFor="password">Mot de passe</label>
            <input
              type="password"
              id="password"
              name="password"
              value={formData.password}
              onChange={handleChange}
              required
              placeholder="••••••••"
              minLength={6}
            />
          </div>

          {error && <div className="error-message">{error}</div>}

          <button type="submit" className="btn-submit" disabled={loading}>
            {loading ? 'Chargement...' : isLogin ? 'Se connecter' : "S'inscrire"}
          </button>
        </form>

        <div className="login-footer">
          <p>
            {isLogin ? "Vous n'avez pas de compte ?" : 'Vous avez déjà un compte ?'}
            <button onClick={toggleMode} className="btn-toggle">
              {isLogin ? "S'inscrire" : 'Se connecter'}
            </button>
          </p>
        </div>
      </div>
    </div>
  );
}

export default Login;
