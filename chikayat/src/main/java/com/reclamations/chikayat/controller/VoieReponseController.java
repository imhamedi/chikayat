package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.VoieReponse;
import com.reclamations.chikayat.service.VoieReponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/voie-reponse")
public class VoieReponseController {

    @Autowired
    private VoieReponseService VoieReponseService;

    @GetMapping
    public ResponseEntity<List<VoieReponse>> getAll() {
        return ResponseEntity.ok(VoieReponseService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<VoieReponse>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(VoieReponseService.search(keyword));
    }

    @PostMapping
    public ResponseEntity<VoieReponse> create(@RequestBody VoieReponse sr) {
        VoieReponse created = VoieReponseService.save(sr);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VoieReponse> update(@PathVariable Integer id, @RequestBody VoieReponse sr) {
        sr.setId(id);
        VoieReponse updated = VoieReponseService.update(sr);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        VoieReponseService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<VoieReponse> getById(@PathVariable Integer id) {
        Optional<VoieReponse> sr = VoieReponseService.findById(id);
        return sr.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
