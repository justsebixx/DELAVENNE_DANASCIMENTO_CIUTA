package com.example.bibliotheque_quali_dev.controller;

import com.example.bibliotheque_quali_dev.entity.Emprunt;
import com.example.bibliotheque_quali_dev.repository.EmpruntRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/emprunts")
@CrossOrigin(origins = "*")
public class EmpruntController {

    @Autowired
    private EmpruntRepository empruntRepository;

    @GetMapping
    public List<Emprunt> getAllEmprunts() {
        return empruntRepository.findAll();
    }

    @GetMapping("/{id}")
    public Emprunt getEmpruntById(@PathVariable Integer id) {
        return empruntRepository.findById(id).orElse(null);
    }
}
