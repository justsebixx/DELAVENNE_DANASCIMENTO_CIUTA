package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.config.RequireRoles;
import com.example.bibliotheque_quali_dev.dto.DashboardResponse;
import com.example.bibliotheque_quali_dev.dto.TopLivreStat;
import com.example.bibliotheque_quali_dev.repository.EmpruntRepository;
import com.example.bibliotheque_quali_dev.repository.LivreRepository;
import com.example.bibliotheque_quali_dev.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequireRoles({"ADMIN", "BIBLIOTHECAIRE"})
public class DashboardController {

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/stats")
    public ResponseEntity<DashboardResponse> getDashboardStats() {
        DashboardResponse response = new DashboardResponse();

        response.setTotalLivres((int) livreRepository.count());
        response.setLivresDisponibles(livreRepository.sumNbDisponibles());
        int totalExemplaires = livreRepository.sumNbExemplaires();
        response.setLivresEmpruntes(totalExemplaires - response.getLivresDisponibles());
        response.setTotalUtilisateurs((int) utilisateurRepository.count());
        response.setEmpruntsEnCours(empruntRepository.countByDateRetourEffectiveIsNull());
        response.setEmpruntsEnRetard(empruntRepository.countByDateRetourPrevueBeforeAndDateRetourEffectiveIsNull(LocalDate.now()));
        response.setTopLivres(empruntRepository.findTop5MostBorrowedBooks());
        return ResponseEntity.ok(response);
    }
}
