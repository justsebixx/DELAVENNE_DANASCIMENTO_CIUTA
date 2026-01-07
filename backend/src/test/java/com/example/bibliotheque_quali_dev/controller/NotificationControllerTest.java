package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.entity.Notification;
import com.example.bibliotheque_quali_dev.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private Notification notification1;
    private Notification notification2;
    private Notification notification3;

    @BeforeEach
    void setUp() {
        notification1 = new Notification();
        notification1.setIdNotif(1);
        notification1.setIdEmprunt(1);
        notification1.setType("RAPPEL_J30");
        notification1.setDateEnvoi(Date.valueOf(LocalDate.now()));
        notification1.setLue(false);

        notification2 = new Notification();
        notification2.setIdNotif(2);
        notification2.setIdEmprunt(2);
        notification2.setType("RAPPEL_J5");
        notification2.setDateEnvoi(Date.valueOf(LocalDate.now().minusDays(5)));
        notification2.setLue(false);

        notification3 = new Notification();
        notification3.setIdNotif(3);
        notification3.setIdEmprunt(3);
        notification3.setType("RETOUR");
        notification3.setDateEnvoi(Date.valueOf(LocalDate.now().minusDays(10)));
        notification3.setLue(true);
    }

    @Test
    void testGetUserNotifications_Success() {
        List<Notification> notifications = Arrays.asList(notification1, notification2, notification3);
        when(notificationService.getNotificationsByUser(100)).thenReturn(notifications);

        ResponseEntity<List<Notification>> response = notificationController.getUserNotifications(100);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(3, response.getBody().size());
        verify(notificationService, times(1)).getNotificationsByUser(100);
    }

    @Test
    void testGetUserNotifications_EmptyList() {
        when(notificationService.getNotificationsByUser(100)).thenReturn(Arrays.asList());

        ResponseEntity<List<Notification>> response = notificationController.getUserNotifications(100);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
        verify(notificationService, times(1)).getNotificationsByUser(100);
    }

    @Test
    void testGetUnreadNotifications_Success() {
        List<Notification> unreadNotifications = Arrays.asList(notification1, notification2);
        when(notificationService.getUnreadNotifications(100)).thenReturn(unreadNotifications);

        ResponseEntity<List<Notification>> response = notificationController.getUnreadNotifications(100);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertFalse(response.getBody().get(0).isLue());
        assertFalse(response.getBody().get(1).isLue());
        verify(notificationService, times(1)).getUnreadNotifications(100);
    }

    @Test
    void testCountUnreadNotifications_Success() {
        when(notificationService.countUnread(100)).thenReturn(2L);

        ResponseEntity<Map<String, Long>> response = notificationController.countUnreadNotifications(100);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2L, response.getBody().get("count"));
        verify(notificationService, times(1)).countUnread(100);
    }

    @Test
    void testCountUnreadNotifications_Zero() {
        when(notificationService.countUnread(100)).thenReturn(0L);

        ResponseEntity<Map<String, Long>> response = notificationController.countUnreadNotifications(100);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(0L, response.getBody().get("count"));
        verify(notificationService, times(1)).countUnread(100);
    }

    @Test
    void testMarkAsRead_Success() {
        notification1.setLue(true);
        when(notificationService.markAsRead(1)).thenReturn(notification1);

        ResponseEntity<Notification> response = notificationController.markAsRead(1);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isLue());
        assertEquals(1, response.getBody().getIdNotif());
        verify(notificationService, times(1)).markAsRead(1);
    }

    @Test
    void testMarkAsRead_NotFound() {
        when(notificationService.markAsRead(999)).thenReturn(null);

        ResponseEntity<Notification> response = notificationController.markAsRead(999);

        assertEquals(404, response.getStatusCodeValue());
        assertNull(response.getBody());
        verify(notificationService, times(1)).markAsRead(999);
    }

    @Test
    void testMarkAllAsRead_Success() {
        List<Notification> unreadNotifications = Arrays.asList(notification1, notification2);
        when(notificationService.getUnreadNotifications(100)).thenReturn(unreadNotifications);
        when(notificationService.markAsRead(1)).thenReturn(notification1);
        when(notificationService.markAsRead(2)).thenReturn(notification2);

        ResponseEntity<Map<String, String>> response = notificationController.markAllAsRead(100);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("All notifications marked as read", response.getBody().get("message"));
        verify(notificationService, times(1)).getUnreadNotifications(100);
        verify(notificationService, times(2)).markAsRead(anyInt());
    }

    @Test
    void testMarkAllAsRead_NoUnreadNotifications() {
        when(notificationService.getUnreadNotifications(100)).thenReturn(Arrays.asList());

        ResponseEntity<Map<String, String>> response = notificationController.markAllAsRead(100);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("All notifications marked as read", response.getBody().get("message"));
        verify(notificationService, times(1)).getUnreadNotifications(100);
        verify(notificationService, never()).markAsRead(anyInt());
    }
}
