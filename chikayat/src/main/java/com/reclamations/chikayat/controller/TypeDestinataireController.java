package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.TypeDestinataire;
import com.reclamations.chikayat.service.TypeDestinataireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/type-destinataire")
public class TypeDestinataireController {

    @Autowired
    private TypeDestinataireService TypeDestinataireService;

    @GetMapping
    public ResponseEntity<List<TypeDestinataire>> getAll() {
        return ResponseEntity.ok(TypeDestinataireService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<TypeDestinataire>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(TypeDestinataireService.search(keyword));
    }

    @PostMapping
    public ResponseEntity<TypeDestinataire> create(@RequestBody TypeDestinataire sr) {
        TypeDestinataire created = TypeDestinataireService.save(sr);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeDestinataire> update(@PathVariable Integer id, @RequestBody TypeDestinataire sr) {
        sr.setId(id);
        TypeDestinataire updated = TypeDestinataireService.update(sr);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        TypeDestinataireService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeDestinataire> getById(@PathVariable Integer id) {
        Optional<TypeDestinataire> sr = TypeDestinataireService.findById(id);
        return sr.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
