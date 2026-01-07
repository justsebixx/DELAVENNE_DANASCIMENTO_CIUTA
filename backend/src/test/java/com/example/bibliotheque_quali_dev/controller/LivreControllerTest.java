package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.entity.Livre;
import com.example.bibliotheque_quali_dev.service.LivreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour LivreController.
 * Vérifie les endpoints REST et les codes de réponse HTTP.
 */
@ExtendWith(MockitoExtension.class)
class LivreControllerTest {

    @Mock
    private LivreService livreService;

    @InjectMocks
    private LivreController livreController;

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
    @DisplayName("Tests GET /livres")
    class GetAllLivresTests {

        @Test
        @DisplayName("Doit retourner 200 avec la liste des livres")
        void getAllLivres_ReturnsOkWithList() {
            when(livreService.findAll()).thenReturn(Arrays.asList(livre1, livre2));

            ResponseEntity<List<Livre>> response = livreController.getAllLivres();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertNotNull(response.getBody());
            assertEquals(2, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("Tests GET /livres/{id}")
    class GetLivreByIdTests {

        @Test
        @DisplayName("Doit retourner 200 si livre trouvé")
        void getLivreById_ExistingId_ReturnsOk() {
            when(livreService.findById(1)).thenReturn(livre1);

            ResponseEntity<Livre> response = livreController.getLivreById(1);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Le Petit Prince", response.getBody().getTitre());
        }

        @Test
        @DisplayName("Doit retourner 404 si livre non trouvé")
        void getLivreById_NonExistingId_ReturnsNotFound() {
            when(livreService.findById(999)).thenThrow(new RuntimeException("Non trouvé"));

            ResponseEntity<Livre> response = livreController.getLivreById(999);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Tests POST /livres")
    class CreateLivreTests {

        @Test
        @DisplayName("Doit retourner 201 à la création")
        void createLivre_ValidBook_ReturnsCreated() {
            when(livreService.create(any(Livre.class))).thenReturn(livre1);

            ResponseEntity<Livre> response = livreController.createLivre(livre1);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
            assertNotNull(response.getBody());
        }

        @Test
        @DisplayName("Doit retourner 400 si données invalides")
        void createLivre_InvalidData_ReturnsBadRequest() {
            when(livreService.create(any(Livre.class)))
                    .thenThrow(new RuntimeException("Données invalides"));

            ResponseEntity<Livre> response = livreController.createLivre(livre1);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Tests PUT /livres/{id}")
    class UpdateLivreTests {

        @Test
        @DisplayName("Doit retourner 200 à la mise à jour")
        void updateLivre_ExistingBook_ReturnsOk() {
            when(livreService.update(eq(1), any(Livre.class))).thenReturn(livre1);

            ResponseEntity<Livre> response = livreController.updateLivre(1, livre1);

            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        @Test
        @DisplayName("Doit retourner 404 si livre non trouvé")
        void updateLivre_NonExistingBook_ReturnsNotFound() {
            when(livreService.update(eq(999), any(Livre.class)))
                    .thenThrow(new RuntimeException("Non trouvé"));

            ResponseEntity<Livre> response = livreController.updateLivre(999, livre1);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Tests DELETE /livres/{id}")
    class DeleteLivreTests {

        @Test
        @DisplayName("Doit retourner 204 à la suppression")
        void deleteLivre_ExistingBook_ReturnsNoContent() {
            doNothing().when(livreService).delete(1);

            ResponseEntity<Void> response = livreController.deleteLivre(1);

            assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        }

        @Test
        @DisplayName("Doit retourner 404 si livre non trouvé")
        void deleteLivre_NonExistingBook_ReturnsNotFound() {
            doThrow(new RuntimeException("Non trouvé")).when(livreService).delete(999);

            ResponseEntity<Void> response = livreController.deleteLivre(999);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Tests GET /livres/search")
    class SearchLivresTests {

        @Test
        @DisplayName("Doit retourner 200 avec résultats de recherche")
        void searchLivres_WithCriteria_ReturnsOk() {
            when(livreService.search("Petit", null, null, null))
                    .thenReturn(Arrays.asList(livre1));

            ResponseEntity<List<Livre>> response = livreController.searchLivres(
                    "Petit", null, null, null);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("Tests GET /livres/categories")
    class GetCategoriesTests {

        @Test
        @DisplayName("Doit retourner 200 avec les catégories")
        void getCategories_ReturnsOkWithList() {
            when(livreService.findAllCategories())
                    .thenReturn(Arrays.asList("Jeunesse", "Science-Fiction", "Classique"));

            ResponseEntity<List<String>> response = livreController.getCategories();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(3, response.getBody().size());
        }
    }
}
