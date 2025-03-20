package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.SourceReclamation;
import com.reclamations.chikayat.repository.SourceReclamationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class SourceReclamationService {

    @Autowired
    private SourceReclamationRepository sourceReclamationRepository;

    public List<SourceReclamation> findAll() {
        return sourceReclamationRepository.findAll();
    }

    public SourceReclamation save(SourceReclamation sourceReclamation) {
        return sourceReclamationRepository.save(sourceReclamation);
    }

    public Optional<SourceReclamation> findById(Integer id) {
        return sourceReclamationRepository.findById(id);
    }

    public SourceReclamation update(SourceReclamation sourceReclamation) {
        return sourceReclamationRepository.save(sourceReclamation);
    }

    public void delete(Integer id) {
        sourceReclamationRepository.deleteById(id);
    }

    public List<SourceReclamation> search(String keyword) {
        return sourceReclamationRepository.findByNomLangue2Containing(keyword);
    }
}
