package com.example.bibliotheque_quali_dev.service;

import com.example.bibliotheque_quali_dev.dto.EmpruntCreateRequest;
import com.example.bibliotheque_quali_dev.entity.Emprunt;
import com.example.bibliotheque_quali_dev.entity.Livre;
import com.example.bibliotheque_quali_dev.repository.EmpruntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 * Service de gestion des emprunts avec logique métier.
 */
@Service
@Transactional
public class EmpruntService {

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private LivreService livreService;

    @Autowired
    private NotificationService notificationService;

    private static final int MAX_EMPRUNTS_PAR_UTILISATEUR = 5;
    private static final int DUREE_EMPRUNT_JOURS = 30;
    private static final int MAX_PROLONGATIONS = 1;

    /**
     * Récupère tous les emprunts.
     */
    public List<Emprunt> findAll() {
        return empruntRepository.findAll();
    }

    /**
     * Récupère un emprunt par son ID.
     */
    public Emprunt findById(Integer id) {
        return empruntRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Emprunt non trouvé avec l'ID: " + id));
    }

    /**
     * Récupère les emprunts actifs d'un utilisateur.
     */
    public List<Emprunt> findActiveByUserId(Integer userId) {
        return empruntRepository.findByIdUserAndDateRetourEffectiveIsNull(userId);
    }

    /**
     * Crée un nouvel emprunt.
     */
    public Emprunt create(EmpruntCreateRequest request) {
        // Validation : vérifier que l'utilisateur n'a pas dépassé la limite d'emprunts
        List<Emprunt> empruntsActifs = findActiveByUserId(request.getIdUser());
        if (empruntsActifs.size() >= MAX_EMPRUNTS_PAR_UTILISATEUR) {
            throw new RuntimeException("Limite d'emprunts atteinte (" + MAX_EMPRUNTS_PAR_UTILISATEUR + " maximum)");
        }

        // Vérifier la disponibilité du livre
        if (!livreService.isAvailable(request.getIdLivre())) {
            throw new RuntimeException("Ce livre n'est pas disponible actuellement");
        }

        // Créer l'emprunt
        Emprunt emprunt = new Emprunt();
        emprunt.setIdUser(request.getIdUser());
        emprunt.setIdLivre(request.getIdLivre());
        emprunt.setDateEmprunt(Date.valueOf(LocalDate.now()));
        emprunt.setDateRetourPrevue(Date.valueOf(LocalDate.now().plusDays(DUREE_EMPRUNT_JOURS)));
        emprunt.setDateRetourEffective(null);

        // Décrémenter le nombre d'exemplaires disponibles
        livreService.decrementDisponibles(request.getIdLivre());

        return empruntRepository.save(emprunt);
    }

    /**
     * Retourne un livre (marque l'emprunt comme terminé).
     */
    public Emprunt returnBook(Integer empruntId) {
        Emprunt emprunt = findById(empruntId);

        if (emprunt.getDateRetourEffective() != null) {
            throw new RuntimeException("Ce livre a déjà été retourné");
        }

        emprunt.setDateRetourEffective(Date.valueOf(LocalDate.now()));

        // Incrémenter le nombre d'exemplaires disponibles
        livreService.incrementDisponibles(emprunt.getIdLivre());

        // Créer une notification de retour
        notificationService.createReturnNotification(emprunt);

        return empruntRepository.save(emprunt);
    }

    /**
     * Prolonge la durée d'un emprunt.
     */
    public Emprunt extend(Integer empruntId) {
        Emprunt emprunt = findById(empruntId);

        if (emprunt.getDateRetourEffective() != null) {
            throw new RuntimeException("Impossible de prolonger un emprunt déjà retourné");
        }

        // Vérifier si l'emprunt est en retard
        if (emprunt.getDateRetourPrevue().toLocalDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Impossible de prolonger un emprunt en retard");
        }

        // Pour simplifier, on prolonge de 15 jours supplémentaires
        LocalDate nouvelleDateRetour = emprunt.getDateRetourPrevue().toLocalDate().plusDays(15);
        emprunt.setDateRetourPrevue(Date.valueOf(nouvelleDateRetour));

        return empruntRepository.save(emprunt);
    }

    /**
     * Récupère tous les emprunts en retard.
     */
    public List<Emprunt> findOverdueEmprunts() {
        return empruntRepository.findOverdueEmprunts(Date.valueOf(LocalDate.now()));
    }

    /**
     * Récupère l'historique complet des emprunts d'un utilisateur.
     */
    public List<Emprunt> findHistoryByUserId(Integer userId) {
        return empruntRepository.findByIdUser(userId);
    }
}
