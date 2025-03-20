package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.TypeReclamation;
import com.reclamations.chikayat.repository.TypeReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TypeReclamationService {

    @Autowired
    private TypeReclamationRepository TypeReclamationRepository;

    public List<TypeReclamation> findAll() {
        return TypeReclamationRepository.findAll();
    }

    public TypeReclamation save(TypeReclamation TypeReclamation) {
        return TypeReclamationRepository.save(TypeReclamation);
    }

    public Optional<TypeReclamation> findById(Integer id) {
        return TypeReclamationRepository.findById(id);
    }

    public TypeReclamation update(TypeReclamation TypeReclamation) {
        return TypeReclamationRepository.save(TypeReclamation);
    }

    public void delete(Integer id) {
        TypeReclamationRepository.deleteById(id);
    }

    public List<TypeReclamation> search(String keyword) {
        return TypeReclamationRepository.findByNomLangue2Containing(keyword);
    }
}
