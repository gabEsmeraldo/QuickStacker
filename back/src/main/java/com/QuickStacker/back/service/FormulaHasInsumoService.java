package com.QuickStacker.back.service;

import com.QuickStacker.back.entity.Formula;
import com.QuickStacker.back.entity.FormulaHasInsumo;
import com.QuickStacker.back.entity.FormulaHasInsumoId;
import com.QuickStacker.back.entity.Insumo;
import com.QuickStacker.back.repository.FormulaHasInsumoRepository;
import com.QuickStacker.back.repository.FormulaRepository;
import com.QuickStacker.back.repository.InsumoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FormulaHasInsumoService {

    @Autowired
    private FormulaHasInsumoRepository repository;

    @Autowired
    private FormulaRepository formulaRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    public List<FormulaHasInsumo> findAll() {
        return repository.findAll();
    }

    public Optional<FormulaHasInsumo> findById(Integer formulaId, Integer insumoId) {
        return repository.findByFormulaIdFormulaAndInsumoIdInsumo(formulaId, insumoId);
    }

    public List<FormulaHasInsumo> findByFormulaId(Integer formulaId) {
        return repository.findByFormulaIdFormula(formulaId);
    }

    public List<FormulaHasInsumo> findByInsumoId(Integer insumoId) {
        return repository.findByInsumoIdInsumo(insumoId);
    }

    public FormulaHasInsumo create(Integer formulaId, Integer insumoId, Integer quantidadeUtilizada) {
        Formula formula = formulaRepository.findById(formulaId)
            .orElseThrow(() -> new IllegalArgumentException("Formula not found with id: " + formulaId));
        
        Insumo insumo = insumoRepository.findById(insumoId)
            .orElseThrow(() -> new IllegalArgumentException("Insumo not found with id: " + insumoId));

        FormulaHasInsumo entity = new FormulaHasInsumo(formula, insumo, quantidadeUtilizada);
        return repository.save(entity);
    }

    public FormulaHasInsumo update(Integer formulaId, Integer insumoId, Integer quantidadeUtilizada) {
        FormulaHasInsumo entity = repository.findByFormulaIdFormulaAndInsumoIdInsumo(formulaId, insumoId)
            .orElseThrow(() -> new IllegalArgumentException(
                "FormulaHasInsumo not found with formulaId: " + formulaId + " and insumoId: " + insumoId));

        entity.setQuantidadeUtilizada(quantidadeUtilizada);

        return repository.save(entity);
    }

    public void delete(Integer formulaId, Integer insumoId) {
        FormulaHasInsumoId id = new FormulaHasInsumoId(formulaId, insumoId);
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException(
                "FormulaHasInsumo not found with formulaId: " + formulaId + " and insumoId: " + insumoId);
        }
        repository.deleteById(id);
    }

    public void deleteByFormulaId(Integer formulaId) {
        repository.deleteByFormulaIdFormula(formulaId);
    }

    public void deleteByInsumoId(Integer insumoId) {
        repository.deleteByInsumoIdInsumo(insumoId);
    }
}

