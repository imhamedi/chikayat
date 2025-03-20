package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.TypeRequete;
import com.reclamations.chikayat.service.TypeRequeteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/type-requete")
public class TypeRequeteController {

    @Autowired
    private TypeRequeteService TypeRequeteService;

    @GetMapping
    public ResponseEntity<List<TypeRequete>> getAll() {
        return ResponseEntity.ok(TypeRequeteService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TypeRequete>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(TypeRequeteService.search(keyword));
    }

    @PostMapping
    public ResponseEntity<TypeRequete> create(@RequestBody TypeRequete sr) {
        TypeRequete created = TypeRequeteService.save(sr);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeRequete> update(@PathVariable Integer id, @RequestBody TypeRequete sr) {
        sr.setId(id);
        TypeRequete updated = TypeRequeteService.update(sr);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        TypeRequeteService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeRequete> getById(@PathVariable Integer id) {
        Optional<TypeRequete> sr = TypeRequeteService.findById(id);
        return sr.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
