import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children, requiredRole, allowedRoles }) => {
  const token = localStorage.getItem('token');
  const role = localStorage.getItem('role');

  if (!token) {
    // si pas de token, rediriger vers la page de connexion
    return <Navigate to="/login" replace />;
  }

  // Support pour plusieurs rôles autorisés
  if (allowedRoles && allowedRoles.length > 0) {
    if (!allowedRoles.includes(role)) {
      return <Navigate to="/" replace />;
    }
  } else if (requiredRole && role !== requiredRole) {
    // Rétrocompatibilité avec requiredRole unique
    return <Navigate to="/" replace />;
  }

  return children;
};

export default ProtectedRoute;
