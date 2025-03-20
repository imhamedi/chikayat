package com.reclamations.chikayat.controller;

import com.reclamations.chikayat.service.OcrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/api/ocr")
public class OcrController {

    @Autowired
    private OcrService ocrService;

    @PostMapping(value = "/extract-text", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> extractText(@RequestParam("file") MultipartFile file,
            @RequestParam("language") String language) {
        try {
            String extractedText = ocrService.extractTextFromImage(file, language);
            return ResponseEntity.ok(Map.of("extractedText", extractedText));
        } catch (IOException | RuntimeException | net.sourceforge.tess4j.TesseractException e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Erreur lors de l'extraction OCR: " + e.getMessage()));
        }
    }
}
