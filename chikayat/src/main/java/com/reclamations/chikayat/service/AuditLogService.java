package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.AuditLog;
import com.reclamations.chikayat.entity.Utilisateur;
import com.reclamations.chikayat.repository.AuditLogRepository;
import com.reclamations.chikayat.repository.UtilisateurRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuditLogService {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    UtilisateurRepository utilisateurRepository;

    public void logUpdate(String tableName, Integer recordId, String oldData, String newData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof Utilisateur) {
            userId = ((Utilisateur) authentication.getPrincipal()).getId();
        } else if (authentication != null) {

            userId = utilisateurRepository.findByLogin(authentication.getName())
                    .map(Utilisateur::getId)
                    .orElse(null);
        }

        AuditLog log = new AuditLog();

        log.setTableName(tableName);
        log.setOperation("Modification");
        log.setRecordId(recordId);

        log.setUserId(userId);
        log.setTimestamp(LocalDateTime.now());
        log.setOldData(oldData);
        log.setNewData(newData);
        auditLogRepository.save(log);
    }

    public void logDeletion(String tableName, Integer recordId, String oldData) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Integer userId = null;
        if (authentication != null && authentication.getPrincipal() instanceof Utilisateur) {
            userId = ((Utilisateur) authentication.getPrincipal()).getId();
        } else if (authentication != null) {

            userId = utilisateurRepository.findByLogin(authentication.getName())
                    .map(Utilisateur::getId)
                    .orElse(null);
        }

        AuditLog log = new AuditLog();
        log.setTableName(tableName);
        log.setOperation("Suppression");
        log.setRecordId(recordId);
        log.setUserId(userId);
        log.setTimestamp(LocalDateTime.now());
        log.setOldData(oldData);
        log.setNewData("");
        auditLogRepository.save(log);
    }
}
