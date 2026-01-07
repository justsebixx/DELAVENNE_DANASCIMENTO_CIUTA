/**
 * Utilitaire pour gérer les appels API avec gestion d'erreur améliorée
 */

const API_BASE_URL = (import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api').replace(/\/+$/, '');

function normalizeEndpoint(endpoint) {
  if (!endpoint) return '';
  if (endpoint.startsWith('http')) return endpoint;
  return endpoint.startsWith('/') ? endpoint : `/${endpoint}`;
}

/**
 * Gestionnaire d'erreurs API
 */
export class ApiError extends Error {
  constructor(message, status, data) {
    super(message);
    this.name = 'ApiError';
    this.status = status;
    this.data = data;
  }
}

/**
 * Wrapper fetch avec gestion d'erreur
 */
export async function apiFetch(endpoint, options = {}) {
  const normalized = normalizeEndpoint(endpoint);
  const url = normalized.startsWith('http') ? normalized : `${API_BASE_URL}${normalized}`;
  
  const defaultHeaders = {
    'Content-Type': 'application/json',
  };

  // Ajouter le token si disponible
  const token = localStorage.getItem('token');
  if (token) {
    defaultHeaders['Authorization'] = `Bearer ${token}`;
  }

  const config = {
    ...options,
    headers: {
      ...defaultHeaders,
      ...options.headers,
    },
  };

  try {
    const response = await fetch(url, config);
    
    // Vérifier si la réponse est OK (200-299)
    if (!response.ok) {
      let errorData;
      try {
        errorData = await response.json();
      } catch {
        errorData = { message: response.statusText };
      }

      throw new ApiError(
        errorData.message || `Erreur HTTP ${response.status}`,
        response.status,
        errorData
      );
    }

    // Tenter de parser le JSON, gérer les réponses vides
    const contentType = response.headers.get('content-type');
    if (contentType && contentType.includes('application/json')) {
      return await response.json();
    }

    return null;
  } catch (error) {
    // Si c'est déjà une ApiError, la relancer
    if (error instanceof ApiError) {
      throw error;
    }

    // Erreur réseau ou autre
    throw new ApiError(
      'Erreur de connexion au serveur. Veuillez vérifier votre connexion internet.',
      0,
      { originalError: error.message }
    );
  }
}

/**
 * Méthodes HTTP simplifiées
 */
export const api = {
  get: (endpoint, options = {}) => apiFetch(endpoint, { ...options, method: 'GET' }),
  
  post: (endpoint, data, options = {}) => 
    apiFetch(endpoint, {
      ...options,
      method: 'POST',
      body: JSON.stringify(data),
    }),
  
  put: (endpoint, data, options = {}) =>
    apiFetch(endpoint, {
      ...options,
      method: 'PUT',
      body: JSON.stringify(data),
    }),
  
  delete: (endpoint, options = {}) =>
    apiFetch(endpoint, { ...options, method: 'DELETE' }),
};

/**
 * Messages d'erreur conviviaux selon le code HTTP
 */
export function getErrorMessage(error) {
  if (!(error instanceof ApiError)) {
    return error.message || 'Une erreur inattendue est survenue';
  }

  switch (error.status) {
    case 400:
      return error.message || 'Données invalides. Veuillez vérifier votre saisie.';
    case 401:
      return 'Session expirée. Veuillez vous reconnecter.';
    case 403:
      return 'Accès refusé. Vous n\'avez pas les permissions nécessaires.';
    case 404:
      return 'Ressource non trouvée.';
    case 409:
      return error.message || 'Conflit avec les données existantes.';
    case 500:
      return 'Erreur serveur. Veuillez réessayer plus tard.';
    case 503:
      return 'Service temporairement indisponible.';
    default:
      return error.message || 'Une erreur est survenue. Veuillez réessayer.';
  }
}

/**
 * Hook pour vérifier si l'utilisateur est connecté
 */
export function isAuthenticated() {
  return !!localStorage.getItem('token');
}

/**
 * Déconnexion
 */
export function logout() {
  localStorage.removeItem('token');
  localStorage.removeItem('userId');
  localStorage.removeItem('role');
  window.location.href = '/login';
}
