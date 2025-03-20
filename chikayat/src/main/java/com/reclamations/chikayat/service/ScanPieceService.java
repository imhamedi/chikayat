package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.ScanPiece;
import com.reclamations.chikayat.repository.ScanPieceRepository;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ScanPieceService {

    private static final Logger logger = LoggerFactory.getLogger(ScanPieceService.class);

    @Autowired
    private ScanPieceRepository scanPieceRepository;

    private final Tika tika = new Tika();

    @Value("${path_scan}")
    private String pathScan; // Dossier de stockage des scans

    @Value("${tesseract_path}")
    private String tesseractPath; // Chemin vers Tesseract-OCR

    private boolean isSupportedImageFormat(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (contentType.equals("image/png") || contentType.equals("image/jpeg")
                || contentType.equals("image/tiff"));
    }

    public ScanPiece extractAndSaveText(MultipartFile file, String language, String numInscription) throws IOException {
        logger.info("📂 Début du traitement du fichier : {}", file.getOriginalFilename());

        if (!isSupportedImageFormat(file)) {
            throw new RuntimeException("❌ Format d'image non supporté. Utilisez PNG, JPG ou TIFF.");
        }

        String extractedText = extractTextFromImage(file, language);
        logger.info("📝 Texte extrait : {}", extractedText.substring(0, Math.min(extractedText.length(), 100)));

        // Définition du nom de fichier avec `XXXX-MM-YYYY.extension`
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String formattedNumInscription = formatNumInscription(numInscription);
        String storedFileName = formattedNumInscription + "." + fileExtension;

        // Vérifier et créer le répertoire si nécessaire
        File directory = new File(pathScan);
        if (!directory.exists()) {
            directory.mkdirs();
            logger.info("📁 Création du répertoire : {}", pathScan);
        }

        // Copier le fichier dans le dossier configuré
        File storedFile = new File(pathScan + "/" + storedFileName);
        Files.copy(file.getInputStream(), Paths.get(storedFile.getAbsolutePath()));
        logger.info("✅ Fichier enregistré sous : {}", storedFile.getAbsolutePath());

        // Création et sauvegarde de l'entité
        ScanPiece scanPiece = new ScanPiece();
        scanPiece.setFileName(storedFileName);
        scanPiece.setLanguage(language);
        scanPiece.setExtractedText(extractedText);
        scanPiece.setDateUploaded(LocalDateTime.now());
        scanPiece.setNumInscription(numInscription);

        return scanPieceRepository.save(scanPiece);
    }

    private String extractTextFromImage(MultipartFile file, String language) throws IOException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tesseractPath);
        tesseract.setLanguage(language);

        // Convertir l'image en BufferedImage pour un meilleur support
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            throw new RuntimeException("❌ Impossible de lire l'image. Format non supporté.");
        }

        try {
            return tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            throw new RuntimeException("❌ Erreur lors de l'extraction du texte avec Tesseract", e);
        }
    }

    private String getFileExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot == -1) ? "png" : fileName.substring(lastDot + 1);
    }

    /**
     * Formater le `numInscription` pour générer un nom de fichier valide.
     * Remplace `/` et `(` `)` par `-` pour éviter les erreurs de fichiers.
     */
    private String formatNumInscription(String numInscription) {
        return numInscription.replace("/", "-").replace("(", "").replace(")", "");
    }

    public Optional<ScanPiece> getById(Integer id) {
        return scanPieceRepository.findById(id);
    }

    public List<ScanPiece> getByNumInscription(String numInscription) {
        return scanPieceRepository.findByNumInscription(numInscription);
    }

    public List<ScanPiece> getAll() {
        return scanPieceRepository.findAll();
    }
}
