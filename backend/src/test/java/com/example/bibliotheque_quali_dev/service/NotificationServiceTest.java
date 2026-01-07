package com.example.bibliotheque_quali_dev.service;

import com.example.bibliotheque_quali_dev.entity.Emprunt;
import com.example.bibliotheque_quali_dev.entity.Notification;
import com.example.bibliotheque_quali_dev.repository.EmpruntRepository;
import com.example.bibliotheque_quali_dev.repository.NotificationRepository;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmpruntRepository empruntRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private NotificationService notificationService;

    private Emprunt emprunt1;
    private Emprunt emprunt2;
    private Notification notification1;
    private Notification notification2;

    @BeforeEach
    void setUp() {
        emprunt1 = new Emprunt();
        emprunt1.setIdEmprunt(1);
        emprunt1.setIdUser(100);
        emprunt1.setIdLivre(1);
        emprunt1.setDateEmprunt(Date.valueOf(LocalDate.now().minusDays(5)));
        emprunt1.setDateRetourPrevue(Date.valueOf(LocalDate.now().plusDays(30)));
        emprunt1.setDateRetourEffective(null);

        emprunt2 = new Emprunt();
        emprunt2.setIdEmprunt(2);
        emprunt2.setIdUser(101);
        emprunt2.setIdLivre(2);
        emprunt2.setDateEmprunt(Date.valueOf(LocalDate.now().minusDays(35)));
        emprunt2.setDateRetourPrevue(Date.valueOf(LocalDate.now().minusDays(5)));
        emprunt2.setDateRetourEffective(null);

        notification1 = new Notification();
        notification1.setIdNotif(1);
        notification1.setIdEmprunt(1);
        notification1.setType("RAPPEL_J30");
        notification1.setDateEnvoi(Date.valueOf(LocalDate.now()));
        notification1.setLue(false);

        notification2 = new Notification();
        notification2.setIdNotif(2);
        notification2.setIdEmprunt(1);
        notification2.setType("RAPPEL_J5");
        notification2.setDateEnvoi(Date.valueOf(LocalDate.now()));
        notification2.setLue(true);
    }

    @Test
    void testSendRemindersJ30_CreatesNotifications() {
        LocalDate targetDate = LocalDate.now().plusDays(30);
        when(empruntRepository.findByDateRetourPrevueAndDateRetourEffectiveIsNull(Date.valueOf(targetDate)))
            .thenReturn(Arrays.asList(emprunt1));
        when(notificationRepository.existsByIdEmpruntAndType(1, "RAPPEL_J30")).thenReturn(false);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        notificationService.sendRemindersJ30();

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(notificationRepository, times(1)).existsByIdEmpruntAndType(1, "RAPPEL_J30");
    }

    @Test
    void testSendRemindersJ30_DoesNotDuplicate() {
        LocalDate targetDate = LocalDate.now().plusDays(30);
        when(empruntRepository.findByDateRetourPrevueAndDateRetourEffectiveIsNull(Date.valueOf(targetDate)))
            .thenReturn(Arrays.asList(emprunt1));
        when(notificationRepository.existsByIdEmpruntAndType(1, "RAPPEL_J30")).thenReturn(true);

        notificationService.sendRemindersJ30();

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void testSendRemindersJ5_CreatesNotifications() {
        LocalDate targetDate = LocalDate.now().plusDays(5);
        when(empruntRepository.findByDateRetourPrevueAndDateRetourEffectiveIsNull(Date.valueOf(targetDate)))
            .thenReturn(Arrays.asList(emprunt1));
        when(notificationRepository.existsByIdEmpruntAndType(1, "RAPPEL_J5")).thenReturn(false);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        notificationService.sendRemindersJ5();

        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(notificationRepository, times(1)).existsByIdEmpruntAndType(1, "RAPPEL_J5");
    }

    @Test
    void testSendOverdueNotifications_CreatesNotifications() {
        LocalDate today = LocalDate.now();
        when(empruntRepository.findOverdueEmprunts(Date.valueOf(today)))
            .thenReturn(Arrays.asList(emprunt2));
        when(notificationRepository.existsByIdEmpruntAndTypeAndDateEnvoi(2, "RETARD", Date.valueOf(today)))
            .thenReturn(false);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        notificationService.sendOverdueNotifications();

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testSendOverdueNotifications_DoesNotDuplicateToday() {
        LocalDate today = LocalDate.now();
        when(empruntRepository.findOverdueEmprunts(Date.valueOf(today)))
            .thenReturn(Arrays.asList(emprunt2));
        when(notificationRepository.existsByIdEmpruntAndTypeAndDateEnvoi(2, "RETARD", Date.valueOf(today)))
            .thenReturn(true);

        notificationService.sendOverdueNotifications();

        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void testCreateNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        notificationService.createNotification(emprunt1, "RAPPEL_J30");

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testGetNotificationsByUser() {
        List<Notification> notifications = Arrays.asList(notification1, notification2);
        when(notificationRepository.findNotificationsByUserId(100)).thenReturn(notifications);

        List<Notification> result = notificationService.getNotificationsByUser(100);

        assertEquals(2, result.size());
        verify(notificationRepository, times(1)).findNotificationsByUserId(100);
    }

    @Test
    void testGetUnreadNotifications() {
        List<Notification> unreadNotifications = Arrays.asList(notification1);
        when(notificationRepository.findUnreadByUserId(100)).thenReturn(unreadNotifications);

        List<Notification> result = notificationService.getUnreadNotifications(100);

        assertEquals(1, result.size());
        assertFalse(result.get(0).isLue());
        verify(notificationRepository, times(1)).findUnreadByUserId(100);
    }

    @Test
    void testMarkAsRead_Success() {
        when(notificationRepository.findById(1)).thenReturn(Optional.of(notification1));
        notification1.setLue(true);
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        Notification result = notificationService.markAsRead(1);

        assertNotNull(result);
        assertTrue(result.isLue());
        verify(notificationRepository, times(1)).findById(1);
        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testMarkAsRead_NotFound() {
        when(notificationRepository.findById(999)).thenReturn(Optional.empty());

        Notification result = notificationService.markAsRead(999);

        assertNull(result);
        verify(notificationRepository, times(1)).findById(999);
        verify(notificationRepository, never()).save(any(Notification.class));
    }

    @Test
    void testCreateReturnNotification() {
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification1);

        notificationService.createReturnNotification(emprunt1);

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    void testCountUnread() {
        when(notificationRepository.countUnreadByUserId(100)).thenReturn(5L);

        long count = notificationService.countUnread(100);

        assertEquals(5L, count);
        verify(notificationRepository, times(1)).countUnreadByUserId(100);
    }
}
