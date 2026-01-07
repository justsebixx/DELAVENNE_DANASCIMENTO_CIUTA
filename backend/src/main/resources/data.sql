-- Insertion des utilisateurs (mot de passe en clair pour les tests : password123)
INSERT INTO utilisateurs (nom, prenom, email, passwordhash, role) VALUES
('Dupont', 'Jean', 'admin@email.com', 'password123', 'ADMIN'),
('Martin', 'Sophie', 'sophie.martin@email.com', 'password123', 'ETUDIANT'),
('Bernard', 'Luc', 'luc.bernard@email.com', 'password123', 'ETUDIANT'),
('Dubois', 'Marie', 'marie.dubois@email.com', 'password123', 'ENSEIGNANT'),
('Thomas', 'Pierre', 'pierre.thomas@email.com', 'password123', 'ENSEIGNANT'),
('Petit', 'Claire', 'bibliothecaire@email.com', 'password123', 'BIBLIOTHECAIRE');

-- Insertion des livres
INSERT INTO livres (titre, auteur, isbn, categorie, annee, nb_exemplaires, nb_disponibles) VALUES
('Le Seigneur des Anneaux', 'J.R.R. Tolkien', '978-2266154348', 'Fantasy', 1954, 5, 3),
('1984', 'George Orwell', '978-0451524935', 'Science-Fiction', 1949, 4, 4),
('Harry Potter à l''école des sorciers', 'J.K. Rowling', '978-2070643028', 'Fantasy', 1997, 6, 4),
('Le Petit Prince', 'Antoine de Saint-Exupéry', '978-2070612758', 'Jeunesse', 1943, 8, 7),
('Les Misérables', 'Victor Hugo', '978-2253096337', 'Classique', 1862, 3, 2),
('L''Étranger', 'Albert Camus', '978-2070360024', 'Classique', 1942, 4, 3),
('Pride and Prejudice', 'Jane Austen', '978-0141439518', 'Romance', 1813, 3, 3),
('To Kill a Mockingbird', 'Harper Lee', '978-0061120084', 'Classique', 1960, 5, 4),
('The Great Gatsby', 'F. Scott Fitzgerald', '978-0743273565', 'Classique', 1925, 4, 3),
('Dune', 'Frank Herbert', '978-0441172719', 'Science-Fiction', 1965, 3, 2),
('Le Comte de Monte-Cristo', 'Alexandre Dumas', '978-2253098058', 'Aventure', 1844, 4, 4),
('Germinal', 'Émile Zola', '978-2253004226', 'Classique', 1885, 3, 3),
('Crime et Châtiment', 'Fiodor Dostoïevski', '978-2253082477', 'Classique', 1866, 2, 2),
('Fondation', 'Isaac Asimov', '978-2070415557', 'Science-Fiction', 1951, 3, 2),
('Le Hobbit', 'J.R.R. Tolkien', '978-2253049111', 'Fantasy', 1937, 5, 4),
('Les Fleurs du Mal', 'Charles Baudelaire', '978-2253006046', 'Poésie', 1857, 2, 2),
('Madame Bovary', 'Gustave Flaubert', '978-2253004271', 'Classique', 1857, 3, 2),
('L''Alchimiste', 'Paulo Coelho', '978-2290339473', 'Développement personnel', 1988, 4, 3),
('Ne tirez pas sur l''oiseau moqueur', 'Harper Lee', '978-2253151081', 'Classique', 1960, 3, 3),
('Fahrenheit 451', 'Ray Bradbury', '978-2070360048', 'Science-Fiction', 1953, 3, 2);

-- Insertion des emprunts
INSERT INTO emprunts (id_user, id_livre, date_emprunt, date_retour_prevue, date_retour_effective) VALUES
(2, 1, '2025-12-15', '2026-01-15', NULL),
(2, 10, '2025-12-20', '2026-01-20', NULL),
(3, 3, '2025-12-10', '2026-01-10', NULL),
(4, 5, '2025-11-25', '2025-12-25', '2025-12-28'),
(5, 9, '2025-12-01', '2026-01-01', NULL),
(3, 14, '2025-12-18', '2026-01-18', NULL),
(4, 17, '2025-12-05', '2026-01-05', NULL),
(5, 15, '2025-12-12', '2026-01-12', NULL),
(2, 18, '2025-11-20', '2025-12-20', '2025-12-19'),
(3, 20, '2025-12-22', '2026-01-22', NULL);

-- Insertion des notifications
INSERT INTO notifications (id_emprunt, type, date_envoi, lue) VALUES
(1, 'RAPPEL', '2026-01-05', false),
(2, 'RAPPEL', '2026-01-10', false),
(3, 'RAPPEL', '2026-01-08', false),
(4, 'RETOUR', '2025-12-28', true),
(5, 'RAPPEL', '2025-12-28', false),
(7, 'RAPPEL', '2026-01-03', false);
