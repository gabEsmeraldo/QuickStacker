package com.QuickStacker.back.service;

import com.QuickStacker.back.entity.Insumo;
import com.QuickStacker.back.entity.LoteInsumo;
import com.QuickStacker.back.repository.InsumoRepository;
import com.QuickStacker.back.repository.LoteInsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoteInsumoService {

    @Autowired
    private LoteInsumoRepository repository;

    @Autowired
    private InsumoRepository insumoRepository;

    public List<LoteInsumo> findAll() {
        return repository.findAll();
    }

    public Optional<LoteInsumo> findById(String id) {
        return repository.findById(id);
    }

    public List<LoteInsumo> findByInsumoId(Integer insumoId) {
        return repository.findByInsumoIdInsumo(insumoId);
    }

    public List<LoteInsumo> findByDataChegadaBetween(LocalDate start, LocalDate end) {
        return repository.findByDataChegadaBetween(start, end);
    }

    public LoteInsumo create(LoteInsumo lote) {
        // Validate insumo exists
        if (lote.getInsumo() != null && lote.getInsumo().getIdInsumo() != null) {
            Insumo insumo = insumoRepository.findById(lote.getInsumo().getIdInsumo())
                .orElseThrow(() -> new IllegalArgumentException("Insumo not found with id: " + lote.getInsumo().getIdInsumo()));
            lote.setInsumo(insumo);
        }
        
        LoteInsumo saved = repository.save(lote);
        
        // Update insumo custo unitario
        if (saved.getInsumo() != null) {
            Double custoUnitario = saved.getInsumo().calculateCustoUnitario();
            if (custoUnitario != null) {
                saved.getInsumo().setCustoUnitario(custoUnitario);
                insumoRepository.save(saved.getInsumo());
            }
        }
        
        return saved;
    }

    public LoteInsumo update(String id, LoteInsumo lote) {
        LoteInsumo existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("LoteInsumo not found with id: " + id));
        
        existing.setDataChegada(lote.getDataChegada());
        existing.setQuantidade(lote.getQuantidade());
        existing.setCustoDeCompra(lote.getCustoDeCompra());
        
        // Update insumo if provided
        if (lote.getInsumo() != null && lote.getInsumo().getIdInsumo() != null) {
            Insumo insumo = insumoRepository.findById(lote.getInsumo().getIdInsumo())
                .orElseThrow(() -> new IllegalArgumentException("Insumo not found with id: " + lote.getInsumo().getIdInsumo()));
            existing.setInsumo(insumo);
        }
        
        LoteInsumo updated = repository.save(existing);
        
        // Update insumo custo unitario
        if (updated.getInsumo() != null) {
            Double custoUnitario = updated.getInsumo().calculateCustoUnitario();
            if (custoUnitario != null) {
                updated.getInsumo().setCustoUnitario(custoUnitario);
                insumoRepository.save(updated.getInsumo());
            }
        }
        
        return updated;
    }

    public void delete(String id) {
        LoteInsumo lote = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("LoteInsumo not found with id: " + id));
        
        Insumo insumo = lote.getInsumo();
        
        repository.deleteById(id);
        
        // Update insumo custo unitario after deletion
        if (insumo != null) {
            Double custoUnitario = insumo.calculateCustoUnitario();
            if (custoUnitario != null) {
                insumo.setCustoUnitario(custoUnitario);
                insumoRepository.save(insumo);
            }
        }
    }
}

