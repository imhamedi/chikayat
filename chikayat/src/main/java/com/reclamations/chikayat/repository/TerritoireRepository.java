package com.reclamations.chikayat.repository;

import com.reclamations.chikayat.entity.Territoire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface TerritoireRepository extends JpaRepository<Territoire, Integer> {
    List<Territoire> findByNomCommuneLangue2Containing(String keyword);

    List<Territoire> findByCodeVilleContaining(String keyword);

    List<Territoire> findByNomVilleLangue2Containing(String keyword);

    List<Territoire> findByCodeCommuneContaining(String keyword);

    List<Territoire> findByCodeArrondissementContaining(String keyword);

    List<Territoire> findByNomArrondissementLangue2Containing(String keyword);

    @Query("SELECT t FROM Territoire t WHERE " +
            "LOWER(t.codeVille) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.nomVilleLangue2) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.codeCommune) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.nomCommuneLangue2) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.codeArrondissement) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.nomArrondissementLangue2) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Territoire> searchAll(@Param("keyword") String keyword);

}
