package com.example.bibliotheque_quali_dev.service;

import com.example.bibliotheque_quali_dev.dto.EmpruntCreateRequest;
import com.example.bibliotheque_quali_dev.entity.Emprunt;
import com.example.bibliotheque_quali_dev.repository.EmpruntRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour EmpruntService.
 * Couvre la création, le retour, la prolongation et les validations métier.
 */
@ExtendWith(MockitoExtension.class)
class EmpruntServiceTest {

    @Mock
    private EmpruntRepository empruntRepository;

    @Mock
    private LivreService livreService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private EmpruntService empruntService;

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
        emprunt2.setDateEmprunt(Date.valueOf(LocalDate.now().minusDays(35)));
        emprunt2.setDateRetourPrevue(Date.valueOf(LocalDate.now().minusDays(5)));
        emprunt2.setDateRetourEffective(null);

        createRequest = new EmpruntCreateRequest();
        createRequest.setIdUser(100);
        createRequest.setIdLivre(3);
    }

    @Nested
    @DisplayName("Tests findAll et findById")
    class FindTests {

        @Test
        @DisplayName("Doit retourner tous les emprunts")
        void findAll_ReturnsAllEmprunts() {
            when(empruntRepository.findAll()).thenReturn(Arrays.asList(emprunt1, emprunt2));

            List<Emprunt> result = empruntService.findAll();

            assertEquals(2, result.size());
            verify(empruntRepository, times(1)).findAll();
        }

        @Test
        @DisplayName("Doit retourner un emprunt par ID")
        void findById_ExistingId_ReturnsEmprunt() {
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt1));

            Emprunt result = empruntService.findById(1);

            assertNotNull(result);
            assertEquals(100, result.getIdUser());
            verify(empruntRepository, times(1)).findById(1);
        }

        @Test
        @DisplayName("Doit lancer une exception si ID non trouvé")
        void findById_NonExistingId_ThrowsException() {
            when(empruntRepository.findById(999)).thenReturn(Optional.empty());

            assertThrows(RuntimeException.class, () -> empruntService.findById(999));
        }

        @Test
        @DisplayName("Doit retourner les emprunts actifs d'un utilisateur")
        void findActiveByUserId_ReturnsActiveEmprunts() {
            when(empruntRepository.findByIdUserAndDateRetourEffectiveIsNull(100))
                    .thenReturn(Arrays.asList(emprunt1, emprunt2));

            List<Emprunt> result = empruntService.findActiveByUserId(100);

            assertEquals(2, result.size());
            verify(empruntRepository, times(1))
                    .findByIdUserAndDateRetourEffectiveIsNull(100);
        }
    }

    @Nested
    @DisplayName("Tests create")
    class CreateTests {

        @Test
        @DisplayName("Doit créer un emprunt avec succès")
        void create_ValidRequest_Success() {
            when(empruntRepository.findByIdUserAndDateRetourEffectiveIsNull(100))
                    .thenReturn(Arrays.asList(emprunt1)); // 1 emprunt actif < 5
            when(livreService.isAvailable(3)).thenReturn(true);
            when(empruntRepository.save(any(Emprunt.class))).thenAnswer(i -> {
                Emprunt e = i.getArgument(0);
                e.setIdEmprunt(10);
                return e;
            });

            Emprunt result = empruntService.create(createRequest);

            assertNotNull(result);
            assertEquals(100, result.getIdUser());
            assertEquals(3, result.getIdLivre());
            assertNotNull(result.getDateEmprunt());
            assertNotNull(result.getDateRetourPrevue());
            assertNull(result.getDateRetourEffective());
            verify(livreService, times(1)).decrementDisponibles(3);
        }

        @Test
        @DisplayName("Doit échouer si limite d'emprunts atteinte (5)")
        void create_MaxEmpruntsReached_ThrowsException() {
            List<Emprunt> fiveEmprunts = Arrays.asList(
                    emprunt1, emprunt1, emprunt1, emprunt1, emprunt1
            );
            when(empruntRepository.findByIdUserAndDateRetourEffectiveIsNull(100))
                    .thenReturn(fiveEmprunts);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> empruntService.create(createRequest));

            assertTrue(exception.getMessage().contains("Limite d'emprunts atteinte"));
            verify(livreService, never()).decrementDisponibles(any());
        }

        @Test
        @DisplayName("Doit échouer si livre non disponible")
        void create_BookNotAvailable_ThrowsException() {
            when(empruntRepository.findByIdUserAndDateRetourEffectiveIsNull(100))
                    .thenReturn(Collections.emptyList());
            when(livreService.isAvailable(3)).thenReturn(false);

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> empruntService.create(createRequest));

            assertTrue(exception.getMessage().contains("n'est pas disponible"));
            verify(empruntRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests returnBook")
    class ReturnBookTests {

        @Test
        @DisplayName("Doit retourner un livre avec succès")
        void returnBook_Success() {
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt1));
            when(empruntRepository.save(any(Emprunt.class))).thenReturn(emprunt1);

            Emprunt result = empruntService.returnBook(1);

            assertNotNull(result.getDateRetourEffective());
            verify(livreService, times(1)).incrementDisponibles(1);
            verify(notificationService, times(1)).createReturnNotification(emprunt1);
        }

        @Test
        @DisplayName("Doit échouer si livre déjà retourné")
        void returnBook_AlreadyReturned_ThrowsException() {
            emprunt1.setDateRetourEffective(Date.valueOf(LocalDate.now()));
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt1));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> empruntService.returnBook(1));

            assertTrue(exception.getMessage().contains("déjà été retourné"));
            verify(livreService, never()).incrementDisponibles(any());
        }
    }

    @Nested
    @DisplayName("Tests extend")
    class ExtendTests {

        @Test
        @DisplayName("Doit prolonger un emprunt de 15 jours")
        void extend_ValidEmprunt_Success() {
            LocalDate originalReturnDate = emprunt1.getDateRetourPrevue().toLocalDate();
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt1));
            when(empruntRepository.save(any(Emprunt.class))).thenReturn(emprunt1);

            Emprunt result = empruntService.extend(1);

            assertEquals(originalReturnDate.plusDays(15), 
                    result.getDateRetourPrevue().toLocalDate());
        }

        @Test
        @DisplayName("Doit échouer si emprunt déjà retourné")
        void extend_AlreadyReturned_ThrowsException() {
            emprunt1.setDateRetourEffective(Date.valueOf(LocalDate.now()));
            when(empruntRepository.findById(1)).thenReturn(Optional.of(emprunt1));

            assertThrows(RuntimeException.class, () -> empruntService.extend(1));
        }

        @Test
        @DisplayName("Doit échouer si emprunt en retard")
        void extend_OverdueEmprunt_ThrowsException() {
            // emprunt2 est en retard (dateRetourPrevue dans le passé)
            when(empruntRepository.findById(2)).thenReturn(Optional.of(emprunt2));

            RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> empruntService.extend(2));

            assertTrue(exception.getMessage().contains("en retard"));
        }
    }

    @Nested
    @DisplayName("Tests emprunts en retard")
    class OverdueTests {

        @Test
        @DisplayName("Doit retourner les emprunts en retard")
        void findOverdueEmprunts_ReturnsOverdueList() {
            when(empruntRepository.findOverdueEmprunts(any(Date.class)))
                    .thenReturn(Arrays.asList(emprunt2));

            List<Emprunt> result = empruntService.findOverdueEmprunts();

            assertEquals(1, result.size());
            verify(empruntRepository, times(1)).findOverdueEmprunts(any(Date.class));
        }
    }

    @Nested
    @DisplayName("Tests historique")
    class HistoryTests {

        @Test
        @DisplayName("Doit retourner l'historique complet d'un utilisateur")
        void findHistoryByUserId_ReturnsAllEmprunts() {
            when(empruntRepository.findByIdUser(100))
                    .thenReturn(Arrays.asList(emprunt1, emprunt2));

            List<Emprunt> result = empruntService.findHistoryByUserId(100);

            assertEquals(2, result.size());
            verify(empruntRepository, times(1)).findByIdUser(100);
        }
    }
}
