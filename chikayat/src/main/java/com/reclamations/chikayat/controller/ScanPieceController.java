package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.entity.ScanPiece;
import com.reclamations.chikayat.service.ScanPieceService;

import org.apache.tika.metadata.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/documents")
public class ScanPieceController {

    private static final Logger logger = LoggerFactory.getLogger(ScanPieceController.class);

    @Value("${path_scan}")
    private String pathScan;

    @Autowired
    private ScanPieceService scanPieceService;

    @PostMapping(value = "/extract-text", consumes = "multipart/form-data")
    public ResponseEntity<?> extractText(
            @RequestParam("file") MultipartFile file,
            @RequestParam("language") String language,
            @RequestParam("numInscription") String numInscription) {

        logger.info("📩 Requête reçue - Fichier : {}, Langue : {}, Numéro d'inscription : {}",
                file.getOriginalFilename(), language, numInscription);

        try {
            ScanPiece scanPiece = scanPieceService.extractAndSaveText(file, language, numInscription);
            logger.info("✅ Extraction réussie pour le fichier : {}", file.getOriginalFilename());
            return ResponseEntity.ok(scanPiece);
        } catch (IOException e) {
            logger.error("❌ Erreur lors de l'extraction du texte", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du traitement du fichier.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        Optional<ScanPiece> scanPiece = scanPieceService.getById(id);
        if (scanPiece.isPresent()) {
            return ResponseEntity.ok(scanPiece.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @GetMapping("/by-num-inscription/{numInscription}")
    public ResponseEntity<List<ScanPiece>> getByNumInscription(@PathVariable String numInscription) {
        List<ScanPiece> scanPieces = scanPieceService.getByNumInscription(numInscription);
        return ResponseEntity.ok(scanPieces);
    }

    @GetMapping
    public ResponseEntity<List<ScanPiece>> getAll() {
        List<ScanPiece> scanPieces = scanPieceService.getAll();
        return ResponseEntity.ok(scanPieces);
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        try {
            File file = new File(pathScan + "/" + filename);
            if (!file.exists()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }

            Resource resource = new UrlResource(file.toURI());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
