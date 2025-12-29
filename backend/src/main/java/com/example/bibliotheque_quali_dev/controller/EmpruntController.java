package com.example.bibliotheque_quali_dev.controller;
import com.example.bibliotheque_quali_dev.entity.Emprunt;
import com.example.bibliotheque_quali_dev.repository.EmpruntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/emprunts")
public class EmpruntController {
    @Autowired
    private EmpruntRepository empruntRepository;
    @GetMapping
    public List<Emprunt> getAllEmprunts() {
        return empruntRepository.findAll();
    }

    @GetMapping("/{id}")
    public Emprunt getEmpruntById(@PathVariable Integer id) {
        return empruntRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Emprunt non trouvé."));
    }
}
