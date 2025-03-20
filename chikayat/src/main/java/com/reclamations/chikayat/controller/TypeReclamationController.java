package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.TypeReclamation;
import com.reclamations.chikayat.service.TypeReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/type-reclamation")
public class TypeReclamationController {

    @Autowired
    private TypeReclamationService TypeReclamationService;

    @GetMapping
    public ResponseEntity<List<TypeReclamation>> getAll() {
        return ResponseEntity.ok(TypeReclamationService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TypeReclamation>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(TypeReclamationService.search(keyword));
    }

    @PostMapping
    public ResponseEntity<TypeReclamation> create(@RequestBody TypeReclamation sr) {
        TypeReclamation created = TypeReclamationService.save(sr);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeReclamation> update(@PathVariable Integer id, @RequestBody TypeReclamation sr) {
        sr.setId(id);
        TypeReclamation updated = TypeReclamationService.update(sr);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        TypeReclamationService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeReclamation> getById(@PathVariable Integer id) {
        Optional<TypeReclamation> sr = TypeReclamationService.findById(id);
        return sr.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
