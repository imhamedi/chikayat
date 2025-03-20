package com.reclamations.chikayat.service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Service
public class OcrService {

    @Value("${tesseract_path}")
    private String tesseractPath; // Chemin vers les données de Tesseract (datapath)

    public String extractTextFromImage(MultipartFile file, String language) throws IOException, TesseractException {
        Tesseract tesseract = new Tesseract();
        tesseract.setDatapath(tesseractPath);
        tesseract.setLanguage(language);

        // Lire l'image depuis le flux du fichier
        BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
        if (bufferedImage == null) {
            throw new IOException("Impossible de lire l'image. Vérifiez le format du fichier.");
        }
        return tesseract.doOCR(bufferedImage);
    }
}
