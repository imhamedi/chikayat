package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.TypeRequete;
import com.reclamations.chikayat.repository.TypeRequeteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TypeRequeteService {

    @Autowired
    private TypeRequeteRepository TypeRequeteRepository;

    public List<TypeRequete> findAll() {
        return TypeRequeteRepository.findAll();
    }

    public TypeRequete save(TypeRequete TypeRequete) {
        return TypeRequeteRepository.save(TypeRequete);
    }

    public Optional<TypeRequete> findById(Integer id) {
        return TypeRequeteRepository.findById(id);
    }

    public TypeRequete update(TypeRequete TypeRequete) {
        return TypeRequeteRepository.save(TypeRequete);
    }

    public void delete(Integer id) {
        TypeRequeteRepository.deleteById(id);
    }

    public List<TypeRequete> search(String keyword) {
        return TypeRequeteRepository.findByNomLangue2Containing(keyword);
    }
}
