package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.Territoire;
import com.reclamations.chikayat.repository.TerritoireRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class TerritoireService {

    @Autowired
    private TerritoireRepository territoireRepository;

    public List<Territoire> findAll() {
        return territoireRepository.findAll();
    }

    public Territoire save(Territoire territoire) {
        return territoireRepository.save(territoire);
    }

    public Optional<Territoire> findById(Integer id) {
        return territoireRepository.findById(id);
    }

    public Territoire update(Territoire territoire) {
        return territoireRepository.save(territoire);
    }

    public void delete(Integer id) {
        territoireRepository.deleteById(id);
    }

    public List<Territoire> search(String keyword) {
        return territoireRepository.searchAll(keyword);
    }
}
