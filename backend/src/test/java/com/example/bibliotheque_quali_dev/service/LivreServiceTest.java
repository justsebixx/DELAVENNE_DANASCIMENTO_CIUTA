package com.example.bibliotheque_quali_dev.service;

import com.example.bibliotheque_quali_dev.entity.Livre;
import com.example.bibliotheque_quali_dev.repository.LivreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour LivreService.
 * Couvre les opérations CRUD et la logique métier.
 */
@ExtendWith(MockitoExtension.class)
class LivreServiceTest {

    @Mock
    private LivreRepository livreRepository;

    @InjectMocks
    private LivreService livreService;

    private Livre livre1;
    private Livre livre2;

    @BeforeEach
    void setUp() {
        livre1 = new Livre(1, "Le Petit Prince", "Antoine de Saint-Exupéry", 
                "Jeunesse", "978-2070612758", 1943, 5, 3);
        livre2 = new Livre(2, "1984", "George Orwell", 
                "Science-Fiction", "978-0451524935", 1949, 4, 4);
    }

    @Nested
    @DisplayName("Tests findAll")
    class FindAllTests {
        
        @Test
        @DisplayName("Doit retourner tous les livres")
        void findAll_ReturnsAllBooks() {
            when(livreRepository.findAll()).thenReturn(Arrays.asList(livre1, livre2));

            List<Livre> result = livreService.findAll();

            assertEquals(2, result.size());
            verify(livreRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Doit retourner une liste vide si aucun livre")
        void findAll_ReturnsEmptyList() {
            when(livreRepository.findAll()).thenReturn(Arrays.asList());

            List<Livre> result = livreService.findAll();

            assertTrue(result.isEmpty());
            verify(livreRepository, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Tests findById")
    class FindByIdTests {

        @Test
        @DisplayName("Doit retourner le livre correspondant à l'ID")
        void findById_ExistingId_ReturnsBook() {
            when(livreRepository.findById(1)).thenReturn(Optional.of(livre1));

            Livre result = livreService.findById(1);

            assertNotNull(result);
            assertEquals("Le Petit Prince", result.getTitre());
            verify(livreRepository, times(1)).findById(1);
        }

        @Test
        @DisplayName("Doit lancer une exception si ID non trouvé")
        void findById_NonExistingId_ThrowsException() {
            when(livreRepository.findById(999)).thenReturn(Optional.empty());

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> livreService.findById(999));

            assertTrue(exception.getMessage().contains("non trouvé"));
            verify(livreRepository, times(1)).findById(999);
        }
    }

    @Nested
    @DisplayName("Tests create")
    class CreateTests {

        @Test
        @DisplayName("Doit créer un livre valide")
        void create_ValidBook_Success() {
            Livre newLivre = new Livre(null, "Nouveau Livre", "Auteur", 
                    "Catégorie", "978-1234567890", 2024, 10, 10);
            when(livreRepository.save(any(Livre.class))).thenReturn(newLivre);

            Livre result = livreService.create(newLivre);

            assertNotNull(result);
            assertEquals("Nouveau Livre", result.getTitre());
            verify(livreRepository, times(1)).save(newLivre);
        }

        @Test
        @DisplayName("Doit échouer si disponibles > exemplaires")
        void create_InvalidDisponibles_ThrowsException() {
            Livre invalidLivre = new Livre(null, "Livre Invalide", "Auteur", 
                    "Catégorie", "978-1234567890", 2024, 5, 10);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> livreService.create(invalidLivre));

            assertTrue(exception.getMessage().contains("ne peut pas dépasser"));
            verify(livreRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests update")
    class UpdateTests {

        @Test
        @DisplayName("Doit mettre à jour un livre existant")
        void update_ExistingBook_Success() {
            Livre updatedDetails = new Livre(1, "Titre Modifié", "Nouvel Auteur", 
                    "Nouvelle Catégorie", "978-1234567890", 2024, 10, 8);
            when(livreRepository.findById(1)).thenReturn(Optional.of(livre1));
            when(livreRepository.save(any(Livre.class))).thenReturn(updatedDetails);

            Livre result = livreService.update(1, updatedDetails);

            assertNotNull(result);
            assertEquals("Titre Modifié", result.getTitre());
            verify(livreRepository, times(1)).findById(1);
            verify(livreRepository, times(1)).save(any(Livre.class));
        }

        @Test
        @DisplayName("Doit échouer si livre non trouvé")
        void update_NonExistingBook_ThrowsException() {
            Livre updatedDetails = new Livre(999, "Titre", "Auteur", 
                    "Catégorie", "978-1234567890", 2024, 10, 8);
            when(livreRepository.findById(999)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> livreService.update(999, updatedDetails));

            verify(livreRepository, never()).save(any());
        }

        @Test
        @DisplayName("Doit échouer si disponibles > exemplaires après update")
        void update_InvalidDisponibles_ThrowsException() {
            Livre updatedDetails = new Livre(1, "Titre", "Auteur", 
                    "Catégorie", "978-1234567890", 2024, 5, 10);
            when(livreRepository.findById(1)).thenReturn(Optional.of(livre1));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> livreService.update(1, updatedDetails));

            assertTrue(exception.getMessage().contains("ne peut pas dépasser"));
            verify(livreRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests delete")
    class DeleteTests {

        @Test
        @DisplayName("Doit supprimer un livre existant")
        void delete_ExistingId_Success() {
            when(livreRepository.existsById(1)).thenReturn(true);
            doNothing().when(livreRepository).deleteById(1);

            assertDoesNotThrow(() -> livreService.delete(1));

            verify(livreRepository, times(1)).existsById(1);
            verify(livreRepository, times(1)).deleteById(1);
        }

        @Test
        @DisplayName("Doit échouer si livre non trouvé")
        void delete_NonExistingId_ThrowsException() {
            when(livreRepository.existsById(999)).thenReturn(false);

            assertThrows(RuntimeException.class, () -> livreService.delete(999));

            verify(livreRepository, never()).deleteById(any());
        }
    }

    @Nested
    @DisplayName("Tests disponibilité")
    class AvailabilityTests {

        @Test
        @DisplayName("Doit retourner true si livre disponible")
        void isAvailable_BookAvailable_ReturnsTrue() {
            when(livreRepository.findById(1)).thenReturn(Optional.of(livre1));

            boolean result = livreService.isAvailable(1);

            assertTrue(result);
        }

        @Test
        @DisplayName("Doit retourner false si aucun exemplaire disponible")
        void isAvailable_NoExemplairesAvailable_ReturnsFalse() {
            livre1.setNbDisponibles(0);
            when(livreRepository.findById(1)).thenReturn(Optional.of(livre1));

            boolean result = livreService.isAvailable(1);

            assertFalse(result);
        }

        @Test
        @DisplayName("Doit décrémenter les exemplaires disponibles")
        void decrementDisponibles_Success() {
            when(livreRepository.findById(1)).thenReturn(Optional.of(livre1));
            when(livreRepository.save(any(Livre.class))).thenReturn(livre1);

            livreService.decrementDisponibles(1);

            assertEquals(2, livre1.getNbDisponibles());
            verify(livreRepository, times(1)).save(livre1);
        }

        @Test
        @DisplayName("Doit échouer si aucun exemplaire à décrémenter")
        void decrementDisponibles_NoExemplaires_ThrowsException() {
            livre1.setNbDisponibles(0);
            when(livreRepository.findById(1)).thenReturn(Optional.of(livre1));

            assertThrows(RuntimeException.class, 
                    () -> livreService.decrementDisponibles(1));

            verify(livreRepository, never()).save(any());
        }

        @Test
        @DisplayName("Doit incrémenter les exemplaires disponibles")
        void incrementDisponibles_Success() {
            when(livreRepository.findById(1)).thenReturn(Optional.of(livre1));
            when(livreRepository.save(any(Livre.class))).thenReturn(livre1);

            livreService.incrementDisponibles(1);

            assertEquals(4, livre1.getNbDisponibles());
            verify(livreRepository, times(1)).save(livre1);
        }

        @Test
        @DisplayName("Doit échouer si déjà au maximum d'exemplaires")
        void incrementDisponibles_AlreadyMax_ThrowsException() {
            livre1.setNbDisponibles(5); // Égal à nbExemplaires
            when(livreRepository.findById(1)).thenReturn(Optional.of(livre1));

            assertThrows(RuntimeException.class, 
                    () -> livreService.incrementDisponibles(1));

            verify(livreRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests search et catégories")
    class SearchTests {

        @Test
        @DisplayName("Doit rechercher les livres selon les critères")
        void search_WithCriteria_ReturnsFilteredBooks() {
            when(livreRepository.searchBooks("Petit", null, null, null))
                    .thenReturn(Arrays.asList(livre1));

            List<Livre> result = livreService.search("Petit", null, null, null);

            assertEquals(1, result.size());
            assertEquals("Le Petit Prince", result.get(0).getTitre());
        }

        @Test
        @DisplayName("Doit retourner toutes les catégories distinctes")
        void findAllCategories_ReturnsDistinctCategories() {
            when(livreRepository.findDistinctCategories())
                    .thenReturn(Arrays.asList("Jeunesse", "Science-Fiction", "Classique"));

            List<String> result = livreService.findAllCategories();

            assertEquals(3, result.size());
            assertTrue(result.contains("Jeunesse"));
        }
    }
}
