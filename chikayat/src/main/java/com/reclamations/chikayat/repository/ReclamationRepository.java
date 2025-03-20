package com.reclamations.chikayat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.reclamations.chikayat.entity.Reclamation;
import com.reclamations.chikayat.entity.ReclamationSummary;

import java.time.LocalDateTime;
import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Integer> {
        List<Reclamation> findByNomCompletContaining(String nom);

        List<Reclamation> findByIdentifiant(String identifiant);

        Page<Reclamation> findAll(Pageable pageable);

        @Query("SELECT r FROM Reclamation r " +
                        "WHERE (:dateDepotStart IS NULL OR r.dateDepot >= :dateDepotStart) " +
                        "  AND (:dateDepotEnd IS NULL OR r.dateDepot <= :dateDepotEnd) " +
                        "  AND (:dateInscriptionStart IS NULL OR r.dateInscription >= :dateInscriptionStart) " +
                        "  AND (:dateInscriptionEnd IS NULL OR r.dateInscription <= :dateInscriptionEnd) " +
                        "  AND (:numInscription IS NULL OR r.numInscription LIKE CONCAT('%', :numInscription, '%')) " +
                        "  AND (:referenceBo IS NULL OR r.referenceBo LIKE CONCAT('%', :referenceBo, '%')) " +
                        "  AND (:identifiant IS NULL OR r.identifiant LIKE CONCAT('%', :identifiant, '%')) " +
                        "  AND (:nomComplet IS NULL OR r.nomComplet LIKE CONCAT('%', :nomComplet, '%')) " +
                        "  AND (:commune IS NULL OR r.commune = :commune) " +
                        "  AND (:sourceReclamation IS NULL OR r.sourceReclamation.id = :sourceReclamation) " +
                        "  AND (:typeReclamation IS NULL OR r.typeReclamation.id = :typeReclamation) " +
                        "  AND (:objetReclamation IS NULL OR r.objetReclamation LIKE CONCAT('%', :objetReclamation, '%')) "
                        +
                        "  AND (:annee IS NULL OR r.annee = :annee) " +
                        "  AND (:telephone IS NULL OR r.telephone LIKE CONCAT('%', :telephone, '%')) " +
                        "  AND (:instructionsGouverneur IS NULL OR r.instructionsGouverneur LIKE CONCAT('%', :instructionsGouverneur, '%')) "
                        +
                        "  AND (:observation1 IS NULL OR r.observation1 LIKE CONCAT('%', :observation1, '%')) " +
                        "  AND (:observation2 IS NULL OR r.observation2 LIKE CONCAT('%', :observation2, '%')) " +
                        "  AND (:flagCloture IS NULL OR r.flagCloture = :flagCloture) " +
                        // Critères depuis la table détail
                        "  AND (:flagEnvoiAutorite IS NULL OR " +
                        "       (:flagEnvoiAutorite = 1 AND EXISTS (SELECT 1 FROM ReclamationDetail d1 " +
                        "                                          WHERE d1.reclamation = r AND d1.flagEnvoiAutorite = 1)) "
                        +
                        "       OR (:flagEnvoiAutorite = 0)) " +
                        "  AND (:dateEnvoiAutoriteStart IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d2 " +
                        "                                                   WHERE d2.reclamation = r AND d2.dateEnvoiAutorite >= :dateEnvoiAutoriteStart)) "
                        +
                        "  AND (:dateEnvoiAutoriteEnd IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d3 " +
                        "                                                 WHERE d3.reclamation = r AND d3.dateEnvoiAutorite <= :dateEnvoiAutoriteEnd)) "
                        +
                        "  AND (:referenceEnvoiAutorite IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d4 " +
                        "                                                     WHERE d4.reclamation = r AND d4.referenceEnvoiAutorite LIKE CONCAT('%', :referenceEnvoiAutorite, '%'))) "
                        +
                        "  AND (:flagRetourAutorite IS NULL OR " +
                        "       (:flagRetourAutorite = 1 AND EXISTS (SELECT 1 FROM ReclamationDetail d5 " +
                        "                                           WHERE d5.reclamation = r AND d5.flagRetourAutorite = 1)) "
                        +
                        "       OR (:flagRetourAutorite = 0)) " +
                        "  AND (:dateRetourAutoriteStart IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d6 " +
                        "                                                      WHERE d6.reclamation = r AND d6.dateRetourAutorite >= :dateRetourAutoriteStart)) "
                        +
                        "  AND (:dateRetourAutoriteEnd IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d7 " +
                        "                                                    WHERE d7.reclamation = r AND d7.dateRetourAutorite <= :dateRetourAutoriteEnd)) "
                        +
                        "  AND (:referenceRetourAutorite IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d8 " +
                        "                                                        WHERE d8.reclamation = r AND d8.referenceRetourAutorite LIKE CONCAT('%', :referenceRetourAutorite, '%'))) "
                        +
                        "  AND (:flagEnvoiReclamant IS NULL OR " +
                        "       (:flagEnvoiReclamant = 1 AND EXISTS (SELECT 1 FROM ReclamationDetail d9 " +
                        "                                             WHERE d9.reclamation = r AND d9.flagEnvoiReclamant = 1)) "
                        +
                        "       OR (:flagEnvoiReclamant = 0)) " +
                        "  AND (:dateEnvoiReclamantStart IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d10 " +
                        "                                                    WHERE d10.reclamation = r AND d10.dateEnvoiReclamant >= :dateEnvoiReclamantStart)) "
                        +
                        "  AND (:dateEnvoiReclamantEnd IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d11 " +
                        "                                                  WHERE d11.reclamation = r AND d11.dateEnvoiReclamant <= :dateEnvoiReclamantEnd)) "
                        +
                        "  AND (:voieEnvoiReclamant IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d12 " +
                        "                                               WHERE d12.reclamation = r AND d12.voieEnvoiReclamant.id = :voieEnvoiReclamant)) "
                        +
                        "  AND (:referenceEnvoiReclamant IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d13 " +
                        "                                                      WHERE d13.reclamation = r AND d13.referenceEnvoiReclamant LIKE CONCAT('%', :referenceEnvoiReclamant, '%'))) "
                        +
                        "  AND (:userEnvoiAutorite IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d14 " +
                        "                                               WHERE d14.reclamation = r AND d14.userEnvoiAutorite.id = :userEnvoiAutorite)) "
                        +
                        "  AND (:userRetourAutorite IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d15 " +
                        "                                                WHERE d15.reclamation = r AND d15.userRetourAutorite.id = :userRetourAutorite)) "
                        +
                        "  AND (:userEnvoiReclamant IS NULL OR EXISTS (SELECT 1 FROM ReclamationDetail d16 " +
                        "                                                WHERE d16.reclamation = r AND d16.userEnvoiReclamant.id = :userEnvoiReclamant))")
        Page<Reclamation> searchReclamations(
                        @Param("dateDepotStart") LocalDateTime dateDepotStart,
                        @Param("dateDepotEnd") LocalDateTime dateDepotEnd,
                        @Param("dateInscriptionStart") LocalDateTime dateInscriptionStart,
                        @Param("dateInscriptionEnd") LocalDateTime dateInscriptionEnd,
                        @Param("numInscription") String numInscription,
                        @Param("referenceBo") String referenceBo,
                        @Param("identifiant") String identifiant,
                        @Param("nomComplet") String nomComplet,
                        @Param("commune") String commune,
                        @Param("typeReclamation") Integer typeReclamation,
                        @Param("objetReclamation") String objetReclamation,
                        @Param("annee") Integer annee,
                        @Param("telephone") String telephone,
                        @Param("instructionsGouverneur") String instructionsGouverneur,
                        @Param("observation1") String observation1,
                        @Param("observation2") String observation2,
                        @Param("sourceReclamation") Integer sourceReclamation,
                        @Param("typeRequete") Integer typeRequete,
                        @Param("flagCloture") Integer flagCloture,
                        // Critères détail
                        @Param("flagEnvoiAutorite") Integer flagEnvoiAutorite,
                        @Param("dateEnvoiAutoriteStart") LocalDateTime dateEnvoiAutoriteStart,
                        @Param("dateEnvoiAutoriteEnd") LocalDateTime dateEnvoiAutoriteEnd,
                        @Param("referenceEnvoiAutorite") String referenceEnvoiAutorite,
                        @Param("flagRetourAutorite") Integer flagRetourAutorite,
                        @Param("dateRetourAutoriteStart") LocalDateTime dateRetourAutoriteStart,
                        @Param("dateRetourAutoriteEnd") LocalDateTime dateRetourAutoriteEnd,
                        @Param("referenceRetourAutorite") String referenceRetourAutorite,
                        @Param("flagEnvoiReclamant") Integer flagEnvoiReclamant,
                        @Param("dateEnvoiReclamantStart") LocalDateTime dateEnvoiReclamantStart,
                        @Param("dateEnvoiReclamantEnd") LocalDateTime dateEnvoiReclamantEnd,
                        @Param("voieEnvoiReclamant") Integer voieEnvoiReclamant,
                        @Param("referenceEnvoiReclamant") String referenceEnvoiReclamant,
                        @Param("userEnvoiAutorite") Integer userEnvoiAutorite,
                        @Param("userRetourAutorite") Integer userRetourAutorite,
                        @Param("userEnvoiReclamant") Integer userEnvoiReclamant,
                        Pageable pageable);

        @Query("SELECT COUNT(r) FROM Reclamation r")
        Long countTotal();

        @Query("SELECT COUNT(DISTINCT r) FROM Reclamation r LEFT JOIN r.details d WHERE d IS NULL OR d.flagEnvoiAutorite IS NULL OR d.flagEnvoiAutorite = 0")
        Long countEnCours();

        @Query("SELECT COUNT(DISTINCT r) FROM Reclamation r LEFT JOIN r.details d WHERE d.flagEnvoiAutorite = 1 and d.flagRetourAutorite is null")
        Long countEnvoyeesAutorite();

        @Query("SELECT COUNT(DISTINCT r) FROM Reclamation r LEFT JOIN r.details d WHERE d.flagRetourAutorite = 1 and d.flagEnvoiReclamant is null")
        Long countReponduAutorite();

        @Query("SELECT COUNT(DISTINCT r) FROM Reclamation r LEFT JOIN r.details d WHERE d.flagEnvoiReclamant = 1")
        Long countCloturees();

        @Query("SELECT COUNT(DISTINCT r) FROM Reclamation r LEFT JOIN r.details d " +
                        "WHERE r.dateDepot <= :threshold AND (d.flagEnvoiReclamant IS NULL OR d.flagEnvoiReclamant = 0)")
        Long countEnCoursDepuis(@Param("threshold") LocalDateTime threshold);

        @Query("SELECT COUNT(r) FROM Reclamation r LEFT JOIN r.details d WHERE r.dateDepot >= :threshold AND (d.flagEnvoiAutorite IS NULL OR d.flagEnvoiAutorite = 0)")
        Long countEnCoursSince(@Param("threshold") LocalDateTime threshold);

        // pour exel List
        @Query("SELECT r FROM Reclamation r " +
                        "WHERE (:dateDepotStart IS NULL OR r.dateDepot >= :dateDepotStart) " +
                        "  AND (:dateDepotEnd IS NULL OR r.dateDepot <= :dateDepotEnd) " +
                        "  AND (:dateInscriptionStart IS NULL OR r.dateInscription >= :dateInscriptionStart) " +
                        "  AND (:dateInscriptionEnd IS NULL OR r.dateInscription <= :dateInscriptionEnd) " +
                        "  AND (:numInscription IS NULL OR r.numInscription LIKE CONCAT('%', :numInscription, '%')) " +
                        "  AND (:referenceBo IS NULL OR r.referenceBo LIKE CONCAT('%', :referenceBo, '%')) " +
                        "  AND (:identifiant IS NULL OR r.identifiant LIKE CONCAT('%', :identifiant, '%')) " +
                        "  AND (:nomComplet IS NULL OR r.nomComplet LIKE CONCAT('%', :nomComplet, '%')) " +
                        "  AND (:commune IS NULL OR r.commune = :commune) " +
                        "  AND (:typeReclamation IS NULL OR r.typeReclamation.id = :typeReclamation) " +
                        "  AND (:typeRequete IS NULL OR r.typeRequete.id = :typeRequete) " +
                        "  AND (:sourceReclamation IS NULL OR r.sourceReclamation.id = :sourceReclamation) " +

                        "  AND (:objetReclamation IS NULL OR r.objetReclamation LIKE CONCAT('%', :objetReclamation, '%')) "
                        +
                        "  AND (:annee IS NULL OR r.annee = :annee) " +
                        "  AND (:flagEnvoiAutorite IS NULL OR " +
                        "       ((:flagEnvoiAutorite = 1 AND EXISTS (SELECT 1 FROM ReclamationDetail d1 WHERE d1.reclamation = r AND d1.flagEnvoiAutorite = 1)) "
                        +
                        "       )" +
                        "  ) " +
                        "  AND (:flagRetourAutorite IS NULL OR " +
                        "       ((:flagRetourAutorite = 1 AND EXISTS (SELECT 1 FROM ReclamationDetail d2 WHERE d2.reclamation = r) "
                        +
                        "            AND NOT EXISTS (SELECT 1 FROM ReclamationDetail d2 WHERE d2.reclamation = r AND d2.flagRetourAutorite <> 1)) "
                        +
                        "       )" +
                        "  ) " +
                        "  AND (:flagEnvoiReclamant IS NULL OR " +
                        "       ((:flagEnvoiReclamant = 1 AND EXISTS (SELECT 1 FROM ReclamationDetail d3 WHERE d3.reclamation = r AND d3.flagEnvoiReclamant = 1)) "
                        +
                        "       )" +
                        "  )")
        List<Reclamation> searchReclamationsList(
                        @Param("dateDepotStart") LocalDateTime dateDepotStart,
                        @Param("dateDepotEnd") LocalDateTime dateDepotEnd,
                        @Param("dateInscriptionStart") LocalDateTime dateInscriptionStart,
                        @Param("dateInscriptionEnd") LocalDateTime dateInscriptionEnd,
                        @Param("numInscription") String numInscription,
                        @Param("referenceBo") String referenceBo,
                        @Param("identifiant") String identifiant,
                        @Param("nomComplet") String nomComplet,
                        @Param("commune") String commune,
                        @Param("typeReclamation") Integer typeReclamation,
                        @Param("objetReclamation") String objetReclamation,
                        @Param("annee") Integer annee,
                        @Param("flagEnvoiAutorite") Integer flagEnvoiAutorite,
                        @Param("flagRetourAutorite") Integer flagRetourAutorite,
                        @Param("flagEnvoiReclamant") Integer flagEnvoiReclamant,
                        @Param("typeRequete") Integer typeRequete,
                        @Param("sourceReclamation") Integer sourceReclamation);

        @Query("SELECT COUNT(r) FROM Reclamation r WHERE r.dateDepot >= :threshold")
        Long countTotalSince(@Param("threshold") LocalDateTime threshold);

        @Query("SELECT COUNT(DISTINCT r) FROM Reclamation r JOIN r.details d " +
                        "WHERE r.dateDepot >= :threshold AND d.flagEnvoiAutorite = 1 AND d.flagRetourAutorite IS NULL")
        Long countEnvoyeesAutoriteSince(@Param("threshold") LocalDateTime threshold);

        @Query("SELECT COUNT(DISTINCT r) FROM Reclamation r JOIN r.details d " +
                        "WHERE r.dateDepot >= :threshold AND d.flagRetourAutorite = 1 AND d.flagEnvoiReclamant IS NULL")
        Long countReponduAutoriteSince(@Param("threshold") LocalDateTime threshold);

        @Query("SELECT COUNT(DISTINCT r) FROM Reclamation r JOIN r.details d " +
                        "WHERE r.dateDepot >= :threshold AND d.flagEnvoiReclamant = 1")
        Long countClotureesSince(@Param("threshold") LocalDateTime threshold);

        @Query("SELECT new com.reclamations.chikayat.entity.ReclamationSummary(" +
                        " r.numInscription, r.dateDepot, r.nomComplet, r.objetReclamation, " +
                        " CASE WHEN (SELECT COUNT(d) FROM com.reclamations.chikayat.entity.ReclamationDetail d " +
                        "        WHERE d.reclamation = r AND d.flagEnvoiAutorite = 1) > 0 THEN 'تم إرساله' ELSE 'لم ترسل' END, "
                        +
                        " CASE WHEN (SELECT COUNT(d) FROM com.reclamations.chikayat.entity.ReclamationDetail d " +
                        "        WHERE d.reclamation = r AND d.flagRetourAutorite = 1) > 0 THEN 'تم إرساله' ELSE 'لم ترسل' END, "
                        +
                        " CASE WHEN (SELECT COUNT(d) FROM com.reclamations.chikayat.entity.ReclamationDetail d " +
                        "        WHERE d.reclamation = r AND d.flagEnvoiReclamant = 1) > 0 THEN 'تم إرساله' ELSE 'لم ترسل' END"
                        +
                        ") FROM Reclamation r WHERE r.identifiant = :identifiant")
        List<ReclamationSummary> getReclamationSummaryByIdentifiant(@Param("identifiant") String identifiant);

        @Query("SELECT DISTINCT r FROM Reclamation r " +
                        "WHERE r.flagCloture is NULL")
        List<Reclamation> findEnCours();

        @Query("SELECT DISTINCT r FROM Reclamation r JOIN r.details d " +
                        "WHERE d.flagEnvoiAutorite = 1 AND d.flagRetourAutorite is NULL AND r.flagCloture is NULL")
        List<Reclamation> findEnvoyeesAutorite();

        @Query("SELECT DISTINCT r FROM Reclamation r JOIN r.details d " +
                        "WHERE d.flagRetourAutorite = 1 AND r.flagEnvoiReclamant is NULL")
        List<Reclamation> findReponduAutorite();

        @Query("SELECT DISTINCT r FROM Reclamation r JOIN r.details d " +
                        "WHERE d.flagEnvoiReclamant = 1")
        List<Reclamation> findCloturees();

        @Query("SELECT DISTINCT r FROM Reclamation r LEFT JOIN FETCH r.details d " +
                        "WHERE d.flagEnvoiAutorite = 1 AND d.flagRetourAutorite is NULL AND r.flagCloture is NULL")
        List<Reclamation> findReclamationsEnvoyeesAvecDetails();

        @Query("SELECT r FROM Reclamation r " +
                        "WHERE r.numInscription LIKE CONCAT('%', :numInscription, '%') " +
                        "  AND r.referenceBo LIKE CONCAT('%', :referenceBo, '%') " +
                        "  AND r.flagCloture = 1")
        List<Reclamation> searchOuverture(@Param("numInscription") String numInscription,
                        @Param("referenceBo") String referenceBo);

        @Query("SELECT r FROM Reclamation r " +
                        "WHERE r.numInscription LIKE CONCAT('%', :numInscription, '%') " +
                        "  AND r.referenceBo LIKE CONCAT('%', :referenceBo, '%') " +
                        "  AND r.flagCloture IS NULL " +
                        "  AND r.flagEnvoiReclamant = 1")
        List<Reclamation> searchCloture(@Param("numInscription") String numInscription,
                        @Param("referenceBo") String referenceBo);

}
