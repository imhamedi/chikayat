package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.SourceReclamation;
import com.reclamations.chikayat.service.SourceReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/source-reclamation")
public class SourceReclamationController {

    @Autowired
    private SourceReclamationService sourceReclamationService;

    @GetMapping
    public ResponseEntity<List<SourceReclamation>> getAll() {
        return ResponseEntity.ok(sourceReclamationService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SourceReclamation>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(sourceReclamationService.search(keyword));
    }

    @PostMapping
    public ResponseEntity<SourceReclamation> create(@RequestBody SourceReclamation sr) {
        SourceReclamation created = sourceReclamationService.save(sr);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SourceReclamation> update(@PathVariable Integer id, @RequestBody SourceReclamation sr) {
        sr.setId(id);
        SourceReclamation updated = sourceReclamationService.update(sr);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        sourceReclamationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SourceReclamation> getById(@PathVariable Integer id) {
        Optional<SourceReclamation> sr = sourceReclamationService.findById(id);
        return sr.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
