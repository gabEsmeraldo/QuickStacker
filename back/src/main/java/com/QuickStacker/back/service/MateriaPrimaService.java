package com.QuickStacker.back.service;

import com.QuickStacker.back.entity.MateriaPrima;
import com.QuickStacker.back.repository.MateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MateriaPrimaService {

    @Autowired
    private MateriaPrimaRepository repository;

    public List<MateriaPrima> findAll() {
        return repository.findAll();
    }

    public Optional<MateriaPrima> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<MateriaPrima> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    public MateriaPrima create(MateriaPrima materiaPrima) {
        return repository.save(materiaPrima);
    }

    public MateriaPrima update(Integer id, MateriaPrima materiaPrima) {
        MateriaPrima existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("MateriaPrima not found with id: " + id));
        
        existing.setNome(materiaPrima.getNome());
        existing.setDensidade(materiaPrima.getDensidade());
        existing.setPesoUnitario(materiaPrima.getPesoUnitario());
        
        return repository.save(existing);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("MateriaPrima not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

