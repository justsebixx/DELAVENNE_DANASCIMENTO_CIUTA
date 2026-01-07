package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.config.AuthPrincipal;
import com.example.bibliotheque_quali_dev.dto.EmpruntCreateRequest;
import com.example.bibliotheque_quali_dev.entity.Emprunt;
import com.example.bibliotheque_quali_dev.service.EmpruntService;
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
import org.springframework.mock.web.MockHttpServletRequest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour EmpruntController.
 * Vérifie les endpoints REST pour la gestion des emprunts.
 */
@ExtendWith(MockitoExtension.class)
class EmpruntControllerTest {

    @Mock
    private EmpruntService empruntService;

    @InjectMocks
    private EmpruntController empruntController;

    private Emprunt emprunt1;
    private Emprunt emprunt2;
    private EmpruntCreateRequest createRequest;

    @BeforeEach
    void setUp() {
        emprunt1 = new Emprunt();
        emprunt1.setIdEmprunt(1);
        emprunt1.setIdUser(100);
        emprunt1.setIdLivre(1);
        emprunt1.setDateEmprunt(Date.valueOf(LocalDate.now().minusDays(5)));
        emprunt1.setDateRetourPrevue(Date.valueOf(LocalDate.now().plusDays(25)));
        emprunt1.setDateRetourEffective(null);

        emprunt2 = new Emprunt();
        emprunt2.setIdEmprunt(2);
        emprunt2.setIdUser(100);
        emprunt2.setIdLivre(2);
        emprunt2.setDateEmprunt(Date.valueOf(LocalDate.now().minusDays(10)));
        emprunt2.setDateRetourPrevue(Date.valueOf(LocalDate.now().plusDays(20)));
        emprunt2.setDateRetourEffective(null);

        createRequest = new EmpruntCreateRequest();
        createRequest.setIdUser(100);
        createRequest.setIdLivre(3);
    }

    @Nested
    @DisplayName("Tests GET /emprunts")
    class GetAllEmpruntsTests {

        @Test
        @DisplayName("Doit retourner 200 avec la liste des emprunts")
        void getAllEmprunts_ReturnsOkWithList() {
            when(empruntService.findAll()).thenReturn(Arrays.asList(emprunt1, emprunt2));

            ResponseEntity<List<Emprunt>> response = empruntController.getAllEmprunts();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("Tests GET /emprunts/{id}")
    class GetEmpruntByIdTests {

        @Test
        @DisplayName("Doit retourner 200 si emprunt trouvé")
        void getEmpruntById_ExistingId_ReturnsOk() {
            when(empruntService.findById(1)).thenReturn(emprunt1);

            ResponseEntity<Emprunt> response = empruntController.getEmpruntById(1);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(100, response.getBody().getIdUser());
        }

        @Test
        @DisplayName("Doit retourner 404 si emprunt non trouvé")
        void getEmpruntById_NonExistingId_ReturnsNotFound() {
            when(empruntService.findById(999)).thenThrow(new RuntimeException("Non trouvé"));

            ResponseEntity<Emprunt> response = empruntController.getEmpruntById(999);

            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        }
    }

    @Nested
    @DisplayName("Tests GET /emprunts/user/{userId}/actifs")
    class GetActiveEmpruntsByUserTests {

        @Test
        @DisplayName("Doit retourner 200 avec les emprunts actifs")
        void getActiveEmpruntsByUser_ReturnsOk() {
            when(empruntService.findActiveByUserId(100))
                    .thenReturn(Arrays.asList(emprunt1, emprunt2));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("auth.principal", new AuthPrincipal(100, "ETUDIANT"));

            ResponseEntity<List<Emprunt>> response = empruntController.getActiveEmpruntsByUser(100, request);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("Tests GET /emprunts/user/{userId}")
    class GetEmpruntHistoryByUserTests {

        @Test
        @DisplayName("Doit retourner 200 avec l'historique")
        void getEmpruntHistoryByUser_ReturnsOk() {
            when(empruntService.findHistoryByUserId(100))
                    .thenReturn(Arrays.asList(emprunt1, emprunt2));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("auth.principal", new AuthPrincipal(100, "ETUDIANT"));

            ResponseEntity<List<Emprunt>> response = empruntController.getEmpruntHistoryByUser(100, request);

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(2, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("Tests GET /emprunts/retards")
    class GetOverdueEmpruntsTests {

        @Test
        @DisplayName("Doit retourner 200 avec les emprunts en retard")
        void getOverdueEmprunts_ReturnsOk() {
            when(empruntService.findOverdueEmprunts())
                    .thenReturn(Arrays.asList(emprunt2));

            ResponseEntity<List<Emprunt>> response = empruntController.getOverdueEmprunts();

            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(1, response.getBody().size());
        }
    }

    @Nested
    @DisplayName("Tests POST /emprunts")
    class CreateEmpruntTests {

        @Test
        @DisplayName("Doit retourner 201 à la création")
        void createEmprunt_ValidRequest_ReturnsCreated() {
            when(empruntService.create(any(EmpruntCreateRequest.class))).thenReturn(emprunt1);

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("auth.principal", new AuthPrincipal(100, "ETUDIANT"));

            ResponseEntity<?> response = empruntController.createEmprunt(createRequest, request);

            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        @Test
        @DisplayName("Doit retourner 400 si limite atteinte")
        void createEmprunt_LimitReached_ReturnsBadRequest() {
            when(empruntService.create(any(EmpruntCreateRequest.class)))
                    .thenThrow(new RuntimeException("Limite d'emprunts atteinte"));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("auth.principal", new AuthPrincipal(100, "ETUDIANT"));

            ResponseEntity<?> response = empruntController.createEmprunt(createRequest, request);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertTrue(response.getBody().toString().contains("Limite"));
        }

        @Test
        @DisplayName("Doit retourner 400 si livre non disponible")
        void createEmprunt_BookNotAvailable_ReturnsBadRequest() {
            when(empruntService.create(any(EmpruntCreateRequest.class)))
                    .thenThrow(new RuntimeException("Livre non disponible"));

            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setAttribute("auth.principal", new AuthPrincipal(100, "ETUDIANT"));

            ResponseEntity<?> response = empruntController.createEmprunt(createRequest, request);

            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        }
    }
}
