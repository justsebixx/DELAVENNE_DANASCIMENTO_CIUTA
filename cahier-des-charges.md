# Cahier des charges - Application de gestion de bibliothèque

## 1. Présentation générale

### Titre du projet
**Application de gestion de bibliothèque universitaire**

### Membres du projet

- Sebastian-Cristian Ciuta
- Théo Da Nascimento
- Baptiste Delavenne

### Contexte / Origine de la demande
Dans le cadre d’un projet universitaire, une bibliothèque souhaite moderniser son système de gestion des livres, des emprunts et des utilisateurs.  
Actuellement, la gestion repose sur des registres papier et des fichiers Excel, ce qui engendre des erreurs de suivi, des retards non détectés et une organisation inefficace.

### Objectifs généraux
Développer une application permettant de gérer efficacement les livres, les emprunts et les utilisateurs d’une bibliothèque universitaire, tout en respectant les bonnes pratiques de développement, la sécurité des données et le travail collaboratif via Git.

### Portée du projet

**Inclus :**
- Gestion des livres (ajout, modification, suppression, recherche)
- Gestion des emprunts et des retours
- Notifications de rappel de retard (J-30 et J-5)
- Authentification et gestion des rôles utilisateurs
- Tableau de bord statistique
- Interface utilisateur ergonomique
- Base de données relationnelle MySQL
- Tests unitaires et documentation
- Dépôt Git avec historique de commits

**Exclus :**
- Gestion des achats de livres
- Paiement d’amendes en ligne
- Intégration avec des systèmes externes (ENT, SSO)
- Application mobile native

---

## 2. Acteurs du projet

- **Commanditaire** : Bibliothèque universitaire / Enseignant encadrant
- **Chef de projet** : Mr Maré
- **Utilisateurs finaux** :
  - Bibliothécaire
  - Étudiant
  - Enseignant
- **Parties prenantes / intervenants externes** :
  - Équipe de développement
  - Encadrant pédagogique

---

## 3. Besoins fonctionnels

### Fonctionnalités attendues

#### 1. Gestion des livres
- Ajouter un livre (titre, auteur, ISBN, catégorie, nombre d’exemplaires)
- Modifier les informations d’un livre
- Supprimer un livre
- Rechercher un livre (titre, auteur, catégorie)
- Afficher la disponibilité des livres
- Pages dédiées :
  - Page de recherche
  - Page de gestion des livres (bibliothécaire)

#### 2. Gestion des emprunts et des retours
- Emprunt d’un livre par un utilisateur
- Enregistrement du retour d’un livre
- Suivi des dates d’emprunt et de retour
- Calcul automatique des retards
- Notifications de rappel :
  - Rappel à J-30
  - Rappel à J-5
- Historique des emprunts par utilisateur

#### 3. Gestion des utilisateurs
- Authentification sécurisée
- Gestion des rôles :
  - **Bibliothécaire** : accès complet
  - **Étudiant** : consultation et emprunts
  - **Enseignant** : consultation et emprunts
- Droits d’accès spécifiques selon le rôle

#### 4. Tableau de bord
*(Réservé aux bibliothécaires)*
- Nombre total d’emprunts
- Taux de retard
- Livres les plus empruntés
- Statistiques globales d’utilisation de la bibliothèque

#### 5. Interface utilisateur
- Interface ergonomique et intuitive
- Application accessible via navigateur web
- Design responsive (ordinateur, tablette, mobile)

### Cas d’usage / scénarios d’utilisation
- Un étudiant recherche un livre et vérifie sa disponibilité
- Un bibliothécaire ajoute ou modifie un livre
- Un utilisateur emprunte un livre et reçoit des rappels avant la date de retour
- Un bibliothécaire consulte les statistiques de la bibliothèque

---

## 4. Contraintes techniques

### Technologies imposées
- **Backend** : Java
- **Base de données** : MySQL
- **Frontend** : React.js 
- **Gestion de version** : Git

### Compatibilité
- **Navigateurs** : Chrome, Firefox, Edge (versions récentes)
- **Plateforme** : Application web

### Normes et standards à respecter
- Bonnes pratiques de développement
- Code modulaire et documenté
- Tests unitaires

### Sécurité et confidentialité
- Mots de passe sécurisés (hash)
- Gestion des accès par rôles
- Protection des données utilisateurs

---

## 5. Contraintes organisationnelles

### Planning prévisionnel
- **Date de rendu** : 07/01/2026
- Répartition des tâches entre les membres du groupe
- Suivi du projet via Git

### Budget estimé
Pas de budget (projet académique)

### Ressources disponibles
- Ordinateurs personnels des membres du groupe
- Outils de développement open-source

---

## 6. Livrables attendus

### Documents
- Cahier des charges
- Rapport technique (architecture, choix techniques, tests)
- Plan de test
- Documentation technique

### Produits
- Application de gestion de bibliothèque fonctionnelle
- Base de données MySQL
- Dépôt Git avec historique de commits

### Tests et validations
- Tests unitaires
- Revue de code
- Démonstration de l’application

---

## 7. Critères de réussite

### Indicateurs de qualité
- Code propre, lisible et maintenable
- Fonctionnalités conformes au cahier des charges
- Interface claire et intuitive
- Utilisation correcte de Git

### Critères d’acceptation

#### Fonctionnalités
- Gestion complète des livres
- Emprunts et retours correctement suivis
- Notifications de rappel fonctionnelles
- Accès restreint selon les rôles utilisateurs

#### Qualité logicielle
- Dépôt Git structuré (README, commits clairs)
- Code commenté
- Tests unitaires présents

#### Livraison
- Application fonctionnelle et démontrable
- Code disponible sur un dépôt Git

### Métriques de performance
- Temps de réponse inférieur à 2 secondes
- Aucune erreur bloquante lors des opérations principales

---

**Date de rédaction** : 06/01/2026  
**Version** : 1.0  
**Statut** : Validé
