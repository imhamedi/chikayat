package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.Commune;
import com.reclamations.chikayat.entity.Reclamation;
import com.reclamations.chikayat.entity.ReclamationDetail;
import com.reclamations.chikayat.entity.ReclamationSummary;
import com.reclamations.chikayat.entity.SequenceGenerator;
import com.reclamations.chikayat.repository.CommuneRepository;
import com.reclamations.chikayat.repository.ReclamationDetailRepository;
import com.reclamations.chikayat.repository.ReclamationRepository;
import com.reclamations.chikayat.repository.SequenceGeneratorRepository;
import com.reclamations.chikayat.repository.SourceReclamationRepository;
import com.reclamations.chikayat.repository.TypeDestinataireRepository;
import com.reclamations.chikayat.repository.TypeReclamationRepository;
import com.reclamations.chikayat.repository.TypeRequeteRepository;
import com.reclamations.chikayat.repository.UtilisateurRepository;
import com.reclamations.chikayat.repository.VoieReponseRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.HashMap;
import java.util.Map;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ReclamationService {

    @Autowired
    private ReclamationRepository reclamationRepository;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private CommuneRepository communeRepository;
    private final AtomicLong sequenceGenerator = new AtomicLong(System.currentTimeMillis());
    @Autowired
    private TypeDestinataireRepository typedestinataireRepository;
    @Autowired
    private TypeReclamationRepository typereclamationRepository;
    @Autowired
    private TypeRequeteRepository typerequeteRepository;
    @Autowired
    private SourceReclamationRepository sourcereclamationRepository;
    @Autowired
    private ReclamationDetailRepository detailRepository;

    @Autowired
    private VoieReponseRepository voiereponseRepository;

    @Autowired
    private UtilisateurRepository utilisateurrepository;
    // temporaire

    public List<String> getAllCommunes() {
        return communeRepository.findAll().stream()
                .map(Commune::getNom)
                .distinct()
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getAllTypesDestinataires() {
        return typedestinataireRepository.findAll().stream()
                .map(tr -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tr.getId());
                    map.put("nomLangue2", tr.getNomLangue2());
                    return map;
                })
                .collect(Collectors.toList());

    }

    public List<Map<String, Object>> getAllTypesReclamations() {
        return typereclamationRepository.findAll().stream()
                .map(tr -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tr.getId());
                    map.put("nomLangue2", tr.getNomLangue2());
                    return map;
                })
                .collect(Collectors.toList());

    }

    public List<Map<String, Object>> getAllSourcesReclamations() {
        return sourcereclamationRepository.findAll().stream()
                .map(tr -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tr.getId());
                    map.put("nomLangue2", tr.getNomLangue2());
                    return map;
                })
                .collect(Collectors.toList());

    }

    public List<Map<String, Object>> getAllVoiesRetours() {
        return voiereponseRepository.findAll().stream()
                .map(tr -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tr.getId());
                    map.put("nomLangue2", tr.getNomLangue2());
                    return map;
                })
                .collect(Collectors.toList());

    }

    public List<Map<String, Object>> getAllLoginss() {
        return utilisateurrepository.findAll().stream()
                .map(tr -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tr.getId());
                    map.put("login", tr.getLogin());
                    return map;
                })
                .collect(Collectors.toList());

    }

    public List<Map<String, Object>> getAllTypesRequetes() {
        return typerequeteRepository.findAll().stream()
                .map(tr -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", tr.getId());
                    map.put("nomLangue2", tr.getNomLangue2());
                    return map;
                })
                .collect(Collectors.toList());
    }

    public Reclamation createReclamation(Reclamation reclamation) {
        if (reclamation.getNomComplet() == null || reclamation.getObjetReclamation() == null
                || reclamation.getTypeRequete() == null) {
            throw new RuntimeException("Le nom complet et l'objet de la réclamation sont obligatoires.");
        }
        reclamation.setNumInscription(sequenceService.generateNumInscription());
        reclamation.setDateDepot(LocalDateTime.now());
        reclamation.setDateInscription(LocalDateTime.now());

        return reclamationRepository.save(reclamation);
    }

    private static final ConcurrentHashMap<String, Boolean> reservedSequences = new ConcurrentHashMap<>();
    @Autowired
    private SequenceGeneratorRepository sequenceGeneratorRepository;

    public String generateNumInscription() {
        long newSequence = sequenceGenerator.incrementAndGet();
        return "REC-" + newSequence; // Numéro unique basé sur le timestamp
    }

    // Libérer un numInscription si la réclamation n’est pas validée
    public void releaseNumInscription(String numInscription) {
        if (!numInscription.matches("\\d{6}\\.\\d{4}")) {
            throw new RuntimeException("Format de numéro d'inscription invalide.");
        }

        String anneeShort = numInscription.substring(0, 2);
        String annee = "20" + anneeShort;
        int sequenceNumber = Integer.parseInt(numInscription.substring(7));

        Optional<SequenceGenerator> optSeq = sequenceGeneratorRepository.findByAnnee(annee);
        if (optSeq.isPresent()) {
            SequenceGenerator seq = optSeq.get();
            if (seq.getDernierNumero() == sequenceNumber) {
                seq.setDernierNumero(seq.getDernierNumero() - 1);
                sequenceGeneratorRepository.save(seq);
            }
        }
        System.out.println("Requête de libération reçue pour le numéro : " + numInscription);
    }

    @Transactional
    public Reclamation save(Reclamation reclamation) {
        return reclamationRepository.save(reclamation);
    }

    public List<ReclamationSummary> getReclamationSummaryByIdentifiant(String identifiant) {
        List<Reclamation> list = reclamationRepository.findByIdentifiant(identifiant);

        return list.stream().map(r -> {
            ReclamationSummary summary = new ReclamationSummary();
            summary.setNumInscription(r.getNumInscription());
            summary.setDateDepot(r.getDateDepot());
            summary.setNomComplet(r.getNomComplet());
            summary.setObjetReclamation(r.getObjetReclamation());
            if (r.getTypeReclamation() != null) {
                String label = (r.getTypeReclamation().getNomLangue2() != null
                        && !r.getTypeReclamation().getNomLangue2().isEmpty())
                                ? r.getTypeReclamation().getNomLangue2()
                                : r.getTypeReclamation().getLibelle();
                summary.setTypeReclamationLabel(label);
            } else {
                summary.setTypeReclamationLabel("");
            }
            if (r.getTypeRequete() != null) {
                String label = (r.getTypeRequete().getNomLangue2() != null
                        && !r.getTypeRequete().getNomLangue2().isEmpty())
                                ? r.getTypeRequete().getNomLangue2()
                                : r.getTypeRequete().getLibelle();
                summary.setTypeRequeteLabel(label);
            } else {
                summary.setTypeRequeteLabel("");
            }
            summary.setFlagEnvoiAutoriteText(hasEnvoiAutorite(r) ? "تم إرساله" : "لم ترسل");
            summary.setFlagRetourAutoriteText(hasRetourAutorite(r) ? "تم إرساله" : "لم ترسل");
            summary.setFlagEnvoiReclamantText(hasEnvoiReclamant(r) ? "تم إرساله" : "لم ترسل");
            summary.setInstructionsGouverneur(r.getInstructionsGouverneur());
            return summary;
        }).collect(Collectors.toList());
    }

    public int countReclamationsByIdentifiant(String identifiant) {
        List<Reclamation> list = reclamationRepository.findByIdentifiant(identifiant);
        return list.size();
    }

    private static final Logger logger = LoggerFactory.getLogger(ReclamationService.class);

    public Page<Reclamation> searchReclamations(
            LocalDateTime dateDepotStart,
            LocalDateTime dateDepotEnd,
            LocalDateTime dateInscriptionStart,
            LocalDateTime dateInscriptionEnd,
            String numInscription,
            String referenceBo,
            String identifiant,
            String nomComplet,
            String commune,
            Integer typeReclamation,
            String objetReclamation,
            Integer annee,
            String telephone,
            String instructionsGouverneur,
            String observation1,
            String observation2,
            Integer sourceReclamation,
            Integer typeRequete,
            Integer flagCloture,
            Integer flagEnvoiAutorite,
            LocalDateTime dateEnvoiAutoriteStart,
            LocalDateTime dateEnvoiAutoriteEnd,
            String referenceEnvoiAutorite,
            Integer flagRetourAutorite,
            LocalDateTime dateRetourAutoriteStart,
            LocalDateTime dateRetourAutoriteEnd,
            String referenceRetourAutorite,
            Integer flagEnvoiReclamant,
            LocalDateTime dateEnvoiReclamantStart,
            LocalDateTime dateEnvoiReclamantEnd,
            Integer voieEnvoiReclamant,
            String referenceEnvoiReclamant,
            Integer userEnvoiAutorite,
            Integer userRetourAutorite,
            Integer userEnvoiReclamant,
            Pageable pageable) {
        logger.info(
                "Recherche avec paramètres: dateDepotStart={}, dateDepotEnd={}, dateInscriptionStart={}, dateInscriptionEnd={}, numInscription={}, referenceBo={}, identifiant={}, nomComplet={}, commune={}, typeReclamation={}, objetReclamation={}, annee={}, telephone={}, instructionsGouverneur={}, observation1={}, observation2={}, sourceReclamation={}, typeRequete={}, flagEnvoiAutorite={}, dateEnvoiAutoriteStart={}, dateEnvoiAutoriteEnd={}, referenceEnvoiAutorite={}, flagRetourAutorite={}, dateRetourAutoriteStart={}, dateRetourAutoriteEnd={}, referenceRetourAutorite={}, flagEnvoiReclamant={}, dateEnvoiReclamantStart={}, dateEnvoiReclamantEnd={}, voieEnvoiReclamant={}, referenceEnvoiReclamant={}, userEnvoiAutorite={}, userRetourAutorite={}, userEnvoiReclamant={}",
                dateDepotStart, dateDepotEnd, dateInscriptionStart, dateInscriptionEnd, numInscription, referenceBo,
                identifiant, nomComplet, commune, typeReclamation, objetReclamation, annee, telephone,
                instructionsGouverneur,
                observation1, observation2, sourceReclamation, typeRequete, flagEnvoiAutorite, dateEnvoiAutoriteStart,
                dateEnvoiAutoriteEnd,
                referenceEnvoiAutorite, flagRetourAutorite, dateRetourAutoriteStart, dateRetourAutoriteEnd,
                referenceRetourAutorite,
                flagEnvoiReclamant, dateEnvoiReclamantStart, dateEnvoiReclamantEnd, voieEnvoiReclamant,
                referenceEnvoiReclamant,
                userEnvoiAutorite, userRetourAutorite, userEnvoiReclamant);

        return reclamationRepository.searchReclamations(
                dateDepotStart, dateDepotEnd, dateInscriptionStart, dateInscriptionEnd,
                numInscription, referenceBo, identifiant, nomComplet, commune,
                typeReclamation, objetReclamation, annee, telephone, instructionsGouverneur, observation1, observation2,
                sourceReclamation, typeRequete, flagCloture,
                flagEnvoiAutorite, dateEnvoiAutoriteStart, dateEnvoiAutoriteEnd, referenceEnvoiAutorite,
                flagRetourAutorite, dateRetourAutoriteStart, dateRetourAutoriteEnd, referenceRetourAutorite,
                flagEnvoiReclamant, dateEnvoiReclamantStart, dateEnvoiReclamantEnd, voieEnvoiReclamant,
                referenceEnvoiReclamant, userEnvoiAutorite, userRetourAutorite, userEnvoiReclamant,
                pageable);
    }

    public Optional<Reclamation> findById(Integer id) {
        return reclamationRepository.findById(id);
    }

    @Transactional
    public Reclamation updateReclamation(Reclamation updatedReclamation) {
        Reclamation existing = reclamationRepository.findById(updatedReclamation.getId())
                .orElseThrow(() -> new RuntimeException("Réclamation non trouvée"));

        existing.setDateDepot(updatedReclamation.getDateDepot());
        existing.setDateInscription(updatedReclamation.getDateInscription());
        existing.setIdentifiant(updatedReclamation.getIdentifiant());
        existing.setNomComplet(updatedReclamation.getNomComplet());
        existing.setCommune(updatedReclamation.getCommune());
        existing.setAdresse(updatedReclamation.getAdresse());
        existing.setTelephone(updatedReclamation.getTelephone());
        existing.setReferenceBo(updatedReclamation.getReferenceBo());
        existing.setObjetReclamation(updatedReclamation.getObjetReclamation());
        existing.setPieceJointeReclamation(updatedReclamation.getPieceJointeReclamation());
        existing.setAnnee(updatedReclamation.getAnnee());
        existing.setFlagEnvoiAutorite(updatedReclamation.getFlagEnvoiAutorite());

        List<ReclamationDetail> updatedDetails = updatedReclamation.getDetails();
        List<ReclamationDetail> existingDetails = existing.getDetails();
        if (existingDetails != null) {
            existingDetails.removeIf(existingDetail -> updatedDetails == null ||
                    updatedDetails.stream().noneMatch(updatedDetail -> updatedDetail.getId() != null
                            && updatedDetail.getId().equals(existingDetail.getId())));
        }

        if (updatedDetails != null) {
            for (ReclamationDetail updatedDetail : updatedDetails) {
                if (updatedDetail.getId() != null) {
                    ReclamationDetail existingDetail = existingDetails.stream()
                            .filter(d -> d.getId().equals(updatedDetail.getId()))
                            .findFirst()
                            .orElse(null);
                    if (existingDetail != null) {
                        existingDetail.setFlagEnvoiAutorite(updatedDetail.getFlagEnvoiAutorite());
                        existingDetail.setDateEnvoiAutorite(updatedDetail.getDateEnvoiAutorite());
                        existingDetail.setTypeDestinataire(updatedDetail.getTypeDestinataire());
                        existingDetail.setReferenceEnvoiAutorite(updatedDetail.getReferenceEnvoiAutorite());
                        existingDetail.setFlagRetourAutorite(updatedDetail.getFlagRetourAutorite());
                        existingDetail.setDateRetourAutorite(updatedDetail.getDateRetourAutorite());
                        existingDetail.setReferenceRetourAutorite(updatedDetail.getReferenceRetourAutorite());
                        existingDetail.setFlagEnvoiReclamant(updatedDetail.getFlagEnvoiReclamant());
                        existingDetail.setDateEnvoiReclamant(updatedDetail.getDateEnvoiReclamant());
                        existingDetail.setVoieEnvoiReclamant(updatedDetail.getVoieEnvoiReclamant());
                        existingDetail.setReferenceEnvoiReclamant(updatedDetail.getReferenceEnvoiReclamant());
                        existingDetail.setUserEnvoiAutorite(updatedDetail.getUserEnvoiAutorite());
                        existingDetail.setUserRetourAutorite(updatedDetail.getUserRetourAutorite());
                        existingDetail.setUserEnvoiReclamant(updatedDetail.getUserEnvoiReclamant());
                    }
                }
            }
        }

        return reclamationRepository.save(existing);
    }

    public void deleteReclamation(Integer id) {
        reclamationRepository.deleteById(id);
    }

    public String concatData(Reclamation rec) {
        int flagEnvoiAutorite = hasEnvoiAutorite(rec) ? 1 : 0;
        int flagRetourAutorite = hasRetourAutorite(rec) ? 1 : 0;
        int flagEnvoiReclamant = hasEnvoiReclamant(rec) ? 1 : 0;

        return rec.getDateDepot() + "|" + rec.getDateInscription() + "|" + rec.getNumInscription() + "|" +
                rec.getIdentifiant() + "|" + rec.getNomComplet() + "|" + rec.getCommune() + "|" +
                rec.getAdresse() + "|" + rec.getTelephone() + "|" + rec.getObjetReclamation() + "|" +
                rec.getPieceJointeReclamation() + "|" + rec.getAnnee() + "|" +
                flagEnvoiAutorite + "|" + flagRetourAutorite + "|" + flagEnvoiReclamant + "|"
                + rec.getInstructionsGouverneur();
    }

    public Long countEnCoursDepuis(String durationType, int durationValue) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold;
        switch (durationType.toLowerCase()) {
            case "jours":
                threshold = now.minusDays(durationValue);
                break;
            case "mois":
                threshold = now.minusMonths(durationValue);
                break;
            case "années":
                threshold = now.minusYears(durationValue);
                break;
            default:
                throw new IllegalArgumentException("Type de durée inconnu");
        }
        return reclamationRepository.countEnCoursDepuis(threshold);
    }

    public Long countTotal() {
        return reclamationRepository.countTotal();
    }

    public Long countEnCours() {
        return reclamationRepository.countEnCours();
    }

    public Long countEnvoyeesAutorite() {
        return reclamationRepository.countEnvoyeesAutorite();
    }

    public Long countReponduAutorite() {
        return reclamationRepository.countReponduAutorite();
    }

    public Long countCloturees() {
        return reclamationRepository.countCloturees();
    }

    /**
     * Retourne la liste des réclamations filtrées selon le flag.
     * Les valeurs possibles pour le flag sont :
     * "total" : toutes les réclamations
     * "enCours" : réclamations en cours (flagEnvoiAutorite null ou égal à 0)
     * "envoyeesAutorite" : réclamations envoyées aux autorités (flagEnvoiAutorite =
     * 1)
     * "reponduAutorite" : réclamations pour lesquelles un retour d'autorité est
     * présent (flagRetourAutorite = 1)
     * "cloturees" : réclamations clôturées (flagEnvoiReclamant = 1)
     *
     * @param flag le critère de filtrage
     * @return la liste des réclamations correspondantes
     */
    public List<Reclamation> getReclamationsByFlag(String flag) {
        if ("total".equalsIgnoreCase(flag)) {
            return reclamationRepository.findAll();
        } else if ("enCours".equalsIgnoreCase(flag)) {
            return reclamationRepository.findEnCours();
        } else if ("envoyeesAutorite".equalsIgnoreCase(flag)) {
            return reclamationRepository.findEnvoyeesAutorite();
        } else if ("reponduAutorite".equalsIgnoreCase(flag)) {
            return reclamationRepository.findReponduAutorite();
        } else if ("cloturees".equalsIgnoreCase(flag)) {
            return reclamationRepository.findCloturees();
        } else {
            throw new IllegalArgumentException("Filtre inconnu : " + flag);
        }
    }

    // Méthode de recherche renvoyant une List (sans pagination)
    public List<Reclamation> searchReclamationsList(
            LocalDateTime dateDepotStart,
            LocalDateTime dateDepotEnd,
            LocalDateTime dateInscriptionStart,
            LocalDateTime dateInscriptionEnd,
            String numInscription,
            String referenceBo,
            String identifiant,
            String nomComplet,
            String commune,
            Integer typeReclamation,
            String objetReclamation,
            Integer annee,
            Integer flagEnvoiAutorite,
            Integer flagRetourAutorite,
            Integer flagEnvoiReclamant,
            Integer typeRequete,
            Integer sourceReclamation) {
        logger.info(
                "Paramètres reçus : dateDepotStart={}, dateDepotEnd={}, dateInscriptionStart={}, dateInscriptionEnd={}, numInscription={}, referenceBo={}, identifiant={}, nomComplet={}, commune={}, typeReclamation={}, objetReclamation={}, annee={}, flagEnvoiAutorite={}, flagRetourAutorite={}, flagEnvoiReclamant={}, typeRequete={},sourceReclamation ={}",
                dateDepotStart, dateDepotEnd, dateInscriptionStart, dateInscriptionEnd, numInscription, referenceBo,
                identifiant, nomComplet, commune, typeReclamation, objetReclamation, annee, flagEnvoiAutorite,
                flagRetourAutorite, flagEnvoiReclamant,
                typeRequete, sourceReclamation);

        return reclamationRepository.searchReclamationsList(
                dateDepotStart, dateDepotEnd, dateInscriptionStart, dateInscriptionEnd,
                numInscription, referenceBo, identifiant, nomComplet, commune, typeReclamation,
                objetReclamation, annee, flagEnvoiAutorite,
                flagRetourAutorite,
                flagEnvoiReclamant,
                typeRequete, sourceReclamation);
    }

    // Méthode de génération du fichier Excel
    public Workbook generateExcelExport(
            LocalDateTime dateDepotStart,
            LocalDateTime dateDepotEnd,
            LocalDateTime dateInscriptionStart,
            LocalDateTime dateInscriptionEnd,
            String numInscription,
            String referenceBo,
            String identifiant,
            String nomComplet,
            String commune,
            Integer typeReclamation,
            String objetReclamation,
            Integer annee,
            Integer flagEnvoiAutorite,
            Integer flagRetourAutorite,
            Integer flagEnvoiReclamant,
            Integer typeRequete,
            Integer sourceReclamation) {

        // Récupérer la liste complète des réclamations filtrées
        List<Reclamation> reclamations = searchReclamationsList(
                dateDepotStart, dateDepotEnd, dateInscriptionStart, dateInscriptionEnd,
                numInscription, referenceBo, identifiant, nomComplet, commune, typeReclamation,
                objetReclamation, annee, flagEnvoiAutorite,
                flagRetourAutorite, flagEnvoiReclamant,
                typeRequete, sourceReclamation);

        System.out.println("Nombre de réclamations à exporter : " + reclamations.size());

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Reclamations");

        // Définir l'en-tête avec toutes les colonnes souhaitées
        String[] columns = {
                "ID Reclamation", "Date Depot", "Date Inscription", "Num Inscription", "Type Identifiant",
                "Identifiant", "Nom Complet", "Commune", "Adresse", "Telephone", "Reference BO",
                "Piece Jointe", "Type Requete", "Type Reclamation", "Objet Reclamation",
                "Annee", "Observation1", "Observation2",
                "Observation3", "Instructions Gouverneur", "User Saisie", "Source Reclamation",
                // Colonnes du detail
                "ID Detail", "Flag Envoi Autorite", "Flag Retour Autorite", "Flag Envoi Réclamant",
                "Date Envoi Autorite", "Type Destinataire",
                "Reference Envoi Autorite", "Date Retour Autorite",
                "Reference Retour Autorite",
                "Date Envoi Reclamant", "Voie Envoi Reclamant", "Reference Envoi Reclamant",
                "User Envoi Autorite", "User Retour Autorite", "User Envoi Reclamant"
        };

        // Création de la ligne d'en-tête
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < columns.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(columns[i]);
        }

        // Remplissage des données
        int rowCount = 1;
        for (Reclamation rec : reclamations) {
            if (rec.getDetails() == null || rec.getDetails().isEmpty()) {
                // S'il n'y a pas de details, écrire une ligne avec seulement les infos de
                // reclamation
                Row row = sheet.createRow(rowCount++);
                fillReclamationCells(row, rec);
            } else {
                // Si on a des détails, une ligne par détail
                for (ReclamationDetail detail : rec.getDetails()) {
                    Row row = sheet.createRow(rowCount++);
                    fillReclamationCells(row, rec);
                    fillDetailCells(row, detail);
                }
            }

        }
        return workbook;
    }

    private void fillReclamationCells(Row row, Reclamation rec) {
        int col = 0;
        row.createCell(col++).setCellValue(rec.getId() != null ? rec.getId() : 0);
        row.createCell(col++).setCellValue(rec.getDateDepot() != null ? rec.getDateDepot().toString() : "");
        row.createCell(col++).setCellValue(rec.getDateInscription() != null ? rec.getDateInscription().toString() : "");
        row.createCell(col++).setCellValue(rec.getNumInscription() != null ? rec.getNumInscription() : "");
        row.createCell(col++).setCellValue(rec.getTypeIdentifiant() != null ? rec.getTypeIdentifiant() : "");
        row.createCell(col++).setCellValue(rec.getIdentifiant() != null ? rec.getIdentifiant() : "");
        row.createCell(col++).setCellValue(rec.getNomComplet() != null ? rec.getNomComplet() : "");
        row.createCell(col++).setCellValue(rec.getCommune() != null ? rec.getCommune() : "");
        row.createCell(col++).setCellValue(rec.getAdresse() != null ? rec.getAdresse() : "");
        row.createCell(col++).setCellValue(rec.getTelephone() != null ? rec.getTelephone() : "");
        row.createCell(col++).setCellValue(rec.getReferenceBo() != null ? rec.getReferenceBo() : "");
        row.createCell(col++).setCellValue(rec.getPieceJointe() != null ? rec.getPieceJointe() : "");
        row.createCell(col++).setCellValue(rec.getTypeRequete() != null ? rec.getTypeRequete().getNomLangue2() : "");
        row.createCell(col++)
                .setCellValue(rec.getTypeReclamation() != null ? rec.getTypeReclamation().getNomLangue2() : "");
        row.createCell(col++).setCellValue(rec.getObjetReclamation() != null ? rec.getObjetReclamation() : "");

        row.createCell(col++).setCellValue(rec.getAnnee() != null ? rec.getAnnee() : 0);
        row.createCell(col++).setCellValue(rec.getObservation1() != null ? rec.getObservation1() : "");
        row.createCell(col++).setCellValue(rec.getObservation2() != null ? rec.getObservation2() : "");
        row.createCell(col++).setCellValue(rec.getObservation3() != null ? rec.getObservation3() : "");
        row.createCell(col++)
                .setCellValue(rec.getInstructionsGouverneur() != null ? rec.getInstructionsGouverneur() : "");
        row.createCell(col++).setCellValue(rec.getUserSaisie() != null ? rec.getUserSaisie().getLogin() : "");
        row.createCell(col++)
                .setCellValue(rec.getSourceReclamation() != null ? rec.getSourceReclamation().getNomLangue2() : "");
    }

    // Méthode pour remplir les cellules du détail
    private void fillDetailCells(Row row, ReclamationDetail detail) {
        int col = 22; // Index après les colonnes de Reclamation
        row.createCell(col++).setCellValue(detail.getId() != null ? detail.getId() : 0);
        row.createCell(col++).setCellValue(detail.getSafeFlagEnvoiAutorite());
        row.createCell(col++).setCellValue(detail.getSafeFlagRetourAutorite());
        row.createCell(col++).setCellValue(detail.getSafeFlagEnvoiReclamant());

        row.createCell(col++)
                .setCellValue(detail.getDateEnvoiAutorite() != null ? detail.getDateEnvoiAutorite().toString() : "");
        row.createCell(col++)
                .setCellValue(detail.getTypeDestinataire() != null ? detail.getTypeDestinataire().getNomLangue2() : "");
        row.createCell(col++)
                .setCellValue(detail.getReferenceEnvoiAutorite() != null ? detail.getReferenceEnvoiAutorite() : "");
        row.createCell(col++)
                .setCellValue(detail.getDateRetourAutorite() != null ? detail.getDateRetourAutorite().toString() : "");
        row.createCell(col++).setCellValue(detail.getReferenceRetourAutorite());
        row.createCell(col++)
                .setCellValue(detail.getDateEnvoiReclamant() != null ? detail.getDateEnvoiReclamant().toString() : "");
        row.createCell(col++).setCellValue(
                detail.getVoieEnvoiReclamant() != null ? detail.getVoieEnvoiReclamant().getNomLangue2() : "");
        row.createCell(col++).setCellValue(detail.getReferenceEnvoiReclamant());
        row.createCell(col++)
                .setCellValue(detail.getUserEnvoiAutorite() != null ? detail.getUserEnvoiAutorite().getLogin() : "");
        row.createCell(col++)
                .setCellValue(detail.getUserRetourAutorite() != null ? detail.getUserRetourAutorite().getLogin() : "");
        row.createCell(col++)
                .setCellValue(detail.getUserEnvoiReclamant() != null ? detail.getUserEnvoiReclamant().getLogin() : "");
    }

    public Map<String, Long> getStatsByThreshold(LocalDateTime threshold) {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", reclamationRepository.countTotalSince(threshold));
        stats.put("enCours", reclamationRepository.countEnCoursSince(threshold));
        stats.put("envoyeesAutorite", reclamationRepository.countEnvoyeesAutoriteSince(threshold));
        stats.put("reponduAutorite", reclamationRepository.countReponduAutoriteSince(threshold));
        stats.put("cloturees", reclamationRepository.countClotureesSince(threshold));
        return stats;
    }

    public boolean hasEnvoiAutorite(Reclamation rec) {
        return detailRepository.countByReclamationAndFlagEnvoiAutorite(rec, 1) > 0;
    }

    public boolean hasRetourAutorite(Reclamation rec) {
        return detailRepository.countByReclamationAndFlagRetourAutorite(rec, 1) > 0;
    }

    public boolean hasEnvoiReclamant(Reclamation rec) {
        return detailRepository.countByReclamationAndFlagEnvoiReclamant(rec, 1) > 0;
    }

    public ReclamationDetail saveDetail(ReclamationDetail detail) {
        return detailRepository.save(detail);
    }

    public Optional<ReclamationDetail> findDetailById(Integer detailId) {
        return detailRepository.findById(detailId);
    }

    public List<Reclamation> getReclamationsEnvoyeesAvecDetails() {
        return reclamationRepository.findReclamationsEnvoyeesAvecDetails();
    }

    public List<Reclamation> searchOuverture(String numInscription, String referenceBo) {
        return reclamationRepository.searchOuverture(numInscription, referenceBo);
    }

    public List<Reclamation> searchCloture(String numInscription, String referenceBo) {
        return reclamationRepository.searchCloture(numInscription, referenceBo);
    }

}
