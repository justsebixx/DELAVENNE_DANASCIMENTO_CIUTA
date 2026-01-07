package com.example.bibliotheque_quali_dev.service;

import com.example.bibliotheque_quali_dev.dto.LoginRequest;
import com.example.bibliotheque_quali_dev.dto.LoginResponse;
import com.example.bibliotheque_quali_dev.dto.RegisterRequest;
import com.example.bibliotheque_quali_dev.entity.Role;
import com.example.bibliotheque_quali_dev.entity.SessionToken;
import com.example.bibliotheque_quali_dev.entity.Utilisateur;
import com.example.bibliotheque_quali_dev.exception.UnauthorizedException;
import com.example.bibliotheque_quali_dev.exception.UtilisateurAlreadyExistsException;
import com.example.bibliotheque_quali_dev.repository.SessionTokenRepository;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires pour AuthService.
 * Couvre l'authentification, l'enregistrement et la gestion des sessions.
 */
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private SessionTokenRepository sessionTokenRepository;

    @Mock
    private TokenGenerator tokenGenerator;

    private AuthService authService;
    private BCryptPasswordEncoder passwordEncoder;

    private Utilisateur utilisateur;
    private LoginRequest loginRequest;
    private RegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        authService = new AuthService(utilisateurRepository, sessionTokenRepository, tokenGenerator);
        passwordEncoder = new BCryptPasswordEncoder();

        utilisateur = new Utilisateur();
        utilisateur.setIdUser(1);
        utilisateur.setNom("Dupont");
        utilisateur.setPrenom("Jean");
        utilisateur.setEmail("jean.dupont@email.com");
        utilisateur.setPasswordhash(passwordEncoder.encode("password123"));
        utilisateur.setRole(Role.ETUDIANT);

        loginRequest = new LoginRequest();
        loginRequest.setEmail("jean.dupont@email.com");
        loginRequest.setPassword("password123");

        registerRequest = new RegisterRequest();
        registerRequest.setNom("Martin");
        registerRequest.setPrenom("Sophie");
        registerRequest.setEmail("sophie.martin@email.com");
        registerRequest.setPassword("securePassword123");
    }

    @Nested
    @DisplayName("Tests login")
    class LoginTests {

        @Test
        @DisplayName("Doit authentifier un utilisateur avec succès")
        void login_ValidCredentials_ReturnsLoginResponse() {
            when(utilisateurRepository.findByEmail("jean.dupont@email.com"))
                    .thenReturn(Optional.of(utilisateur));
            when(tokenGenerator.generateToken()).thenReturn("generated-token-123");
            when(sessionTokenRepository.save(any(SessionToken.class)))
                    .thenAnswer(i -> i.getArgument(0));

            LoginResponse response = authService.login(loginRequest);

            assertNotNull(response);
            assertEquals("generated-token-123", response.getToken());
            assertEquals(1, response.getIdUser());
            assertEquals("Dupont", response.getNom());
            assertEquals("Jean", response.getPrenom());
            assertEquals("jean.dupont@email.com", response.getEmail());
            assertEquals("ETUDIANT", response.getRole());
            verify(sessionTokenRepository, times(1)).save(any(SessionToken.class));
        }

        @Test
        @DisplayName("Doit échouer si email non trouvé")
        void login_EmailNotFound_ThrowsUnauthorizedException() {
            when(utilisateurRepository.findByEmail("unknown@email.com"))
                    .thenReturn(Optional.empty());

            loginRequest.setEmail("unknown@email.com");

            UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                    () -> authService.login(loginRequest));

            assertTrue(exception.getMessage().contains("incorrect"));
            verify(sessionTokenRepository, never()).save(any());
        }

        @Test
        @DisplayName("Doit échouer si mot de passe incorrect")
        void login_WrongPassword_ThrowsUnauthorizedException() {
            when(utilisateurRepository.findByEmail("jean.dupont@email.com"))
                    .thenReturn(Optional.of(utilisateur));

            loginRequest.setPassword("wrongPassword");

            UnauthorizedException exception = assertThrows(UnauthorizedException.class,
                    () -> authService.login(loginRequest));

            assertTrue(exception.getMessage().contains("incorrect"));
            verify(sessionTokenRepository, never()).save(any());
        }
    }

    @Nested
    @DisplayName("Tests register")
    class RegisterTests {

        @Test
        @DisplayName("Doit enregistrer un nouvel utilisateur avec succès")
        void register_ValidRequest_ReturnsLoginResponse() {
            when(utilisateurRepository.findByEmail("sophie.martin@email.com"))
                    .thenReturn(Optional.empty());
            when(utilisateurRepository.save(any(Utilisateur.class)))
                    .thenAnswer(i -> {
                        Utilisateur u = i.getArgument(0);
                        u.setIdUser(2);
                        return u;
                    });
            when(tokenGenerator.generateToken()).thenReturn("new-user-token");
            when(sessionTokenRepository.save(any(SessionToken.class)))
                    .thenAnswer(i -> i.getArgument(0));

            LoginResponse response = authService.register(registerRequest);

            assertNotNull(response);
            assertEquals("new-user-token", response.getToken());
            assertEquals("Martin", response.getNom());
            assertEquals("Sophie", response.getPrenom());
            assertEquals("sophie.martin@email.com", response.getEmail());
            assertEquals("ETUDIANT", response.getRole()); // Rôle par défaut
            verify(utilisateurRepository, times(1)).save(any(Utilisateur.class));
        }

        @Test
        @DisplayName("Doit échouer si email existe déjà")
        void register_EmailAlreadyExists_ThrowsException() {
            when(utilisateurRepository.findByEmail("sophie.martin@email.com"))
                    .thenReturn(Optional.of(utilisateur));

            UtilisateurAlreadyExistsException exception = assertThrows(
                    UtilisateurAlreadyExistsException.class,
                    () -> authService.register(registerRequest));

            assertTrue(exception.getMessage().contains("existe déjà"));
            verify(utilisateurRepository, never()).save(any());
        }

        @Test
        @DisplayName("Doit enregistrer avec rôle ENSEIGNANT si spécifié")
        void register_WithEnseignantRole_Success() {
            registerRequest.setRole("ENSEIGNANT");
            when(utilisateurRepository.findByEmail("sophie.martin@email.com"))
                    .thenReturn(Optional.empty());
            when(utilisateurRepository.save(any(Utilisateur.class)))
                    .thenAnswer(i -> {
                        Utilisateur u = i.getArgument(0);
                        u.setIdUser(2);
                        return u;
                    });
            when(tokenGenerator.generateToken()).thenReturn("enseignant-token");
            when(sessionTokenRepository.save(any(SessionToken.class)))
                    .thenAnswer(i -> i.getArgument(0));

            LoginResponse response = authService.register(registerRequest);

            assertEquals("ENSEIGNANT", response.getRole());
        }

        @Test
        @DisplayName("Doit utiliser rôle ETUDIANT si rôle invalide")
        void register_InvalidRole_DefaultsToEtudiant() {
            registerRequest.setRole("INVALID_ROLE");
            when(utilisateurRepository.findByEmail("sophie.martin@email.com"))
                    .thenReturn(Optional.empty());
            when(utilisateurRepository.save(any(Utilisateur.class)))
                    .thenAnswer(i -> {
                        Utilisateur u = i.getArgument(0);
                        u.setIdUser(2);
                        return u;
                    });
            when(tokenGenerator.generateToken()).thenReturn("default-role-token");
            when(sessionTokenRepository.save(any(SessionToken.class)))
                    .thenAnswer(i -> i.getArgument(0));

            LoginResponse response = authService.register(registerRequest);

            assertEquals("ETUDIANT", response.getRole());
        }
    }

    @Nested
    @DisplayName("Tests logout")
    class LogoutTests {

        @Test
        @DisplayName("Doit supprimer le token de session")
        void logout_ValidToken_DeletesSession() {
            doNothing().when(sessionTokenRepository).deleteById("token-to-delete");

            assertDoesNotThrow(() -> authService.logout("token-to-delete"));

            verify(sessionTokenRepository, times(1)).deleteById("token-to-delete");
        }
    }
}
