package com.reclamations.chikayat.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.url.frontend.reset-password}")
    private String resetPasswordUrl;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendPasswordResetEmail(String email, String token) {
        String resetLink = resetPasswordUrl + "?token=" + token;
        String subject = "Réinitialisation de votre mot de passe";
        String content = "<p>Bonjour,</p>"
                + "<p>Vous avez demandé la réinitialisation de votre mot de passe.</p>"
                + "<p>Cliquez sur le lien ci-dessous pour définir un nouveau mot de passe :</p>"
                + "<p><a href=\"" + resetLink + "\">Réinitialiser mon mot de passe</a></p>"
                + "<p>Ce lien expirera dans un délai défini.</p>";

        sendEmail(email, subject, content);
    }

    public void sendEmail(String to, String subject, String content) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
            System.out.println("✅ Email envoyé avec succès à " + to);

        } catch (MessagingException e) {
            System.err.println("❌ Erreur lors de l'envoi de l'email : " + e.getMessage());
            throw new RuntimeException("Erreur lors de l'envoi de l'e-mail", e);
        }
    }
}
