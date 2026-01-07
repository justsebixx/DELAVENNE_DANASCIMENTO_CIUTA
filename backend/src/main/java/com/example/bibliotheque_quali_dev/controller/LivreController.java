package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.entity.Livre;
import com.example.bibliotheque_quali_dev.repository.LivreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livres")
@CrossOrigin(origins = "*")
public class LivreController {

    @Autowired
    private LivreRepository livreRepository;

    @GetMapping
    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    @GetMapping("/{id}")
    public Livre getLivreById(@PathVariable Integer id) {
        return livreRepository.findById(id).orElse(null);
    }
}
