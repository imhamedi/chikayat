package com.reclamations.chikayat.controller;

import org.springframework.data.domain.PageRequest;

import com.reclamations.chikayat.entity.Reclamation;
import com.reclamations.chikayat.entity.ReclamationDetail;
import com.reclamations.chikayat.entity.ReclamationSummary;
import com.reclamations.chikayat.entity.TypeRequete;
import com.reclamations.chikayat.entity.Utilisateur;
import com.reclamations.chikayat.entity.VoieReponse;
import com.reclamations.chikayat.entity.TypeReclamation;
import com.reclamations.chikayat.entity.TypeDestinataire;
import com.reclamations.chikayat.repository.TypeRequeteRepository;
import com.reclamations.chikayat.repository.UtilisateurRepository;
import com.reclamations.chikayat.repository.VoieReponseRepository;
import com.reclamations.chikayat.repository.TypeReclamationRepository;
import com.reclamations.chikayat.repository.TypeDestinataireRepository;
import com.reclamations.chikayat.service.AuditLogService;
import com.reclamations.chikayat.service.FileUploadService;
import com.reclamations.chikayat.service.ReclamationService;
import com.reclamations.chikayat.service.SequenceService;

import jakarta.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;

    @Autowired
    private FileUploadService fileUploadService;

    @Autowired
    private SequenceService sequenceService;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private TypeRequeteRepository typeRequeteRepository;

    @Autowired
    private TypeReclamationRepository typeReclamationRepository;

    @Autowired
    private TypeDestinataireRepository typeDestinataireRepository;
    @Autowired
    private AuditLogService auditLogService;

    @Autowired
    private VoieReponseRepository voieReponseRepository;
    @Value("${path_scan}")
    private String pathScan;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN') or hasRole('UTILISATEUR')")
    public ResponseEntity<Reclamation> createReclamation(@RequestBody Reclamation reclamation, Principal principal) {
        Optional<Utilisateur> optUser = utilisateurRepository.findByLogin(principal.getName());
        if (!optUser.isPresent()) {
            throw new RuntimeException("Utilisateur non trouvé.");
        }
        reclamation.setUserSaisie(optUser.get());

        if (reclamation.getTypeRequete() != null && reclamation.getTypeRequete().getId() != null) {
            TypeRequete typeRequete = typeRequeteRepository.findById(reclamation.getTypeRequete().getId())
                    .orElseThrow(() -> new RuntimeException("TypeRequete invalide"));
            reclamation.setTypeRequete(typeRequete);
        } else {
            throw new RuntimeException("Le type de requête est obligatoire.");
        }

        if (reclamation.getTypeReclamation() != null && reclamation.getTypeReclamation().getId() != null) {
            TypeReclamation typeReclamation = typeReclamationRepository
                    .findById(reclamation.getTypeReclamation().getId())
                    .orElseThrow(() -> new RuntimeException("TypeReclamation invalide"));
            reclamation.setTypeReclamation(typeReclamation);
        } else {
            throw new RuntimeException("Le type de réclamation est obligatoire.");
        }

        Reclamation savedReclamation = reclamationService.save(reclamation);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReclamation);
    }

    @PostMapping(value = "/{id}/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@PathVariable int id, @RequestParam("file") MultipartFile file) {
        try {
            String fileName = fileUploadService.uploadFile(file, String.valueOf(id));

            Optional<Reclamation> optReclamation = reclamationService.findById(id);
            if (optReclamation.isPresent()) {
                Reclamation reclamation = optReclamation.get();
                String fullPath = pathScan + "/" + fileName;
                reclamation.setPieceJointeReclamation(fullPath);
                reclamationService.save(reclamation);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("{\"error\": \"Réclamation non trouvée pour mise à jour du fichier.\"}");
            }

            String jsonResponse = "{\"message\": \"Fichier téléversé avec succès\", \"fileName\": \"" + fileName
                    + "\"}";
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body("{\"error\": \"Erreur lors du téléversement du fichier : " + e.getMessage() + "\"}");
        }
    }

    @GetMapping(value = "/generate-num-inscription", produces = "text/plain")
    public ResponseEntity<String> generateNumInscription() {
        try {
            String numInscription = sequenceService.generateNumInscription();
            return ResponseEntity.ok().body(numInscription);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Erreur lors de la génération du numéro.");
        }
    }

    @PostMapping("/release-num-inscription")
    public ResponseEntity<Void> releaseNumInscription(@RequestBody Map<String, String> payload) {
        String numInscription = payload.get("numInscription");
        reclamationService.releaseNumInscription(numInscription);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/territoires/nom_commune_langue_2")
    public ResponseEntity<List<String>> getCommunes() {
        List<String> communes = reclamationService.getAllCommunes();
        return ResponseEntity.ok(communes);
    }

    @GetMapping("/type-destinataire/nom_destinataire_langue_2")
    public ResponseEntity<List<Map<String, Object>>> getDestinataires() {
        List<Map<String, Object>> destinataires = reclamationService.getAllTypesDestinataires();
        return ResponseEntity.ok(destinataires);
    }

    @GetMapping("/type-reclamation/nom_reclamation_langue_2")
    public ResponseEntity<List<Map<String, Object>>> getReclamations() {
        List<Map<String, Object>> reclamations = reclamationService.getAllTypesReclamations();
        return ResponseEntity.ok(reclamations);
    }

    @GetMapping("/type-requete/nom_requete_langue_2")
    public ResponseEntity<List<Map<String, Object>>> getRequetes() {
        List<Map<String, Object>> requetes = reclamationService.getAllTypesRequetes();
        return ResponseEntity.ok(requetes);
    }

    @GetMapping("/source-reclamation/nom_source_langue_2")
    public ResponseEntity<List<Map<String, Object>>> getSources() {
        List<Map<String, Object>> requetes = reclamationService.getAllSourcesReclamations();
        return ResponseEntity.ok(requetes);
    }

    @GetMapping("/voie-reponse/nom_voie_langue_2")
    public ResponseEntity<List<Map<String, Object>>> getVoies() {
        List<Map<String, Object>> requetes = reclamationService.getAllVoiesRetours();
        return ResponseEntity.ok(requetes);
    }

    @GetMapping("/utilisateurs/login")
    public ResponseEntity<List<Map<String, Object>>> getLogins() {
        List<Map<String, Object>> requetes = reclamationService.getAllLoginss();
        return ResponseEntity.ok(requetes);
    }

    @GetMapping("/by-identifiant/{identifiant}")
    public ResponseEntity<?> getReclamationsByIdentifiant(@PathVariable String identifiant) {
        List<ReclamationSummary> summaries = reclamationService.getReclamationSummaryByIdentifiant(identifiant);
        int count = summaries.size();
        Map<String, Object> response = new HashMap<>();
        response.put("count", count);
        response.put("reclamations", summaries);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> searchReclamations(
            // Critères de la table principale
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDepotStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDepotEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateInscriptionStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateInscriptionEnd,
            @RequestParam(required = false) String numInscription,
            @RequestParam(required = false) String referenceBo,
            @RequestParam(required = false) String identifiant,
            @RequestParam(required = false) String nomComplet,
            @RequestParam(required = false) String commune,
            @RequestParam(required = false) Integer typeReclamation,
            @RequestParam(required = false) String objetReclamation,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) String telephone,
            @RequestParam(required = false) String instructionsGouverneur,
            @RequestParam(required = false) String observation1,
            @RequestParam(required = false) String observation2,
            @RequestParam(required = false) Integer sourceReclamation,
            @RequestParam(required = false) Integer typeRequete,
            @RequestParam(required = false) Integer flagCloture,
            // Critères de la table détail
            @RequestParam(required = false) Integer flagEnvoiAutorite,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEnvoiAutoriteStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEnvoiAutoriteEnd,
            @RequestParam(required = false) String referenceEnvoiAutorite,
            @RequestParam(required = false) Integer flagRetourAutorite,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateRetourAutoriteStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateRetourAutoriteEnd,
            @RequestParam(required = false) String referenceRetourAutorite,
            @RequestParam(required = false) Integer flagEnvoiReclamant,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEnvoiReclamantStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateEnvoiReclamantEnd,
            @RequestParam(required = false) Integer voieEnvoiReclamant,
            @RequestParam(required = false) String referenceEnvoiReclamant,
            @RequestParam(required = false) Integer userEnvoiAutorite,
            @RequestParam(required = false) Integer userRetourAutorite,
            @RequestParam(required = false) Integer userEnvoiReclamant,
            // Autres critères
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Reclamation> results = reclamationService.searchReclamations(
                dateDepotStart, dateDepotEnd,
                dateInscriptionStart, dateInscriptionEnd,
                numInscription, referenceBo, identifiant, nomComplet, commune,
                typeReclamation, objetReclamation, annee, telephone, instructionsGouverneur, observation1, observation2,
                sourceReclamation, typeRequete, flagCloture,
                flagEnvoiAutorite, dateEnvoiAutoriteStart, dateEnvoiAutoriteEnd, referenceEnvoiAutorite,
                flagRetourAutorite, dateRetourAutoriteStart, dateRetourAutoriteEnd, referenceRetourAutorite,
                flagEnvoiReclamant, dateEnvoiReclamantStart, dateEnvoiReclamantEnd, voieEnvoiReclamant,
                referenceEnvoiReclamant, userEnvoiAutorite, userRetourAutorite, userEnvoiReclamant,
                pageable);

        Map<String, Object> response = new HashMap<>();
        response.put("content", results.getContent());
        response.put("totalPages", results.getTotalPages());
        response.put("totalElements", results.getTotalElements());
        response.put("page", results.getNumber());
        response.put("size", results.getSize());

        return ResponseEntity.ok(response);
    }

    // Récupération par ID
    @GetMapping("/{id}")
    public ResponseEntity<Reclamation> getReclamation(@PathVariable Integer id) {
        Optional<Reclamation> reclamation = reclamationService.findById(id);
        if (reclamation.isPresent()) {
            return ResponseEntity.ok(reclamation.get());
        }
        return ResponseEntity.notFound().build();
    }

    // Mise à jour d'une réclamation (en empêchant la modification de champs non
    // autorisés)
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReclamation(@PathVariable Integer id, @RequestBody Reclamation updatedReclamation) {
        Optional<Reclamation> optionalReclamation = reclamationService.findById(id);
        if (!optionalReclamation.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réclamation non trouvée");
        }
        Reclamation existing = optionalReclamation.get();

        // Ne pas modifier les champs non éditables
        updatedReclamation.setId(existing.getId());
        updatedReclamation.setNumInscription(existing.getNumInscription());
        updatedReclamation.setUserSaisie(existing.getUserSaisie());
        // Sauvegarde et audit
        String oldData = reclamationService.concatData(existing);
        Reclamation saved = reclamationService.updateReclamation(updatedReclamation);
        String newData = reclamationService.concatData(saved);

        auditLogService.logUpdate("reclamations", existing.getId(), oldData, newData);

        return ResponseEntity.ok(saved);
    }

    // Suppression d'une réclamation avec audit
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReclamation(@PathVariable Integer id) {
        Optional<Reclamation> optionalReclamation = reclamationService.findById(id);
        if (!optionalReclamation.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réclamation non trouvée");
        }
        Reclamation toDelete = optionalReclamation.get();
        String data = reclamationService.concatData(toDelete);
        reclamationService.deleteReclamation(id);
        auditLogService.logDeletion("reclamations", id, data);
        return ResponseEntity.ok("Réclamation supprimée");
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats(
            @RequestParam(value = "durationType", required = false) String durationType,
            @RequestParam(value = "durationValue", required = false) Integer durationValue) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", reclamationService.countTotal());
        stats.put("enCours", reclamationService.countEnCours());
        stats.put("envoyeesAutorite", reclamationService.countEnvoyeesAutorite());
        stats.put("reponduAutorite", reclamationService.countReponduAutorite());
        stats.put("cloturees", reclamationService.countCloturees());

        if (durationType != null && durationValue != null) {
            stats.put("enCoursDepuis", reclamationService.countEnCoursDepuis(durationType, durationValue));
        }
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Reclamation>> getReclamationsByFlag(@RequestParam("flag") String flag) {
        List<Reclamation> results = reclamationService.getReclamationsByFlag(flag);
        return ResponseEntity.ok(results);
    }

    /**
     * Endpoint pour l'envoi de la réclamation à l'autorité.
     * Conditions : flagEnvoiAutorite est nul ou 0.
     * Le front envoie dans le body une map avec :
     * - "voieEnvoiAutoriteId" : l'identifiant de la voie sélectionnée
     * - "referenceEnvoiAutorite" : la référence saisie par l'utilisateur
     */
    @PostMapping("/envoiAutorite/{reclamationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('UTILISATEUR')")
    public ResponseEntity<?> envoiAutorite(@PathVariable Integer reclamationId,
            @RequestBody Map<String, String> payload,
            Principal principal) {
        Optional<Reclamation> optionalRec = reclamationService.findById(reclamationId);
        if (!optionalRec.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réclamation non trouvée");
        }
        Reclamation rec = optionalRec.get();
        ReclamationDetail detail = new ReclamationDetail();
        detail.setReclamation(rec);
        detail.setNumInscription(rec.getNumInscription());
        detail.setFlagEnvoiAutorite(1);
        // Traitement de la date d'envoi
        String dateEnvoiStr = payload.get("dateEnvoiAutorite");
        try {
            if (dateEnvoiStr != null && !dateEnvoiStr.isEmpty()) {
                LocalDateTime dateEnvoi = LocalDateTime.parse(dateEnvoiStr);
                detail.setDateEnvoiAutorite(dateEnvoi);
            } else {
                detail.setDateEnvoiAutorite(LocalDateTime.now());
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Format de date invalide");
        }

        // Récupérer et affecter le TypeDestinataire via son id
        String typeDestIdStr = payload.get("idTypeDestinataire");
        if (typeDestIdStr == null) {
            return ResponseEntity.badRequest().body("Le champ idTypeDestinataire est requis");
        }
        Integer typeDestId = Integer.valueOf(typeDestIdStr);
        TypeDestinataire typeDest = typeDestinataireRepository.findById(typeDestId)
                .orElseThrow(() -> new RuntimeException("TypeDestinataire non trouvé"));
        detail.setTypeDestinataire(typeDest);

        detail.setReferenceEnvoiAutorite(payload.get("referenceEnvoiAutorite"));

        // Affecter l'utilisateur connecté pour cet envoi
        Utilisateur user = utilisateurRepository.findByLogin(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        detail.setUserEnvoiAutorite(user);

        reclamationService.saveDetail(detail);
        rec.setFlagEnvoiAutorite(1);
        reclamationService.updateReclamation(rec);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Envoi vers l'autorité enregistré");

        return ResponseEntity.ok(response);
    }

    @PostMapping("/retourAutorite/{reclamationId}/{detailId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('UTILISATEUR')")
    public ResponseEntity<?> retourAutorite(@PathVariable Integer reclamationId,
            @PathVariable Integer detailId, @RequestBody Map<String, String> payload,
            Principal principal) {
        Optional<Reclamation> optionalRec = reclamationService.findById(reclamationId);
        if (!optionalRec.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réclamation non trouvée");
        }

        Optional<ReclamationDetail> optionalDetail = reclamationService.findDetailById(detailId);
        if (!optionalDetail.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Détail d'envoi non trouvé");
        }
        ReclamationDetail detail = optionalDetail.get();

        // Mettre à jour les informations de retour
        detail.setFlagRetourAutorite(1);
        String dateRetourStr = payload.get("dateRetourAutorite");
        try {
            if (dateRetourStr != null && !dateRetourStr.isEmpty()) {
                LocalDateTime dateRetour = LocalDateTime.parse(dateRetourStr);
                detail.setDateRetourAutorite(dateRetour);
            } else {
                detail.setDateRetourAutorite(LocalDateTime.now());
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Format de date invalide");
        }
        detail.setReferenceRetourAutorite(payload.get("referenceRetourAutorite"));

        // Affecter l'utilisateur de retour
        Utilisateur user = utilisateurRepository.findByLogin(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        detail.setUserRetourAutorite(user);

        reclamationService.saveDetail(detail);
        Reclamation rec = optionalRec.get();
        if (reclamationService.hasRetourAutorite(rec)) {
            rec.setFlagRetourAutorite(1);
            reclamationService.updateReclamation(rec);
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Retour de l'autorité enregistré");
        return ResponseEntity.ok(response);

    }

    @PostMapping("/envoiReclamant/{reclamationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('UTILISATEUR')")
    public ResponseEntity<?> envoiReclamant(@PathVariable Integer reclamationId,
            @RequestBody Map<String, String> payload,
            Principal principal) {
        Optional<Reclamation> optionalRec = reclamationService.findById(reclamationId);
        if (!optionalRec.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réclamation non trouvée");
        }
        Reclamation rec = optionalRec.get();

        ReclamationDetail detail = new ReclamationDetail();
        detail.setReclamation(rec);
        detail.setNumInscription(rec.getNumInscription());
        detail.setFlagEnvoiReclamant(1);

        String dateEnvoiStr = payload.get("dateEnvoiReclamant");
        try {
            if (dateEnvoiStr != null && !dateEnvoiStr.isEmpty()) {
                LocalDateTime dateEnvoi = LocalDateTime.parse(dateEnvoiStr);
                detail.setDateEnvoiReclamant(dateEnvoi);
            } else {
                detail.setDateEnvoiReclamant(LocalDateTime.now());
            }
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body("Format de date invalide");
        }

        // Si une voie d'envoi est sélectionnée, récupérer l'objet VoieReponse
        String voieIdStr = payload.get("voieEnvoiReclamantId");
        if (voieIdStr != null && !voieIdStr.isEmpty()) {
            Integer voieId = Integer.valueOf(voieIdStr);
            VoieReponse voie = voieReponseRepository.findById(voieId)
                    .orElseThrow(() -> new RuntimeException("Voie non trouvée"));
            detail.setVoieEnvoiReclamant(voie);
        }
        detail.setReferenceEnvoiReclamant(payload.get("referenceEnvoiReclamant"));

        Utilisateur user = utilisateurRepository.findByLogin(principal.getName())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        detail.setUserEnvoiReclamant(user);

        reclamationService.saveDetail(detail);
        rec.setFlagCloture(1);
        rec.setFlagEnvoiReclamant(1);
        rec.setObservation2(payload.get("observation2"));
        reclamationService.updateReclamation(rec);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Envoi au réclamant enregistré");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/export")
    public void exportToExcel(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDepotStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateDepotEnd,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateInscriptionStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime dateInscriptionEnd,
            @RequestParam(required = false) String numInscription,
            @RequestParam(required = false) String referenceBo,
            @RequestParam(required = false) String identifiant,
            @RequestParam(required = false) String nomComplet,
            @RequestParam(required = false) String commune,
            @RequestParam(required = false) Integer typeReclamation,
            @RequestParam(required = false) String objetReclamation,
            @RequestParam(required = false) Integer annee,
            @RequestParam(required = false) Integer flagEnvoiAutorite,
            @RequestParam(required = false) Integer flagRetourAutorite,
            @RequestParam(required = false) Integer flagEnvoiReclamant,
            @RequestParam(required = false) Integer typeRequete,
            @RequestParam(required = false) Integer sourceReclamation,
            HttpServletResponse response) throws IOException {

        Workbook workbook = reclamationService.generateExcelExport(
                dateDepotStart, dateDepotEnd, dateInscriptionStart, dateInscriptionEnd, numInscription, referenceBo,
                identifiant, nomComplet, commune, typeReclamation, objetReclamation, annee, flagEnvoiAutorite,
                flagRetourAutorite,
                flagEnvoiReclamant,
                typeRequete, sourceReclamation);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=reclamations.xlsx");
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    @GetMapping("/stats/duration/{period}")
    public ResponseEntity<Map<String, Long>> getStatsByPeriod(@PathVariable String period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold;
        switch (period.toLowerCase()) {
            case "7days":
                threshold = now.minusDays(7);
                break;
            case "30days":
                threshold = now.minusDays(30);
                break;
            case "year":
                threshold = LocalDateTime.of(now.getYear(), 1, 1, 0, 0);
                break;
            default:
                return ResponseEntity.badRequest().body(null);
        }
        Map<String, Long> stats = reclamationService.getStatsByThreshold(threshold);
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/retourAutorite/details")
    public ResponseEntity<List<Reclamation>> getReclamationsPourRetourAvecDetails() {
        List<Reclamation> list = reclamationService.getReclamationsEnvoyeesAvecDetails();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/ouverture/{reclamationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('UTILISATEUR')")
    public ResponseEntity<?> ouvertureReclamation(@PathVariable Integer reclamationId) {
        Optional<Reclamation> opt = reclamationService.findById(reclamationId);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réclamation non trouvée");
        }
        Reclamation rec = opt.get();
        rec.setFlagCloture(null);
        reclamationService.updateReclamation(rec);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Réclamation réouverte");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cloture/{reclamationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('UTILISATEUR')")
    public ResponseEntity<?> clotureReclamation(@PathVariable Integer reclamationId) {
        Optional<Reclamation> opt = reclamationService.findById(reclamationId);
        if (!opt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Réclamation non trouvée");
        }
        Reclamation rec = opt.get();
        rec.setFlagCloture(1);
        reclamationService.updateReclamation(rec);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Réclamation clôturée");
        return ResponseEntity.ok(response);

    }

    @GetMapping("/ouverture/search")
    public ResponseEntity<List<Reclamation>> searchOuverture(
            @RequestParam(required = false, defaultValue = "") String numInscription,
            @RequestParam(required = false, defaultValue = "") String referenceBo) {
        List<Reclamation> results = reclamationService.searchOuverture(numInscription, referenceBo);
        return ResponseEntity.ok(results);
    }

    // Recherche pour clôture : on cherche dans les réclamations où flagCloture IS
    // NULL et flagEnvoiReclamant = 1
    @GetMapping("/cloture/search")
    public ResponseEntity<List<Reclamation>> searchCloture(
            @RequestParam(required = false, defaultValue = "") String numInscription,
            @RequestParam(required = false, defaultValue = "") String referenceBo) {
        List<Reclamation> results = reclamationService.searchCloture(numInscription, referenceBo);
        return ResponseEntity.ok(results);
    }

}
