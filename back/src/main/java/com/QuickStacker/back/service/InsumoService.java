package com.QuickStacker.back.service;

import com.QuickStacker.back.entity.Insumo;
import com.QuickStacker.back.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class InsumoService {

    @Autowired
    private InsumoRepository repository;

    public List<Insumo> findAll() {
        return repository.findAll();
    }

    public Optional<Insumo> findById(Integer id) {
        return repository.findById(id);
    }

    public Optional<Insumo> findByNome(String nome) {
        return repository.findByNome(nome);
    }

    public Insumo create(Insumo insumo) {
        // Calculate custo unitario if lotes are available
        if (insumo.getLotesInsumo() != null && !insumo.getLotesInsumo().isEmpty()) {
            Double custoUnitario = insumo.calculateCustoUnitario();
            if (custoUnitario != null) {
                insumo.setCustoUnitario(custoUnitario);
            }
        }
        
        return repository.save(insumo);
    }

    public Insumo update(Integer id, Insumo insumo) {
        Insumo existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Insumo not found with id: " + id));
        
        existing.setNome(insumo.getNome());
        
        // Recalculate custo unitario if lotes changed
        if (existing.getLotesInsumo() != null && !existing.getLotesInsumo().isEmpty()) {
            Double custoUnitario = existing.calculateCustoUnitario();
            if (custoUnitario != null) {
                existing.setCustoUnitario(custoUnitario);
            }
        }
        
        return repository.save(existing);
    }

    public void delete(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Insumo not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

