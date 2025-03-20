package com.reclamations.chikayat.service;

import com.reclamations.chikayat.entity.VoieReponse;
import com.reclamations.chikayat.repository.VoieReponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class VoieReponseService {

    @Autowired
    private VoieReponseRepository VoieReponseRepository;

    public List<VoieReponse> findAll() {
        return VoieReponseRepository.findAll();
    }

    public VoieReponse save(VoieReponse VoieReponse) {
        return VoieReponseRepository.save(VoieReponse);
    }

    public Optional<VoieReponse> findById(Integer id) {
        return VoieReponseRepository.findById(id);
    }

    public VoieReponse update(VoieReponse VoieReponse) {
        return VoieReponseRepository.save(VoieReponse);
    }

    public void delete(Integer id) {
        VoieReponseRepository.deleteById(id);
    }

    public List<VoieReponse> search(String keyword) {
        return VoieReponseRepository.findByNomLangue2Containing(keyword);
    }
}
