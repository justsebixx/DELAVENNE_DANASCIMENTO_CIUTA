# Tests réalisés (exemples)

Ce document liste **10 tests** (exemples) présents dans le projet et utilisés pour valider les fonctionnalités principales.

## Prérequis
- Java 25+
- Maven 3.6+
- (Recommandé) Docker Desktop si vous lancez les tests d’intégration Spring (`@SpringBootTest`)

## Lancer les tests

### Backend (JUnit)
```bash
cd backend

# Optionnel (si nécessaire pour les tests d’intégration)
docker compose up -d

mvn test
```

### Frontend (qualité)
```bash
cd frontend
npm install
npm run build
```

## Liste de 10 tests (exemples)

### Auth (service)
1. `AuthServiceTest#login_ValidCredentials_ReturnsLoginResponse`
   - Vérifie qu’un utilisateur avec identifiants valides reçoit un `LoginResponse` et qu’un token de session est créé.
2. `AuthServiceTest#login_EmailNotFound_ThrowsUnauthorizedException`
   - Vérifie l’échec de connexion si l’email n’existe pas.
3. `AuthServiceTest#login_WrongPassword_ThrowsUnauthorizedException`
   - Vérifie l’échec de connexion si le mot de passe est incorrect.
4. `AuthServiceTest#register_ValidRequest_ReturnsLoginResponse`
   - Vérifie l’inscription d’un nouvel utilisateur et le retour d’un token.

### Livres (service + controller)
5. `LivreServiceTest#findAll_ReturnsAllBooks`
   - Vérifie le retour de tous les livres.
6. `LivreServiceTest#findById_NonExistingId_ThrowsException`
   - Vérifie le comportement lorsqu’un ID de livre n’existe pas.
7. `LivreControllerTest#getLivreById_NonExistingId_ReturnsNotFound`
   - Vérifie que l’API renvoie `404` quand le livre est introuvable.

### Emprunts (service)
8. `EmpruntServiceTest#create_ValidRequest_Success`
   - Vérifie la création d’un emprunt, la génération des dates et la décrémentation des disponibilités.
9. `EmpruntServiceTest#create_MaxEmpruntsReached_ThrowsException`
   - Vérifie le blocage quand la limite d’emprunts actifs est atteinte.

### Notifications (controller)
10. `NotificationControllerTest#testGetUnreadNotifications_Success`
    - Vérifie la récupération des notifications non lues pour un utilisateur authentifié.
