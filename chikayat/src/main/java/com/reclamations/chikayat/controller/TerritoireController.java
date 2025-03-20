package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.Territoire;
import com.reclamations.chikayat.service.TerritoireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/territoires")
public class TerritoireController {

    @Autowired
    private TerritoireService territoireService;

    @GetMapping
    public ResponseEntity<List<Territoire>> getAll() {
        return ResponseEntity.ok(territoireService.findAll());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Territoire>> search(@RequestParam String keyword) {
        return ResponseEntity.ok(territoireService.search(keyword));
    }

    @PostMapping
    public ResponseEntity<Territoire> create(@RequestBody Territoire territoire) {
        Territoire created = territoireService.save(territoire);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Territoire> update(@PathVariable Integer id, @RequestBody Territoire territoire) {
        territoire.setId(id);
        Territoire updated = territoireService.update(territoire);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        territoireService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Territoire> getById(@PathVariable Integer id) {
        Optional<Territoire> territoire = territoireService.findById(id);
        return territoire.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
