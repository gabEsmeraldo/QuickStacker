package com.QuickStacker.back.service;

import com.QuickStacker.back.entity.LoteMateriaPrima;
import com.QuickStacker.back.entity.MateriaPrima;
import com.QuickStacker.back.repository.LoteMateriaPrimaRepository;
import com.QuickStacker.back.repository.MateriaPrimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LoteMateriaPrimaService {

    @Autowired
    private LoteMateriaPrimaRepository repository;

    @Autowired
    private MateriaPrimaRepository materiaPrimaRepository;

    public List<LoteMateriaPrima> findAll() {
        return repository.findAll();
    }

    public Optional<LoteMateriaPrima> findById(String id) {
        return repository.findById(id);
    }

    public List<LoteMateriaPrima> findByMateriaPrimaId(Integer materiaPrimaId) {
        return repository.findByMateriaPrimaIdMateriaPrima(materiaPrimaId);
    }

    public List<LoteMateriaPrima> findExpired() {
        return repository.findByDataValidadeBefore(LocalDate.now());
    }

    public List<LoteMateriaPrima> findExpiringBetween(LocalDate start, LocalDate end) {
        return repository.findByDataValidadeBetween(start, end);
    }

    public List<LoteMateriaPrima> findByFornecedor(String fornecedor) {
        return repository.findByFornecedor(fornecedor);
    }

    public LoteMateriaPrima create(LoteMateriaPrima lote) {
        // Validate materia prima exists
        if (lote.getMateriaPrima() != null && lote.getMateriaPrima().getIdMateriaPrima() != null) {
            MateriaPrima materiaPrima = materiaPrimaRepository.findById(lote.getMateriaPrima().getIdMateriaPrima())
                .orElseThrow(() -> new IllegalArgumentException("MateriaPrima not found with id: " + lote.getMateriaPrima().getIdMateriaPrima()));
            lote.setMateriaPrima(materiaPrima);
        }
        
        return repository.save(lote);
    }

    public LoteMateriaPrima update(String id, LoteMateriaPrima lote) {
        LoteMateriaPrima existing = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("LoteMateriaPrima not found with id: " + id));
        
        existing.setDataValidade(lote.getDataValidade());
        existing.setDataChegada(lote.getDataChegada());
        existing.setCustoDeCompra(lote.getCustoDeCompra());
        existing.setQuantidadeUnidades(lote.getQuantidadeUnidades());
        existing.setFornecedor(lote.getFornecedor());
        existing.setNumeroNotaFiscal(lote.getNumeroNotaFiscal());
        existing.setQuantidadeDeCaixas(lote.getQuantidadeDeCaixas());
        existing.setLaudo(lote.getLaudo());
        existing.setResponsavelRecebimento(lote.getResponsavelRecebimento());
        
        // Update materia prima if provided
        if (lote.getMateriaPrima() != null && lote.getMateriaPrima().getIdMateriaPrima() != null) {
            MateriaPrima materiaPrima = materiaPrimaRepository.findById(lote.getMateriaPrima().getIdMateriaPrima())
                .orElseThrow(() -> new IllegalArgumentException("MateriaPrima not found with id: " + lote.getMateriaPrima().getIdMateriaPrima()));
            existing.setMateriaPrima(materiaPrima);
        }
        
        return repository.save(existing);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("LoteMateriaPrima not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

