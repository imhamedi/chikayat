package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.Niveau;
import com.reclamations.chikayat.repository.NiveauRepository;
import com.reclamations.chikayat.service.NiveauService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/niveaux")
public class NiveauController {

    @Autowired
    private NiveauRepository niveauRepository;
    @Autowired
    private NiveauService niveauService;

    @GetMapping
    public ResponseEntity<List<Niveau>> getAllNiveaux() {
        return ResponseEntity.ok(niveauService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Niveau>> searchNiveaux(@RequestParam String keyword) {
        return ResponseEntity.ok(niveauService.search(keyword));
    }

    @PostMapping
    public ResponseEntity<Niveau> createNiveau(@RequestBody Niveau niveau) {
        Niveau created = niveauService.save(niveau);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Niveau> updateNiveau(@PathVariable Integer id, @RequestBody Niveau niveau) {
        niveau.setId(id);
        Niveau updated = niveauService.update(niveau);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNiveau(@PathVariable Integer id) {
        niveauService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Niveau> getNiveauById(@PathVariable Integer id) {
        Optional<Niveau> niveau = niveauService.findById(id);
        return niveau.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
