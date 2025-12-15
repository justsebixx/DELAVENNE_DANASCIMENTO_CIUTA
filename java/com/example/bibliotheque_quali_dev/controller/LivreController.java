package com.example.bibliotheque_quali_dev.controller;
import com.example.bibliotheque_quali_dev.entity.Livre;
import com.example.bibliotheque_quali_dev.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/livres")
public class LivreController {

    @Autowired
    private LivreRepository livreRepository;
    @GetMapping
    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    @GetMapping("/{id}")
    public Livre getLivreById(@PathVariable Integer id) {
        return livreRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livre pas trouvé."));
    }
}
