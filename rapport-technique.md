# Documentation Technique – Application de Gestion de Bibliothèque

## I. Introduction

### A. Présentation de l'application
Développement d'une application web moderne de gestion de bibliothèque universitaire utilisant Spring Boot 3.5 (Java), React 19 et MySQL 8.0. L'application permet la gestion complète des livres, emprunts, utilisateurs et notifications avec un système d'authentification basé sur des rôles (bibliothécaire, étudiant, enseignant). Elle offre un tableau de bord statistique, un système de notifications automatiques pour les rappels de retour (J-30 et J-5), et une interface utilisateur moderne et ergonomique.

### B. Objectifs
- Moderniser la gestion des prêts et retours de livres
- Éliminer les erreurs de suivi liées aux registres papier
- Automatiser les notifications de rappel
- Fournir des statistiques en temps réel sur l'utilisation de la bibliothèque
- Garantir la sécurité des données avec un système d'authentification robuste

---

## II. Architecture de l'Application

### A. Technologies

**Backend :**
- Spring Boot 3.5.7
- Java 25
- Spring Data JPA (ORM)
- MySQL 8.0
- Maven (gestion des dépendances)
- Port : 8080 (API REST avec contexte `/api`)

**Frontend :**
- React 19.1.1
- Vite 7.1.7 (build tool)
- React Router DOM 7.9.6 (routing)
- Chart.js 4.5.1 + React-Chartjs-2 (visualisation statistiques)
- Port : 5173 (dev server)

**Infrastructure :**
- Docker Compose (conteneurisation MySQL)
- CORS configuré pour localhost:5173

### B. Organisation des fichiers

```
/backend
  ├── src/main/java/com/example/bibliotheque_quali_dev/
  │   ├── BibliothequeQualiDevApplication.java    # Point d'entrée Spring Boot
  │   ├── config/
  │   │   ├── AuthInterceptor.java                # Intercepteur d'authentification
  │   │   ├── AuthPrincipal.java                  # Objet principal d'authentification
  │   │   ├── CorsConfig.java                     # Configuration CORS
  │   │   ├── RequireRoles.java                   # Annotation pour les rôles
  │   │   ├── SecurityConfig.java                 # Configuration Spring Security
  │   │   └── WebConfig.java                      # Configuration intercepteurs
  │   ├── controller/
  │   │   ├── AuthController.java                 # API authentification
  │   │   ├── DashboardController.java            # API statistiques dashboard
  │   │   ├── EmpruntController.java              # API emprunts
  │   │   ├── LivreController.java                # API livres
  │   │   ├── NotificationController.java         # API notifications
  │   │   └── UtilisateurController.java          # API utilisateurs
  │   ├── dto/
  │   │   ├── DashboardResponse.java              # Statistiques dashboard
  │   │   ├── EmpruntCreateRequest.java           # Requête création emprunt
  │   │   ├── LoginRequest.java                   # Requête connexion
  │   │   ├── LoginResponse.java                  # Réponse connexion
  │   │   ├── RegisterRequest.java                # Requête inscription
  │   │   └── TopLivreStat.java                   # Statistiques livres populaires
  │   ├── entity/
  │   │   ├── Emprunt.java                        # Entité Emprunt (JPA)
  │   │   ├── Livre.java                          # Entité Livre (JPA)
  │   │   ├── Notification.java                   # Entité Notification (JPA)
  │   │   ├── SessionToken.java                   # Entité Token de session (JPA)
  │   │   └── Utilisateur.java                    # Entité Utilisateur (JPA)
  │   ├── exception/
  │   │   ├── EmpruntAlreadyReturnedException.java
  │   │   ├── EmpruntNotFoundException.java
  │   │   ├── ForbiddenException.java
  │   │   ├── GlobalExceptionHandler.java         # Gestionnaire global d'exceptions
  │   │   ├── LivreNotAvailableException.java
  │   │   ├── LivreNotFoundException.java
  │   │   ├── NotificationNotFoundException.java
  │   │   ├── UnauthorizedException.java
  │   │   └── UtilisateurAlreadyExistsException.java
  │   ├── repository/
  │   │   ├── EmpruntRepository.java              # Repository JPA Emprunts
  │   │   ├── LivreRepository.java                # Repository JPA Livres
  │   │   ├── NotificationRepository.java         # Repository JPA Notifications
  │   │   ├── SessionTokenRepository.java         # Repository JPA Sessions
  │   │   └── UtilisateurRepository.java          # Repository JPA Utilisateurs
  │   └── service/
  │       └── TokenGenerator.java                 # Génération tokens session
  └── src/main/resources/
      ├── application.properties                  # Configuration Spring Boot
      └── data.sql                                # Données initiales (seed)

/frontend
  ├── src/
  │   ├── App.jsx                                 # Composant racine + routing
  │   ├── main.jsx                                # Point d'entrée React
  │   ├── components/
  │   │   ├── ErrorBoundary.jsx                   # Gestion des erreurs React
  │   │   ├── Footer.jsx                          # Pied de page
  │   │   ├── Header.jsx                          # En-tête de navigation
  │   │   ├── LoadingSpinner.jsx                  # Indicateur de chargement
  │   │   ├── NotificationBadge.jsx               # Badge de notifications
  │   │   ├── ProtectedRoute.jsx                  # Protection des routes par rôle
  │   │   └── ScrollToTop.jsx                     # Scroll automatique
  │   ├── pages/
  │   │   ├── About.jsx                           # À propos
  │   │   ├── Admin.jsx                           # Dashboard administrateur
  │   │   ├── Contact.jsx                         # Contact
  │   │   ├── Home.jsx                            # Page d'accueil
  │   │   ├── LegalNotice.jsx                     # Mentions légales
  │   │   ├── Login.jsx                           # Connexion/Inscription
  │   │   ├── ManageBooks.jsx                     # Gestion des livres (ADMIN)
  │   │   ├── MyBorrows.jsx                       # Mes emprunts
  │   │   ├── NotFound.jsx                        # Page 404
  │   │   ├── Notifications.jsx                   # Page notifications
  │   │   ├── PrivacyPolicy.jsx                   # Politique de confidentialité
  │   │   ├── Profile.jsx                         # Profil utilisateur
  │   │   └── SearchBooks.jsx                     # Recherche et consultation livres
  │   ├── services/
  │   │   ├── apiService.js                       # Service API générique
  │   │   └── livreService.js                     # Service API livres
  │   └── styles/
  │       ├── Home.css                            # Styles page accueil
  │       ├── Login.css                           # Styles page connexion
  │       ├── ManageBooks.css                     # Styles gestion livres
  │       ├── MyBorrows.css                       # Styles mes emprunts
  │       ├── Notifications.css                   # Styles notifications
  │       ├── Profile.css                         # Styles profil
  │       ├── responsive.css                      # Styles responsive
  │       └── SearchBooks.css                     # Styles recherche livres
  └── package.json                                # Dépendances npm

/docker-compose.yml                               # Configuration MySQL Docker
```

---

## III. Fonctionnalités

### A. Gestion des Livres
- **Consultation** : Liste complète avec recherche (titre, auteur, ISBN, catégorie)
- **Ajout** : Formulaire avec validation (titre, auteur, ISBN, catégorie, année, exemplaires)
- **Modification** : Mise à jour des informations (réservé bibliothécaire)
- **Suppression** : Suppression sécurisée (réservé bibliothécaire)
- **Disponibilité** : Affichage en temps réel du nombre d'exemplaires disponibles

### B. Gestion des Emprunts et Retours
- **Création d'emprunt** : Enregistrement avec date de retour prévue (31 jours par défaut)
- **Mes emprunts** : Page dédiée pour consulter ses emprunts personnels
- **Retour de livre** : Marquage de la date de retour effective et libération d'un exemplaire
- **Suivi des emprunts** : Statut (en cours, rendu, en retard)
- **Historique** : Consultation de tous les emprunts passés et en cours
- **Validation** : Vérification de la disponibilité avant création d'emprunt

### C. Authentification et Gestion des Utilisateurs
- **Inscription** : Création de compte avec email unique
- **Connexion** : Authentification par email/mot de passe avec token de session
- **Profil utilisateur** : Page dédiée pour consulter et modifier son profil
- **Rôles** :
  - `USER` : Étudiant (consultation, emprunt personnel)
  - `BIBLIOTHECAIRE` : Bibliothécaire (gestion complète)
  - `ADMIN` : Administrateur (tous les droits)
- **Sécurité** : 
  - Tokens de session avec expiration
  - Spring Security configuré (CSRF désactivé pour API REST)
  - Routes protégées côté frontend avec ProtectedRoute
  - Hachage BCrypt des mots de passe
- **Autorisation** : 
  - Backend : Intercepteur vérifiant les rôles via annotation `@RequireRoles`
  - Frontend : Composant ProtectedRoute pour routes protégées

### D. Système de Notifications
- **Rappel J-30** : Notification 30 jours avant la date de retour prévue
- **Rappel J-5** : Notification 5 jours avant la date de retour prévue
- **Types** : RAPPEL, RETARD
- **Suivi** : Historique des notifications envoyées par emprunt
- **Interface** : Page dédiée pour consulter toutes ses notifications
- **Badge** : Indicateur visuel du nombre de notifications non lues

### E. Tableau de Bord Statistiques (Bibliothécaires)
- **Vue d'ensemble** :
  - Total de livres (156)
  - Livres disponibles (124)
  - Livres empruntés (32)
  - Total utilisateurs (89)
  - Emprunts en cours (45)
  - Emprunts en retard (7)
- **Graphiques** :
  - Emprunts par mois (Bar Chart)
  - Distribution par catégorie (Pie Chart)
  - Top 10 livres populaires (Doughnut Chart)
  - Tendances d'emprunts (Line Chart)
  - Activité par rôle utilisateur (Radar Chart)
  - Disponibilité par catégorie (Polar Area Chart)

### F. Interface Utilisateur
- **Responsive Design** : Adaptation mobile/tablette/desktop avec fichier CSS dédié
- **Navigation** : 
  - Desktop : Header avec menu horizontal (Accueil, Livres, Mes Emprunts, Notifications, Admin, Profil)
  - Mobile (< 482px) : Menu hamburger avec navigation déroulante verticale
- **Menu Hamburger** :
  - Bouton avec animation de transformation en croix (X)
  - Menu déroulant collé au header sur toute la largeur
  - Animation fluide avec transition max-height
  - Fermeture automatique lors du clic sur un lien
- **Recherche en temps réel** : Filtrage instantané des livres sur la page SearchBooks
- **Gestion d'erreurs** : ErrorBoundary pour capturer les erreurs React
- **États de chargement** : LoadingSpinner pour les requêtes asynchrones
- **Feedback utilisateur** : Messages de succès/erreur avec gestion améliorée via apiService
- **Design moderne** : Interface épurée et intuitive avec composants réutilisables

---

## IV. Architecture Logicielle

### A. Modèle de Données

**Entités JPA :**

```
Utilisateur
├── idUser (PK, auto-increment)
├── nom
├── prenom
├── email (unique)
├── passwordhash (BCrypt)
└── role (USER, BIBLIOTHECAIRE, ADMIN)

Livre
├── idLivre (PK, auto-increment)
├── titre
├── auteur
├── categorie
├── isbn (unique)
├── annee
├── nb_exemplaires
└── nb_disponibles

Emprunt
├── idEmprunt (PK, auto-increment)
├── idUser (FK → Utilisateur)
├── idLivre (FK → Livre)
├── dateEmprunt
├── dateRetourPrevue
└── dateRetourEffective (nullable)

Notification
├── idNotif (PK, auto-increment)
├── idEmprunt (FK → Emprunt)
├── type (RAPPEL, RETARD)
└── dateEnvoi

SessionToken
├── id (PK, auto-increment)
├── token (unique)
├── userId (FK → Utilisateur)
└── expiresAt
```

### B. Architecture REST

**Contrôleurs (API Endpoints) :**

- **AuthController** (`/api/auth`)
  - `POST /login` : Authentification utilisateur
  - `POST /register` : Inscription nouvel utilisateur
  - `POST /logout` : Déconnexion et suppression token
  - `GET /me` : Récupérer informations utilisateur connecté

- **DashboardController** (`/api/dashboard`)
  - `GET /stats` : Récupérer statistiques complètes dashboard

- **LivreController** (`/api/livres`)
  - `GET /` : Récupérer tous les livres
  - `GET /{id}` : Récupérer un livre par ID
  - `POST /` : Ajouter un nouveau livre (ADMIN)
  - `PUT /{id}` : Modifier un livre (ADMIN)
  - `DELETE /{id}` : Supprimer un livre (ADMIN)

- **EmpruntController** (`/api/emprunts`)
  - `GET /` : Récupérer tous les emprunts (ADMIN)
  - `GET /user/{userId}` : Emprunts d'un utilisateur
  - `POST /` : Créer un emprunt
  - `PUT /{id}/retour` : Marquer un retour

- **UtilisateurController** (`/api/utilisateurs`)
  - `GET /` : Récupérer tous les utilisateurs (ADMIN)
  - `GET /{id}` : Récupérer un utilisateur par ID
  - `PUT /{id}` : Modifier un utilisateur
  - `DELETE /{id}` : Supprimer un utilisateur (ADMIN)

- **NotificationController** (`/api/notifications`)
  - `GET /` : Récupérer toutes les notifications
  - `GET /user/{userId}` : Notifications d'un utilisateur
  - `GET /emprunt/{empruntId}` : Notifications par emprunt
  - `POST /` : Créer une notification (ADMIN)
  - `PUT /{id}/mark-read` : Marquer une notification comme lue

### C. Architecture Frontend

**Structure par composants :**

- **App.jsx** : Routeur principal avec React Router et ErrorBoundary
- **Composants réutilisables** :
  - Header.jsx : Navigation globale avec badge notifications
  - Footer.jsx : Pied de page avec liens légaux
  - ProtectedRoute.jsx : HOC pour protéger les routes selon rôle
  - ErrorBoundary.jsx : Gestion des erreurs React
  - LoadingSpinner.jsx : Indicateur de chargement
  - NotificationBadge.jsx : Badge de notifications
  - ScrollToTop.jsx : Scroll automatique lors du changement de page
- **Pages** :
  - Home : Page d'accueil avec présentation
  - SearchBooks : Recherche et consultation de livres
  - ManageBooks : Gestion des livres (ajout, modification, suppression - ADMIN)
  - MyBorrows : Consultation de ses emprunts personnels
  - Notifications : Liste et gestion des notifications
  - Profile : Profil utilisateur avec informations personnelles
  - Admin : Dashboard avec graphiques Chart.js (ADMIN)
  - Login : Authentification (formulaire double connexion/inscription)
  - About, Contact, LegalNotice, PrivacyPolicy : Pages informatives
  - NotFound : Page 404
- **Services** :
  - apiService.js : Service API générique avec gestion d'erreurs (classe ApiError)
  - livreService.js : Service API spécifique aux livres

### D. Flux d'Exécution

**Initialisation Backend :**
1. `BibliothequeQualiDevApplication.main()` → Démarrage Spring Boot
2. Configuration JPA → Connexion MySQL
3. `create-drop` → Recréation schéma
4. `data.sql` → Insertion données initiales (6 utilisateurs, 20 livres, 10 emprunts, 2 notifications)
5. WebConfig → Configuration CORS et enregistrement AuthInterceptor
6. Controllers → Exposition API REST sur port 8080

**Initialisation Frontend :**
1. `main.jsx` → Montage de l'application React
2. `App.jsx` → Configuration routes React Router
3. Chargement composants Header/Footer
4. Navigation vers page d'accueil

**Flux d'une requête API protégée :**
1. Client → Requête HTTP avec header `Authorization: Bearer <token>` (via apiService)
2. Spring Security → Autorisation de la requête (CSRF désactivé)
3. AuthInterceptor.preHandle() → Vérification token
4. SessionTokenRepository → Validation token dans BDD
5. UtilisateurRepository → Récupération utilisateur
6. Vérification `@RequireRoles` → Autorisation selon rôle
7. Controller → Traitement logique métier
8. Repository → Requête JPA vers MySQL
9. Response → JSON renvoyé au client
10. apiService → Gestion erreur (ApiError) ou retour données

**Flux de recherche de livres (Frontend) :**
1. Utilisateur accède à SearchBooks.jsx
2. Chargement initial → `apiService.get('/livres')` → Récupération tous les livres
3. Utilisateur saisit dans barre de recherche
4. `handleSearch()` → Mise à jour `searchTerm` (state)
5. `useEffect()` → Déclenchement filtrage local
6. Filtrage sur titre/auteur/ISBN/catégorie
7. `setFilteredLivres()` → Mise à jour affichage
8. Re-render automatique de la liste avec LoadingSpinner pendant le chargement

**Flux de protection de route (Frontend) :**
1. Utilisateur tente d'accéder à une route protégée (ex: /admin)
2. ProtectedRoute.jsx → Vérification token dans localStorage
3. Si pas de token → Redirection vers /login
4. Si token présent → Vérification du rôle requis
5. Si rôle insuffisant → Redirection vers /
6. Si autorisé → Affichage du composant enfant

---

## V. Sécurité et Bonnes Pratiques

### A. Sécurité
- **Spring Security** : Configuration avec SecurityConfig (CSRF désactivé pour API REST)
- **Authentification** : Tokens de session avec expiration stockés en base
- **Hachage** : Mots de passe hashés avec BCrypt (salt rounds: 10)
- **Autorisation** : 
  - Backend : Contrôle d'accès par rôles avec AuthInterceptor et annotation @RequireRoles
  - Frontend : Routes protégées avec composant ProtectedRoute
- **CORS** : Configuration dédiée dans CorsConfig pour localhost:5173
- **Validation** : Validation des entrées côté backend
- **Gestion d'erreurs** : 
  - Backend : GlobalExceptionHandler pour exceptions centralisées
  - Frontend : ErrorBoundary pour erreurs React + ApiError pour erreurs API

### B. Bonnes Pratiques Développement
- **Architecture en couches** : Controller → Service → Repository
- **DTO Pattern** : Séparation entités JPA et objets de transfert
- **Conventions de nommage** :
  - Java : PascalCase (classes), camelCase (méthodes/variables)
  - React : PascalCase (composants), camelCase (fonctions/hooks)
  - SQL : snake_case (tables/colonnes)
- **Gestion d'erreurs** : Exceptions custom avec messages explicites
- **Modularité** : Un fichier par composant/contrôleur/entité
- **Documentation** : Commentaires sur les fonctions complexes

### C. Performances
- **JPA** : Lazy loading par défaut
- **React** : Hooks useState/useEffect pour gestion d'état optimisée
- **Vite** : Build ultra-rapide avec Hot Module Replacement (HMR)
- **MySQL** : Indexes sur clés primaires/étrangères

---

## VI. Installation et Déploiement

### A. Prérequis
- **Java** : JDK 25 ou supérieur
- **Maven** : 3.6+
- **Node.js** : 18+ avec npm
- **Docker** : Docker Desktop (pour MySQL)
- **Mémoire** : 4 Go RAM minimum

### B. Étapes d'installation

**1. Cloner le dépôt :**
```bash
git clone <URL_DEPOT>
cd DELAVENNE_DANASCIMENTO_CIUTA
```

**2. Démarrer MySQL avec Docker :**
```bash
docker-compose up -d
```
Attendre ~10 secondes que MySQL soit prêt.

**3. Lancer le Backend :**
```bash
cd backend
mvnw.cmd spring-boot:run
# Ou sur Linux/Mac: ./mvnw spring-boot:run
```
API accessible sur `http://localhost:8080/api`

**4. Lancer le Frontend :**
```bash
cd frontend
npm install
npm run dev
```
Application accessible sur `http://localhost:5173`

**5. Connexion initiale :**
- Email : `jean.dupont@email.com` (ADMIN)
- Mot de passe : `password123`

### C. Configuration

**Backend (`application.properties`) :**
```properties
spring.datasource.url=jdbc:mysql://localhost/bibliotheque
spring.datasource.username=bibli
spring.datasource.password=mdp123
server.servlet.context-path=/api
app.cors.allowed-origins=http://localhost:5173
```

**Frontend (`vite.config.js`) :**
```javascript
server: {
  port: 5173,
  proxy: {
    '/api': 'http://localhost:8080'
  }
}
```

---

## VII. Tests

### A. Tests Unitaires (Backend)
- Framework : **JUnit 5** + **Spring Boot Test**
- Localisation : `src/test/java/com/example/bibliotheque_quali_dev/`
- Classe de base : `BibliothequeQualiDevApplicationTests.java`

**Tests à implémenter :**
- Repositories : CRUD sur chaque entité
- Services : Logique métier (calcul dates, génération tokens)
- Controllers : Endpoints API avec MockMvc
- Exceptions : Cas d'erreur (livre indisponible, emprunt déjà retourné)

**Exécution :**
```bash
cd backend
mvnw.cmd test
```

### B. Tests Fonctionnels
- **Authentification** :
  - Inscription avec email unique
  - Connexion valide/invalide
  - Token de session expiré
- **Gestion livres** :
  - Ajout/modification/suppression (bibliothécaire)
  - Recherche par titre/auteur/ISBN/catégorie
  - Disponibilité décrémentée après emprunt
- **Emprunts** :
  - Création avec vérification disponibilité
  - Retour avec libération exemplaire
  - Détection retard (date retour prévue dépassée)
- **Notifications** :
  - Création automatique J-30 et J-5
  - Envoi uniquement pour emprunts non rendus
- **Tableau de bord** :
  - Statistiques en temps réel
  - Graphiques Chart.js (6 types)
  - Accès réservé bibliothécaires

---

## VIII. Structure de la Base de Données

### A. Tables

**utilisateurs**
```sql
CREATE TABLE utilisateurs (
  id_user INT PRIMARY KEY AUTO_INCREMENT,
  nom VARCHAR(100),
  prenom VARCHAR(100),
  email VARCHAR(255) UNIQUE,
  passwordhash VARCHAR(255),
  role ENUM('USER', 'BIBLIOTHECAIRE', 'ADMIN')
);
```

**livres**
```sql
CREATE TABLE livres (
  id_livre INT PRIMARY KEY AUTO_INCREMENT,
  titre VARCHAR(255),
  auteur VARCHAR(255),
  isbn VARCHAR(20) UNIQUE,
  categorie VARCHAR(100),
  annee INT,
  nb_exemplaires INT,
  nb_disponibles INT
);
```

**emprunts**
```sql
CREATE TABLE emprunts (
  id_emprunt INT PRIMARY KEY AUTO_INCREMENT,
  id_user INT,
  id_livre INT,
  date_emprunt DATE,
  date_retour_prevue DATE,
  date_retour_effective DATE,
  FOREIGN KEY (id_user) REFERENCES utilisateurs(id_user),
  FOREIGN KEY (id_livre) REFERENCES livres(id_livre)
);
```

**notifications**
```sql
CREATE TABLE notifications (
  id_notif INT PRIMARY KEY AUTO_INCREMENT,
  id_emprunt INT,
  type VARCHAR(50),
  date_envoi DATE,
  FOREIGN KEY (id_emprunt) REFERENCES emprunts(id_emprunt)
);
```

**session_tokens**
```sql
CREATE TABLE session_tokens (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  token VARCHAR(255) UNIQUE,
  user_id INT,
  expires_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES utilisateurs(id_user)
);
```

### B. Données Initiales

**6 utilisateurs** : 1 ADMIN, 1 BIBLIOTHECAIRE, 4 USER  
**20 livres** : Fantasy (4), Science-Fiction (5), Classiques (8), Jeunesse (1), Romance (1), Poésie (1)  
**10 emprunts** : 7 en cours, 3 rendus  
**2 notifications** : Rappels pour emprunts proches de l'échéance

---

## IX. Design et Expérience Utilisateur

### A. Palette de Couleurs
- **Primaire** : Bleu foncé (#1a1a2e) - Header/Footer
- **Secondaire** : Bleu clair (#16213e) - Cartes
- **Accent** : Orange (#e94560) - Boutons d'action
- **Texte** : Blanc (#ffffff) - Texte principal
- **Fond** : Gris clair (#f4f4f4) - Fond de page
- **Succès** : Vert (#28a745)
- **Erreur** : Rouge (#dc3545)

### B. Composants UI
- **Header** : 
  - Navigation sticky avec logo, menu et badge notifications
  - Menu hamburger animé pour mobile (< 482px)
  - Transformation fluide en croix (X) lors de l'ouverture
- **Cards** : Livres affichés en grille responsive (1-4 colonnes)
- **Forms** : Validation en temps réel avec messages d'erreur
- **Buttons** : Boutons primaires/secondaires avec hover effects
- **Charts** : 6 types de graphiques Chart.js avec légendes interactives (Admin dashboard)
- **LoadingSpinner** : Composant dédié pour états de chargement
- **NotificationBadge** : Badge avec compteur de notifications non lues
- **ErrorBoundary** : Gestion gracieuse des erreurs React
- **ProtectedRoute** : Composant HOC pour protection des routes
- **Mobile Navigation** : Menu déroulant pleine largeur collé au header

### C. Accessibilité
- **Responsive** : 
  - Breakpoints (mobile < 482px, tablet < 768px, desktop)
  - Menu hamburger pour écrans < 482px avec animation fluide
  - Navigation adaptative selon la taille d'écran
- **Contraste** : Ratio texte/fond respectant WCAG 2.1
- **Navigation** : Liens clairs et breadcrumb
- **Messages** : Feedback utilisateur sur toutes les actions
- **Mobile-First** : Menu déroulant optimisé pour petits écrans

---

## X. Améliorations Futures

### A. Fonctionnalités
- **Réservation de livres** : File d'attente pour livres indisponibles
- **Prolongation d'emprunt** : Extension de la date de retour (max 2 fois)
- **Système de notation** : Avis et notes sur les livres
- **Historique personnel** : Tableau de bord utilisateur avec ses emprunts
- **Recherche avancée** : Filtres multiples (année, catégorie, disponibilité)
- **Export PDF** : Génération de reçus d'emprunt
- **Email automatique** : Envoi réel de notifications par email
- **API publique** : Endpoints pour intégration externe

### B. Technique
- **Tests unitaires** : Couverture > 80%
- **Tests E2E** : Selenium/Cypress pour parcours utilisateur
- **CI/CD** : Pipeline GitHub Actions (build, test, deploy)
- **Docker full** : Conteneurisation backend + frontend
- **Cache** : Redis pour sessions et requêtes fréquentes
- **Logging** : SLF4J + Logback pour traçabilité
- **Monitoring** : Prometheus + Grafana pour métriques
- **Documentation API** : Swagger/OpenAPI

### C. Design
- **Mode sombre** : Thème alternatif
- **PWA** : Progressive Web App pour utilisation hors-ligne
- **Animations** : Transitions fluides entre pages
- **Internationalisation** : Support multilingue (FR/EN)

---

## XI. Gestion de Version

### A. Git - Conventions de Commit
```
feat: Ajout nouvelle fonctionnalité
fix: Correction de bug
docs: Modification documentation
refactor: Refactorisation code
style: Formatage code (sans changement logique)
test: Ajout/modification tests
chore: Maintenance (dépendances, config)
```

**Exemples :**
```bash
feat(emprunt): Ajout système de prolongation
fix(auth): Correction validation token expiré
docs(readme): Mise à jour instructions installation
refactor(livre): Optimisation requêtes JPA
test(controller): Tests unitaires LivreController
```

### B. Branches
- `main` : Production (code stable)
- `develop` : Développement (features en cours)
- `feature/<nom>` : Nouvelles fonctionnalités
- `fix/<nom>` : Corrections de bugs

---

## XII. Dépendances

### A. Backend (Maven)
```xml
spring-boot-starter-web (3.5.7)         # REST API
spring-boot-starter-data-jpa (3.5.7)   # JPA/Hibernate
mysql-connector-java (8.0.33)          # Driver MySQL
hsqldb (runtime)                       # BDD en mémoire (tests)
```

### B. Frontend (npm)
```json
react (19.1.1)                         # Framework UI
react-router-dom (7.9.6)               # Routing SPA
chart.js (4.5.1)                       # Graphiques
react-chartjs-2 (5.3.1)                # Wrapper Chart.js pour React
vite (7.1.7)                           # Build tool
eslint (9.36.0)                        # Linter JavaScript
```

---

## XIII. Informations Projet

### Équipe
- **Membres** : DELAVENNE, DANASCIMENTO, CIUTA
- **Formation** : BUT Informatique 2025-2026 (3ème année)
- **Module** : R5-08 - Qualité de développement
- **Encadrant** : À préciser
- **Date limite** : 32/12/2025 *(sic)*

### Dépôt Git
- **Nom** : `DELAVENNE_DANASCIMENTO_CIUTA`
- **Localisation** : `c:\iut\But3\R5-08\DELAVENNE_DANASCIMENTO_CIUTA`
- **Technologies** : Full Stack Java/React

### Livrables
1. ✅ Code source avec historique Git
2. ✅ Documentation technique (ce document)
3. ⏳ Rapport de tests (à compléter)
4. ⏳ Démonstration live (à planifier)

---

## XIV. Contact et Support

### A. Problèmes Connus
- **Configuration MySQL** : Le commentaire dans `application.properties` indique un problème d'accès utilisateur. Recommandation : utiliser `root` avec `mdp123` pour tous les membres de l'équipe.
- **CORS** : Si erreur CORS, vérifier que le frontend tourne bien sur port 5173.
- **Données initiales** : Si `data.sql` ne s'exécute pas, vérifier `spring.jpa.defer-datasource-initialization=true`.

### B. Ressources
- **Documentation Spring Boot** : https://spring.io/projects/spring-boot
- **Documentation React** : https://react.dev
- **Chart.js** : https://www.chartjs.org
- **MySQL** : https://dev.mysql.com/doc

---

**Dernière mise à jour** : 7 janvier 2026  
**Version du document** : 1.1  
**Statut du projet** : En cours

**Changelog :**
- v1.1 (07/01/2026) : Ajout menu hamburger responsive pour mobile
- v1.0 (07/01/2026) : Version initiale de la documentation
