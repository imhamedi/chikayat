package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.TypeDestinataire;
import com.reclamations.chikayat.repository.TypeDestinataireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TypeDestinataireService {

    @Autowired
    private TypeDestinataireRepository TypeDestinataireRepository;

    public List<TypeDestinataire> findAll() {
        return TypeDestinataireRepository.findAll();
    }

    public TypeDestinataire save(TypeDestinataire TypeDestinataire) {
        return TypeDestinataireRepository.save(TypeDestinataire);
    }

    public Optional<TypeDestinataire> findById(Integer id) {
        return TypeDestinataireRepository.findById(id);
    }

    public TypeDestinataire update(TypeDestinataire TypeDestinataire) {
        return TypeDestinataireRepository.save(TypeDestinataire);
    }

    public void delete(Integer id) {
        TypeDestinataireRepository.deleteById(id);
    }

    public List<TypeDestinataire> search(String keyword) {
        return TypeDestinataireRepository.findByNomLangue2Containing(keyword);
    }
}
