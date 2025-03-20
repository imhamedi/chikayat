package com.reclamations.chikayat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@Service
public class FileUploadService {

    @Value("${path_scan}") // 🔹 Récupération du chemin depuis application.properties
    private String uploadDir;

    public String uploadFile(MultipartFile file, String reference) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Le fichier est vide.");
        }

        // 🔹 Déterminer le nom du fichier
        String fileName = "reclamation_" + reference + "_" + file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir, fileName);

        // 🔹 Créer le dossier si inexistant
        Files.createDirectories(filePath.getParent());
        Files.write(filePath, file.getBytes());

        return fileName; // 🔹 Retourne le nom du fichier stocké
    }
}
